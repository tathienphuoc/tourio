package SGU.Tourio.lib.m2m;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;

public class MappedObject {
    JSONObject obj;

    public MappedObject(JSONObject jsonObject) {
        this.obj = jsonObject;
    }

    public <E> E getEntity(JpaRepository<E, Long> repo, String field) {
        if (this.obj.has(field)) {
            Optional<E> entity = repo.findById(Long.parseLong(this.obj.getString(field)));
            return entity.orElse(null);
        } else {
            return null;
        }
    }

    public Long getLong(String field) {
        if (this.obj.has(field)) {
            return Long.parseLong(this.obj.getString(field));
        }
        return null;
    }

    public Float getFloat(String field) {
        if (this.obj.has(field)) {
            return Float.parseFloat(this.obj.getString(field));
        }
        return null;
    }

    public Date getDate(String field) throws JSONException, ParseException {
        if (this.obj.has(field)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            return formatter.parse(this.obj.getString(field));
        }
        return null;
    }

    public String getString(String field) {
        if (this.obj.has(field)) {
            return this.obj.getString(field);
        }
        return null;
    }


}
