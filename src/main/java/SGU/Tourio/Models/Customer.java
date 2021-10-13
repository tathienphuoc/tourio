package SGU.Tourio.Models;

import SGU.Tourio.lib.enums.SexType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String idNumber;
    private String address;
    private String phone;

    @Enumerated(EnumType.ORDINAL)
    private SexType sex;
}