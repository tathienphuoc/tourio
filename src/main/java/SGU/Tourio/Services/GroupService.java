package SGU.Tourio.Services;

import SGU.Tourio.DTO.*;
import SGU.Tourio.Models.*;
import SGU.Tourio.Repositories.*;
import SGU.Tourio.lib.m2m.GroupCostMapper;
import SGU.Tourio.lib.m2m.GroupEmpMapper;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    GroupEmpRelRepository groupEmpRelRepository;

    @Autowired
    GroupCostRelRepository groupCostRelRepository;

    @Autowired
    GroupEmpMapper groupEmpMapper;

    @Autowired
    GroupCostMapper groupCostMapper;

    @Autowired
    TourRepository tourRepository;

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public List<ViewGroupDTO> getAllForView(Optional<String> from, Optional<String> to) throws ParseException {
        List<Group> groups;

        if (from.isPresent() && to.isPresent()) {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from.get());
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to.get());
            groups = groupRepository.findAllByCreatedAtBetween(fromDate, toDate);
        } else {
            groups = getAll();
        }

        List<ViewGroupDTO> dtoList = new ArrayList<>();
        for (Group group : groups) {
            ViewGroupDTO dto = new ModelMapper().map(group, ViewGroupDTO.class);
            dto.setTourName(group.getTour().getName());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<ReportGroupDTO> getForReport(Optional<String> from, Optional<String> to) throws ParseException {
        List<Group> groups;

        if (from.isPresent() && to.isPresent()) {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from.get());
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to.get());
            groups = groupRepository.findAllByCreatedAtBetween(fromDate, toDate);
        } else {
            groups = getAll();
        }

        List<ReportGroupDTO> dtoList = new ArrayList<>();
        for (Group group : groups) {
            ReportGroupDTO dto = new ModelMapper().map(group, ReportGroupDTO.class);
            dto.setCustomerCount(group.getCustomers().size());
            dto.setEmployeeCount(group.getGroupEmployeeRels().size());
            dto.setTotalCost(group.getGroupCostRels().stream().map(GroupCostRel::getAmount).reduce(0L, Long::sum));
            dto.setTotalSale(group.getCustomers().size() * group.getTourPrice());
            float revenue = 0;
            if (dto.getTotalSale() > 0) {
                revenue = (dto.getTotalSale() - dto.getTotalCost()) / (float) (dto.getTotalSale());
            }
            dto.setRevenue((int) (revenue * 100));
            dto.setTourName(group.getTour().getName());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public Group get(long id) {
        Optional<Group> group = groupRepository.findById(id);
        return group.orElse(null);
    }

    public Group create(CreateGroupDTO dto) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<CreateGroupDTO, Group>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        Group group = modelMapper.map(dto, Group.class);

        if (group.getDateStart().after(group.getDateEnd())) {
            throw new Exception("Start date must before end date");
        }
        group.setCreatedAt(new Date());

        Tour tour = tourRepository.getById(dto.getTourId());
        group.setTour(tour);
        group.setTourPrice(tour.getCurrentPrice());


        List<Customer> customers = customerRepository.findAllById(dto.getCustomerIds());
        group.setCustomers(customers);

        Group created = groupRepository.save(group);

        List<GroupEmployeeRel> employees = groupEmpMapper.toEntities(dto.getEmployeeData(), created);
        groupEmpRelRepository.saveAll(employees);

        List<GroupCostRel> costs = groupCostMapper.toEntities(dto.getCostData(), created);
        groupCostRelRepository.saveAll(costs);

        return created;
    }

    @Transactional
    public Group update(UpdateGroupDTO dto) throws Exception {
        Optional<Group> existed = groupRepository.findById(dto.getId());

        if (!existed.isPresent()) {
            throw new NotFoundException("Not Existed");
        }
        Group group = new ModelMapper().map(dto, Group.class);

        if (group.getDateStart().after(group.getDateEnd())) {
            throw new Exception("Start date must before end date");
        }

        group.setCreatedAt(existed.get().getCreatedAt());
        group.setTourPrice(existed.get().getTourPrice());

        group.setTour(tourRepository.getById(dto.getTourId()));

        if (dto.getCustomerIds() != null) {
            List<Customer> customers = customerRepository.findAllById(dto.getCustomerIds());
            group.setCustomers(customers);
        }

        groupEmpRelRepository.deleteAll(existed.get().getGroupEmployeeRels());
        groupCostRelRepository.deleteAll(existed.get().getGroupCostRels());
        groupCostRelRepository.flush();
        groupEmpRelRepository.flush();
        List<GroupEmployeeRel> employees = groupEmpMapper.toEntities(dto.getEmployeeData(), group);
        List<GroupCostRel> costs = groupCostMapper.toEntities(dto.getCostData(), group);
        try {
            groupEmpRelRepository.saveAll(employees);
            groupCostRelRepository.saveAll(costs);
        } catch (Exception e) {
            groupEmpRelRepository.saveAll(existed.get().getGroupEmployeeRels());
            groupCostRelRepository.saveAll(existed.get().getGroupCostRels());
            throw e;
        }
        return groupRepository.save(group);
    }

    public void delete(Long id) {
        if (groupRepository.existsById(id))
            groupRepository.deleteById(id);
    }
}
