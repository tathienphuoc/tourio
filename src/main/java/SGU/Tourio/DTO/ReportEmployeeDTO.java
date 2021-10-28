package SGU.Tourio.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportEmployeeDTO {
    private Long id;
    private String name;
    private int groupCount;
    private String jobs;
}
