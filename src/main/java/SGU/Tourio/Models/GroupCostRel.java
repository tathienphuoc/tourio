package SGU.Tourio.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupCostRel implements Serializable {
    @EmbeddedId
    private GroupCostRelID id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("costTypeId")
    @JoinColumn(name = "cost_type_id")
    private CostType costType;


    private Long amount;

    public GroupCostRel(Group group, CostType costType, Long amount) {
        this.id = new GroupCostRelID(costType.getId(), group.getId());
        this.group = group;
        this.costType = costType;
        this.amount = amount;
    }
}