package SGU.Tourio.Models;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourLocationRel implements Serializable {
    @EmbeddedId
    private TourLocationRelID id;

    @ManyToOne
    @MapsId("tourId")
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ManyToOne
    @MapsId("locationId")
    @JoinColumn(name = "location_id")
    private Location location;

    private Long sequence;

    public TourLocationRel(Tour tour, Location location, Long sequence) {
        this.id = new TourLocationRelID(tour.getId(), location.getId());
        this.tour = tour;
        this.location = location;
        this.sequence = sequence;
    }
}