package SGU.Tourio.DTO;

import SGU.Tourio.lib.enums.SexType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerDTO {
    private String name;
    private String idNumber;
    private String address;
    private String phone;
    private SexType sex;
}
