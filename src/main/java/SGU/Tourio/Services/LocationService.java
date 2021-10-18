package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.DTO.CreateLocationDTO;
import SGU.Tourio.Models.Location;
import SGU.Tourio.Repositories.LocationRepository;
import SGU.Tourio.Utils.FormatString;

@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location get(Long id) {
        if (id == null)
            throw new NullPointerException("ID is null");

        Optional<Location> location = locationRepository.findById(id);

        return location.orElse(null);
    }

    public Location get(String name) {
        if (name == null)
            throw new NullPointerException("Name is null");

        // Format
        name = FormatString.TitleCase(name);

        Optional<Location> location = locationRepository.findByName(name);
        return location.orElse(null);
    }

    public Location create(CreateLocationDTO dto) throws EntityExistsException{
        // Format
        dto.setName(FormatString.TitleCase(dto.getName()));

        if (locationRepository.existsByName(dto.getName())) {
            throw new EntityExistsException(dto.getName() + " existed");
        }
        Location location = new ModelMapper().map(dto, Location.class);
        return locationRepository.save(location);
    }

    public Location update(Location location) throws EntityExistsException {
        // Format
        location.setName(FormatString.TitleCase(location.getName()));
        Location isExist = get(location.getName());
        if (isExist != null && !location.getId().equals(isExist.getId())) {
            throw new EntityExistsException(location.getName() + " existed");
        }
        return locationRepository.save(location);
    }

    public void delete(Long id) {
        if (id == null)
            throw new NullPointerException("ID is null");

        if (locationRepository.existsById(id))
            locationRepository.deleteById(id);
    }
}
