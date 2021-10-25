package SGU.Tourio.Services;

import SGU.Tourio.DTO.CreateGroupDTO;
import SGU.Tourio.DTO.UpdateGroupDTO;
import SGU.Tourio.Models.Customer;
import SGU.Tourio.Models.Group;
import SGU.Tourio.Models.GroupEmployeeRel;
import SGU.Tourio.Repositories.CustomerRepository;
import SGU.Tourio.Repositories.GroupEmpRelRepository;
import SGU.Tourio.Repositories.GroupRepository;
import SGU.Tourio.lib.m2m.GroupEmpMapper;
import javassist.NotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    GroupEmpMapper groupEmpMapper;

    @Autowired
    GroupEmpRelRepository groupEmpRelRepository;

    public List<Group> getAll() {
        return groupRepository.findAll();
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

        List<GroupEmployeeRel> employees = groupEmpMapper.toEntities(dto.getEmployeeData(), group);
        try {
            groupEmpRelRepository.saveAll(employees);
        } catch (Exception e) {
            groupEmpRelRepository.saveAll(existed.get().getGroupEmployeeRels());
            throw e;
        }
        return groupRepository.save(group);
    }

    public void delete(Long id) {
        if (groupRepository.existsById(id))
            groupRepository.deleteById(id);
    }
}
