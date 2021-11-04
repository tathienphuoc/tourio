package SGU.Tourio.DTO;

import SGU.Tourio.Models.TourType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportGroupDTO {
    private Long id;

    private String tourName;

    private String name;

    private Long tourPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEnd;

    private Integer customerCount;

    private Integer employeeCount;

    private Long totalCost;

    private Long totalSale;

    private int revenue;

    private Date createdAt;
}
