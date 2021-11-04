package SGU.Tourio.DTO;

import SGU.Tourio.lib.enums.SexType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupDTO {
    private String name;

    private String description;

    private Long tourId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEnd;

    private List<Long> customerIds;

    private String employeeData;

    private String costData;
}
