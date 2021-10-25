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
public class GroupCostRelID implements Serializable {
    @Column(name = "cost_type_id")
    private Long costTypeId;

    @Column(name = "group_id")
    private Long groupId;

}
