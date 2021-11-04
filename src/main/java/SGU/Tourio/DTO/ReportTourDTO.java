package SGU.Tourio.DTO;

import SGU.Tourio.Models.TourLocationRel;
import SGU.Tourio.Models.TourType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTourDTO {
    private Long id;
    private String name;
    private TourType tourType;
    private Long totalSale;
    private Long totalCost;
    private Integer revenue;
    private int groupCount;
}
