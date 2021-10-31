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
        Float amount = obj.getFloat("amount");
        Date dateStart = obj.getDate("startDateData");
        Date dateEnd = obj.getDate("endDateData");
        // if ( dateStart == null|| dateEnd == null ||  amount == null)
        //     return null;
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
        // object.put("costType", entity.getCostType().getId().toString());
        object.put("amount",Float.toString(entity.getAmount()));
        return object;
    }
}
