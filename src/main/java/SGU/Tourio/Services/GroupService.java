package SGU.Tourio.Services;

import SGU.Tourio.DTO.CreateGroupDTO;
import SGU.Tourio.DTO.UpdateGroupDTO;
import SGU.Tourio.DTO.ViewGroupDTO;
import SGU.Tourio.Models.Customer;
import SGU.Tourio.Models.Group;
import SGU.Tourio.Models.GroupCostRel;
import SGU.Tourio.Models.GroupEmployeeRel;
import SGU.Tourio.Repositories.CustomerRepository;
import SGU.Tourio.Repositories.GroupCostRelRepository;
import SGU.Tourio.Repositories.GroupEmpRelRepository;
import SGU.Tourio.Repositories.GroupRepository;
import SGU.Tourio.lib.m2m.GroupCostMapper;
import SGU.Tourio.lib.m2m.GroupEmpMapper;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public List<ViewGroupDTO> getAllForView(Optional<String> from, Optional<String> to) throws ParseException {
        List<Group> groups;

        if (from.isPresent() && to.isPresent()) {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from.get());
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to.get());
            groups = groupRepository.findAllByDateStartBetween(fromDate, toDate);
        } else {
            groups = getAll();
        }

        List<ViewGroupDTO> dtoList = new ArrayList<>();
        for (Group group : groups) {
            ViewGroupDTO dto = new ModelMapper().map(group, ViewGroupDTO.class);
            dto.setCustomerCount(group.getCustomers().size());
            dto.setEmployeeCount(group.getGroupEmployeeRels().size());
            dto.setCostTotal(group.getGroupCostRels().stream().map(GroupCostRel::getAmount).reduce(0L, Long::sum));
            dtoList.add(dto);
        }
        return dtoList;
    }

    public Group get(long id) {
        Optional<Group> group = groupRepository.findById(id);
        return group.orElse(null);
    }

    public Group create(CreateGroupDTO dto) throws Exception {
        Group group = new ModelMapper().map(dto, Group.class);

        List<Customer> customers = customerRepository.findAllById(dto.getCustomerIds());
        group.setCustomers(customers);

        Group created = groupRepository.save(group);

        List<GroupEmployeeRel> employees = groupEmpMapper.toEntities(dto.getEmployeeData(), created);
        groupEmpRelRepository.saveAll(employees);

        List<GroupCostRel> costs = groupCostMapper.toEntities(dto.getCostData(), created);
        groupCostRelRepository.saveAll(costs);

        return created;
    }

    public Group update(UpdateGroupDTO dto) throws Exception {
        Optional<Group> existed = groupRepository.findById(dto.getId());

        if (!existed.isPresent()) {
            throw new NotFoundException("Not Existed");
        }
        Group group = new ModelMapper().map(dto, Group.class);

        if (dto.getCustomerIds() != null) {
            List<Customer> customers = customerRepository.findAllById(dto.getCustomerIds());
            group.setCustomers(customers);
        }

        groupEmpRelRepository.deleteAll(existed.get().getGroupEmployeeRels());
        groupCostRelRepository.deleteAll(existed.get().getGroupCostRels());

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
