package SGU.Tourio.Models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tour{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    
    @ManyToOne
    private TourType tourType;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TourPrice> tourPrices;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TourLocationRel> tourLocationRels;
}