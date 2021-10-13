package SGU.Tourio.Services;

import SGU.Tourio.DTO.CreateCustomerDTO;
import SGU.Tourio.Models.Customer;
import SGU.Tourio.Repositories.CustomerRepository;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer get(long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    public Customer create(CreateCustomerDTO dto) throws EntityExistsException {
        Customer customer = new ModelMapper().map(dto, Customer.class);
        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) throws NotFoundException {
        if (!customerRepository.existsById(customer.getId())) {
            throw new NotFoundException("Not Existed");
        }
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        if (customerRepository.existsById(id))
            customerRepository.deleteById(id);
    }
}
