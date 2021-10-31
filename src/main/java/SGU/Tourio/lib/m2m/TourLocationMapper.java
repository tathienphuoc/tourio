package SGU.Tourio.lib.m2m;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import SGU.Tourio.Models.Location;
import SGU.Tourio.Models.Tour;
import SGU.Tourio.Models.TourLocationRel;
import SGU.Tourio.Repositories.LocationRepository;


@Component
public class TourLocationMapper extends BaseMapper<TourLocationRel, Tour> {
    @Autowired
    LocationRepository locationRepository;

    @Override
    protected TourLocationRel convert(JSONObject object, Tour tour) {
        MappedObject obj = new MappedObject(object);
        Long sequence = obj.getLong("sequence");
        Location location = obj.getEntity(locationRepository, "location");
        if (location == null || sequence == null)
            return null;

        return new TourLocationRel(tour, location, sequence);
    }

    @Override
    protected JSONObject convertJson(TourLocationRel entity) {
        JSONObject object = new JSONObject();
        object.put("location", entity.getLocation().getId().toString());
        object.put("sequence", entity.getSequence().toString());
        return object;
    }
}
