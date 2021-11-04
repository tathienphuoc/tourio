package SGU.Tourio.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupEmployeeRel implements Serializable {
    @EmbeddedId
    private GroupEmployeeRelID id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    private Job job;

    public GroupEmployeeRel(Group group, Employee employee, Job job) {
        this.id = new GroupEmployeeRelID(employee.getId(), group.getId());
        this.group = group;
        this.employee = employee;
        this.job = job;
    }
}