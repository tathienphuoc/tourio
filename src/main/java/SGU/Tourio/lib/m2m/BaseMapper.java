package SGU.Tourio.lib.m2m;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMapper<Dest, Parent> {
    protected abstract Dest convert(JSONObject object, Parent parent) throws Exception;

    protected abstract JSONObject convertJson(Dest entity) throws Exception;

    public List<Dest> toEntities(String data, Parent parent) throws Exception {
        List<Dest> result = new ArrayList<>();
        if (data.isEmpty()) {
            return result;
        }
        JSONArray list = new JSONArray(data);
        for (int i = 0; i < list.length(); i++) {
            Dest entity = this.convert(list.getJSONObject(i), parent);
            if (entity != null) {
                result.add(entity);
            }
        }
        return result;
    }

    public String toJsonString(List<Dest> entities) throws Exception {
        JSONArray list = new JSONArray();
        for (Dest entity : entities) {
            list.put(convertJson(entity));
        }
        return list.toString();
    }
}
