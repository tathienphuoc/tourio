package SGU.Tourio.lib.m2m;

import org.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

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

    public String getString(String field) {
        if (this.obj.has(field)) {
            return this.obj.getString(field);
        }
        return null;
    }


}
