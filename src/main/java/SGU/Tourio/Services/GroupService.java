package SGU.Tourio.Services;

import SGU.Tourio.DTO.CreateGroupDTO;
import SGU.Tourio.DTO.UpdateGroupDTO;
import SGU.Tourio.Models.Customer;
import SGU.Tourio.Models.Group;
import SGU.Tourio.Repositories.CustomerRepository;
import SGU.Tourio.Repositories.GroupRepository;
import javassist.NotFoundException;
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

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public Group get(long id) {
        Optional<Group> group = groupRepository.findById(id);
        return group.orElse(null);
    }

    public Group create(CreateGroupDTO dto) throws EntityExistsException {
        System.out.println(dto);
        Group group = new ModelMapper().map(dto, Group.class);

        List<Customer> customers = customerRepository.findAllById(dto.getCustomerIds());
        group.setCustomers(customers);

        return groupRepository.save(group);
    }

    public Group update(UpdateGroupDTO dto) throws NotFoundException {
        if (!groupRepository.existsById(dto.getId())) {
            throw new NotFoundException("Not Existed");
        }
        Group group = new ModelMapper().map(dto, Group.class);

        List<Customer> customers = customerRepository.findAllById(dto.getCustomerIds());
        group.setCustomers(customers);

        return groupRepository.save(group);
    }

    public void delete(Long id) {
        if (groupRepository.existsById(id))
            groupRepository.deleteById(id);
    }
}
