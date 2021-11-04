package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import SGU.Tourio.DTO.CreateGroupDTO;
import SGU.Tourio.Models.Group;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.DTO.CreateTourPriceDTO;
import SGU.Tourio.Models.TourPrice;
import SGU.Tourio.Repositories.TourPriceRepository;

@Service
public class TourPriceService {
    @Autowired
    TourPriceRepository tourPriceRepository;

    public List<TourPrice> getAll() {
        return tourPriceRepository.findAll();
    }

    public TourPrice get(Long id) {
        if (id == null)
            throw new NullPointerException("ID is null");

        Optional<TourPrice> tourPrice = tourPriceRepository.findById(id);

        return tourPrice.orElse(null);
    }

    public TourPrice create(CreateTourPriceDTO dto) throws EntityExistsException {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<CreateTourPriceDTO, TourPrice>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        TourPrice tourPrice = modelMapper.map(dto, TourPrice.class);

        return tourPriceRepository.save(tourPrice);
    }

    public TourPrice update(TourPrice tourPrice) throws EntityExistsException {
        return tourPriceRepository.save(tourPrice);
    }

    public void delete(Long id) {
        if (id == null)
            throw new NullPointerException("ID is null");

        if (tourPriceRepository.existsById(id))
            tourPriceRepository.deleteById(id);
    }
}
