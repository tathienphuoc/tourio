package SGU.Tourio.DTO;

import SGU.Tourio.lib.enums.SexType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerDTO {
    private String name;
    private String cccd;
    private String address;
    private String phone;
    private SexType sex;
    private String nationality;

}
