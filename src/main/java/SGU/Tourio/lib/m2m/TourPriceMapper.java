package SGU.Tourio.lib.m2m;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import SGU.Tourio.Models.Tour;
import SGU.Tourio.Models.TourPrice;

@Component
public class TourPriceMapper extends BaseMapper<TourPrice, Tour> {

    @Override
    protected TourPrice convert(JSONObject object, Tour tour) throws JSONException, ParseException {
        MappedObject obj = new MappedObject(object);
        Long amount = obj.getLong("amount");
        Date dateStart = obj.getDate("dateStart");
        Date dateEnd = obj.getDate("dateEnd");

        if (amount == null || dateStart == null || dateEnd == null) {
            return null;
        }

        TourPrice tourPrice = new TourPrice();

        tourPrice.setTour(tour);
        tourPrice.setAmount(amount);
        tourPrice.setDateStart(dateStart);
        tourPrice.setDateEnd(dateEnd);
        return tourPrice;
    }

    @Override
    protected JSONObject convertJson(TourPrice entity) {
        JSONObject object = new JSONObject();
        object.put("amount", Long.toString(entity.getAmount()));
        object.put("dateStart", entity.getDateStart());
        object.put("dateEnd", entity.getDateEnd());
        return object;
    }
}
