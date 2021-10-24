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

import java.util.Optional;


@Component
public class GroupEmpMapper extends BaseMapper<GroupEmployeeRel, Group> {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    JobRepository jobRepository;

    @Override
    protected GroupEmployeeRel convert(JSONObject object, Group group) {
        Optional<Job> job = jobRepository.findById(Long.parseLong(object.getString("job")));
        Optional<Employee> employee = employeeRepository.findById(Long.parseLong(object.getString("employee")));
        GroupEmployeeRel emp = null;
        if (employee.isPresent() && job.isPresent()) {
            emp = new GroupEmployeeRel(group, employee.get(), job.get());
        }
        return emp;
    }

    @Override
    protected JSONObject convertJson(GroupEmployeeRel entity) {
        JSONObject object = new JSONObject();
        object.put("employee", entity.getEmployee().getId().toString());
        object.put("job", entity.getJob().getId().toString());
        return object;
    }
}
