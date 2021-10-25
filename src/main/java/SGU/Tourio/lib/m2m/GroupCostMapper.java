package SGU.Tourio.lib.m2m;

import SGU.Tourio.Models.*;
import SGU.Tourio.Repositories.CostTypeRepository;
import SGU.Tourio.Repositories.EmployeeRepository;
import SGU.Tourio.Repositories.JobRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GroupCostMapper extends BaseMapper<GroupCostRel, Group> {
    @Autowired
    CostTypeRepository costTypeRepository;


    @Override
    protected GroupCostRel convert(JSONObject object, Group group) {
        MappedObject obj = new MappedObject(object);
        CostType costType = obj.getEntity(costTypeRepository, "costType");
        Long amount = obj.getLong("amount");

        if (costType == null || amount == null)
            return null;

        return new GroupCostRel(group, costType, amount);
    }

    @Override
    protected JSONObject convertJson(GroupCostRel entity) {
        JSONObject object = new JSONObject();
        object.put("costType", entity.getCostType().getId().toString());
        object.put("amount", entity.getAmount().toString());
        return object;
    }
}
