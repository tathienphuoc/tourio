package SGU.Tourio.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import SGU.Tourio.DTO.ReportTourDTO;
import SGU.Tourio.Models.Group;
import SGU.Tourio.Repositories.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.DTO.CreateTourDTO;
import SGU.Tourio.DTO.UpdateTourDTO;
import SGU.Tourio.Models.Tour;
import SGU.Tourio.Models.TourLocationRel;
import SGU.Tourio.Models.TourPrice;
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

    @Autowired
    GroupRepository groupRepository;

    // @Autowired
    // TourPriceMapper tourPriceMapper;

    public List<Tour> getAll() {
        Tour t = new Tour();
        return tourRepository.findAll();
    }

    public List<ReportTourDTO> getForSaleReport(Optional<String> from, Optional<String> to) throws ParseException {
        List<Tour> tours = getAll();
        List<ReportTourDTO> dtoList = new ArrayList<>();
        for (Tour tour : tours) {
            ReportTourDTO dto = new ModelMapper().map(tour, ReportTourDTO.class);
            List<Group> groups;
            if (from.isPresent() && to.isPresent()) {
                Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from.get());
                Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to.get());
                groups = groupRepository.findAllByTourAndCreatedAtBetween(tour, fromDate, toDate);
            } else {
                groups = groupRepository.findAllByTour(tour);
            }
            dto.setTotalSale(groups.stream().map(Group::getTotalSale).reduce(0L, Long::sum));
            dto.setTotalCost(groups.stream().map(Group::getTotalCost).reduce(0L, Long::sum));
            float revenue = 0;
            if (dto.getTotalSale() > 0) {
                revenue = (dto.getTotalSale() - dto.getTotalCost()) / (float) (dto.getTotalSale());
            }
            dto.setRevenue((int) (revenue * 100));
            dto.setGroupCount(groups.size());
            dtoList.add(dto);
        }
        return dtoList;
    }

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

        List<TourPrice> prices = tourPriceMapper.toEntities(dto.getTourPriceData(), tour);
        for (TourPrice price : prices) {
            if (price.getDateStart().after(price.getDateEnd())) {
                throw new Exception("Start date must before end date");
            }
        }
        tour.setTourPrices(prices);


        Tour created = tourRepository.save(tour);
        System.out.println(created);

        List<TourLocationRel> locations = tourLocationMapper.toEntities(dto.getLocationData(), created);
        tourLocationRelRepository.saveAll(locations);


        return created;
    }

    public Tour update(UpdateTourDTO dto) throws Exception {
        Optional<Tour> existed = tourRepository.findById(dto.getId());

        System.out.println(dto);

        if (!existed.isPresent()) {
            throw new NotFoundException("Not Existed");
        }
        Tour tour = new ModelMapper().map(dto, Tour.class);

        List<TourPrice> prices = tourPriceMapper.toEntities(dto.getTourPriceData(), tour);
        for (TourPrice price : prices) {
            if (price.getDateStart().after(price.getDateEnd())) {
                throw new Exception("Start date must before end date");
            }
        }
        tour.setTourPrices(prices);

        if (existed.get().getTourLocationRels() != null)
            tourLocationRelRepository.deleteAll(existed.get().getTourLocationRels());
        if (existed.get().getTourPrices() != null)
            tourPriceRepository.deleteAll(existed.get().getTourPrices());

        List<TourLocationRel> locations = tourLocationMapper.toEntities(dto.getLocationData(), tour);
        try {

            tourLocationRelRepository.saveAll(locations);
        } catch (Exception e) {
            tourLocationRelRepository.saveAll(existed.get().getTourLocationRels());
            throw e;
        }
        System.out.println(tour);
        return tourRepository.save(tour);
    }

    public void delete(Long id) {
        if (tourRepository.existsById(id))
            tourRepository.deleteById(id);
    }
}
