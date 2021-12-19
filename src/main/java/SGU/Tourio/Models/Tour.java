package SGU.Tourio.Models;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private TourType tourType;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TourPrice> tourPrices;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TourLocationRel> tourLocationRels;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Group> groups;

    public Long getPriceByDate(Date date) throws Exception {
        Optional<TourPrice> currentPrice = this.getTourPrices().stream().filter(p -> (date.equals(p.getDateEnd()) || date.before(p.getDateEnd())) && (date.after(p.getDateStart()) || date.equals(p.getDateStart()))).findFirst();
        if (!currentPrice.isPresent()) {
            throw new Exception("No price has been defined on Tour for this period");
        }
        return currentPrice.get().getAmount();
    }
}