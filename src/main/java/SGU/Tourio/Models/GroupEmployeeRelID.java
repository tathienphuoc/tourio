package SGU.Tourio.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupEmployeeRelID implements Serializable {
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "group_id")
    private Long groupId;

}
