package SGU.Tourio.Models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourLocationRelID implements Serializable {
    @Column(name = "tour_id")
    private Long tourId;

    @Column(name = "location_id")
    private Long LocationId;
}
