package SGU.Tourio.lib.m2m;

import SGU.Tourio.Models.Employee;
import SGU.Tourio.Models.Group;
import SGU.Tourio.Models.GroupEmployeeRel;
import SGU.Tourio.Models.Job;
import SGU.Tourio.Repositories.EmployeeRepository;
import SGU.Tourio.Repositories.JobRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GroupEmpMapper extends BaseMapper<GroupEmployeeRel, Group> {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    JobRepository jobRepository;

    @Override
    protected GroupEmployeeRel convert(JSONObject object, Group group) {
        MappedObject obj = new MappedObject(object);
        Job job = obj.getEntity(jobRepository, "job");
        Employee employee = obj.getEntity(employeeRepository, "employee");
        if (employee == null || job == null)
            return null;

        return new GroupEmployeeRel(group, employee, job);
    }

    @Override
    protected JSONObject convertJson(GroupEmployeeRel entity) {
        JSONObject object = new JSONObject();
        object.put("employee", entity.getEmployee().getId().toString());
        object.put("job", entity.getJob().getId().toString());
        return object;
    }
}
