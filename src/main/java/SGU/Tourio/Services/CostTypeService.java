package SGU.Tourio.Services;

import SGU.Tourio.DTO.CreateCostTypeDTO;
import SGU.Tourio.Models.CostType;
import SGU.Tourio.Repositories.CostTypeRepository;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
public class CostTypeService {
    @Autowired
    CostTypeRepository costTypeRepository;

    public List<CostType> getAll() {
        return costTypeRepository.findAll();
    }

    public CostType get(long id) {
        Optional<CostType> costType = costTypeRepository.findById(id);
        return costType.orElse(null);
    }

    public CostType create(CreateCostTypeDTO dto) throws EntityExistsException {
        CostType costType = new ModelMapper().map(dto, CostType.class);
        return costTypeRepository.save(costType);
    }

    public CostType update(CostType dto) throws NotFoundException {
        if (!costTypeRepository.existsById(dto.getId())) {
            throw new NotFoundException("Not Existed");
        }
        return costTypeRepository.save(dto);
    }

    public void delete(Long id) {
        if (costTypeRepository.existsById(id))
            costTypeRepository.deleteById(id);
    }
}
