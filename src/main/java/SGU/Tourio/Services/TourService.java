package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.DTO.CreateTourDTO;
import SGU.Tourio.DTO.UpdateTourDTO;
import SGU.Tourio.Models.Tour;
import SGU.Tourio.Models.TourLocationRel;
import SGU.Tourio.Models.TourPrice;
import SGU.Tourio.Repositories.CustomerRepository;
import SGU.Tourio.Repositories.TourLocationRelRepository;
import SGU.Tourio.Repositories.TourPriceRepository;
import SGU.Tourio.Repositories.TourRepository;
import SGU.Tourio.Repositories.TourTypeRepository;
import SGU.Tourio.Utils.FormatString;
import SGU.Tourio.lib.m2m.TourLocationMapper;
import SGU.Tourio.lib.m2m.TourPriceMapper;
import javassist.NotFoundException;

@Service
public class TourService {
    @Autowired
    TourRepository tourRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TourPriceRepository tourPriceRepository;

    @Autowired
    TourLocationRelRepository tourLocationRelRepository;

    // @Autowired
    // TourPriceRelRepository tourPriceRelRepository;

    @Autowired
    TourLocationMapper tourLocationMapper;

    @Autowired
    TourPriceMapper tourPriceMapper;

    @Autowired
    TourTypeRepository tourTypeRepository;

    // @Autowired
    // TourPriceMapper tourPriceMapper;

    public List<Tour> getAll() {
        Tour t=new Tour();
        return tourRepository.findAll();
    }

    // public List<ViewTourDTO> getAllForView(Optional<String> from, Optional<String> to) throws ParseException {
    //     List<Tour> tours;

    //     if (from.isPresent() && to.isPresent()) {
    //         Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from.get());
    //         Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to.get());
    //         tours = tourRepository.findAllByDateStartBetween(fromDate, toDate);
    //     } else {
    //         tours = getAll();
    //     }

    //     List<ViewTourDTO> dtoList = new ArrayList<>();
    //     for (Tour tour : tours) {
    //         ViewTourDTO dto = new ModelMapper().map(tour, ViewTourDTO.class);
    //         dto.setCustomerCount(tour.getCustomers().size());
    //         dto.setEmployeeCount(tour.getTourEmployeeRels().size());
    //         dto.setPriceTotal(tour.getTourPriceRels().stream().map(TourPriceRel::getAmount).reduce(0L, Long::sum));
    //         dtoList.add(dto);
    //     }
    //     return dtoList;
    // }

    public Tour get(Long id) {
        if (id == null)
            throw new NullPointerException("ID is null");

        Optional<Tour> tour = tourRepository.findById(id);

        return tour.orElse(null);
    }

    public Tour create(CreateTourDTO dto) throws Exception {

        // Format
        dto.setName(FormatString.TitleCase(dto.getName()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<CreateTourDTO, Tour>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        Tour tour = modelMapper.map(dto, Tour.class);
        tour.setTourType(tourTypeRepository.getById(dto.getTourTypeId()));
        Tour created = tourRepository.save(tour);

        List<TourLocationRel> locations = tourLocationMapper.toEntities(dto.getLocationData(), created);
        tourLocationRelRepository.saveAll(locations);
        
        List<TourPrice> prices = tourPriceMapper.toEntities(dto.getTourPriceData(), created);
        tourPriceRepository.saveAll(prices);
        return created;
    }

    public Tour update(UpdateTourDTO dto) throws Exception {
        Optional<Tour> existed = tourRepository.findById(dto.getId());

        if (!existed.isPresent()) {
            throw new NotFoundException("Not Existed");
        }
        Tour tour = new ModelMapper().map(dto, Tour.class);

        tourPriceRepository.deleteAll(existed.get().getTourPrices());

        List<TourPrice> prices = tourPriceMapper.toEntities(dto.getTourPriceData(), tour);
        try {
            tourPriceRepository.saveAll(prices);
        } catch (Exception e) {
            tourPriceRepository.saveAll(existed.get().getTourPrices());
            throw e;
        }
        return tourRepository.save(tour);
    }

    public void delete(Long id) {
        if (tourRepository.existsById(id))
            tourRepository.deleteById(id);
    }
}
