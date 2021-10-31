package SGU.Tourio.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTourDTO {
    private String name;
    private String description;

    private Long tourTypeId;

    private String locationData;

    private String tourPriceData;
}
