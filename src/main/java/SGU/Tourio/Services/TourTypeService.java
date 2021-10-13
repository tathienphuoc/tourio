package SGU.Tourio.Services;

import SGU.Tourio.Models.TourType;
import SGU.Tourio.Repositories.TourTypeRepository;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

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

    public TourType create(TourType dto) throws EntityExistsException {
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