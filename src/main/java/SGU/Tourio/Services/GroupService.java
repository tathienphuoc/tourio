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
import java.util.*;

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
        group.setTourPrice(tour.getPriceByDate(group.getDateStart()));

        List<Customer> customers = customerRepository.findAllById(dto.getCustomerIds());
        group.setCustomers(customers);


        List<GroupEmployeeRel> employees = groupEmpMapper.toEntities(dto.getEmployeeData(), group);
        List<GroupCostRel> costs = groupCostMapper.toEntities(dto.getCostData(), group);

        if (duplicatedEmployee(employees)) {
            throw new Exception("Duplicated employee!");
        }

        group.setGroupEmployeeRels(employees);
        group.setGroupCostRels(costs);

        return groupRepository.save(group);
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

        Tour tour = tourRepository.getById(dto.getTourId());

        group.setTour(tour);
        group.setTourPrice(tour.getPriceByDate(group.getDateStart()));

        if (dto.getCustomerIds() != null) {
            List<Customer> customers = customerRepository.findAllById(dto.getCustomerIds());
            group.setCustomers(customers);
        }

        List<GroupEmployeeRel> employees = groupEmpMapper.toEntities(dto.getEmployeeData(), group);
        List<GroupCostRel> costs = groupCostMapper.toEntities(dto.getCostData(), group);

        if (duplicatedEmployee(employees)) {
            throw new Exception("Duplicated employee!");
        }

        group.setGroupCostRels(costs);
        group.setGroupEmployeeRels(employees);

        return groupRepository.save(group);
    }

    private boolean duplicatedEmployee(List<GroupEmployeeRel> employees) {
        Set<Long> employeeSet = new HashSet<>();
        for (GroupEmployeeRel employeeRel : employees) {
            if (employeeSet.contains(employeeRel.getEmployee().getId())) {
                return true;
            }
            employeeSet.add(employeeRel.getEmployee().getId());
        }
        return false;
    }

    public void delete(Long id) {
        if (groupRepository.existsById(id))
            groupRepository.deleteById(id);
    }
}
