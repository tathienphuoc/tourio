package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.Models.Location;
import SGU.Tourio.Repositories.LocationRepository;
import javassist.NotFoundException;

@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location get(Long id) {
        if(id ==null) return null;
        Optional<Location> location = locationRepository.findById(id);
        return location.orElse(null);
    }

    public Location create(Location location) throws EntityExistsException {
        // Location isExist = get(location.getId());
        // if (isExist != null) {
        //     throw new EntityExistsException("Existed");
        // }
        return locationRepository.save(location);
    }

    public Location update(Location location) throws NotFoundException {
        Location isExist = get(location.getId());
        if (isExist == null) {
            throw new NotFoundException("Not Existed");
        }
        return locationRepository.save(location);
    }

    public void delete(Long id) {
        Location location = get(id);
        if (location != null)
            locationRepository.delete(location);
    }
}
