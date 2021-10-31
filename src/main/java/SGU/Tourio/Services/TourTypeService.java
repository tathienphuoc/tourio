package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.DTO.CreateTourTypeDTO;
import SGU.Tourio.Models.TourType;
import SGU.Tourio.Repositories.TourTypeRepository;
import javassist.NotFoundException;

@Service
public class TourTypeService {
    @Autowired
    TourTypeRepository tourTypeRepository;

    public List<TourType> getAll() {
        return tourTypeRepository.findAll();
    }

    public TourType get(long id) {
        Optional<TourType> tourType = tourTypeRepository.findById(id);
        return tourType.orElse(null);
    }

    public TourType create(CreateTourTypeDTO dto) throws EntityExistsException {
        TourType tourType = new ModelMapper().map(dto, TourType.class);
        return tourTypeRepository.save(tourType);
    }

    public TourType update(TourType dto) throws NotFoundException {
        if (!tourTypeRepository.existsById(dto.getId())) {
            throw new NotFoundException("Not Existed");
        }
        return tourTypeRepository.save(dto);
    }

    public void delete(Long id) {
        if (tourTypeRepository.existsById(id))
            tourTypeRepository.deleteById(id);
    }
}
