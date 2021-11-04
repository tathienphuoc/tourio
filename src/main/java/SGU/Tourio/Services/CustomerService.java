package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import SGU.Tourio.DTO.CreateTourPriceDTO;
import SGU.Tourio.Models.TourPrice;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.DTO.CreateCustomerDTO;
import SGU.Tourio.Models.Customer;
import SGU.Tourio.Repositories.CustomerRepository;
import javassist.NotFoundException;

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
        System.out.println(dto);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<CreateCustomerDTO, Customer>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        Customer customer = modelMapper.map(dto, Customer.class);
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
