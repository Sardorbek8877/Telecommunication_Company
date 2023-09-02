package uz.bek.telecommunication_company.payload;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.bek.telecommunication_company.entity.enums.ClientType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffDto {

    @NotNull
    @Column(unique = true)
    private String name;
    private double price;
    private double switchPrice;
    private int expireDate;
    private int mb;
    private int sms;
    private int min;
    private int mbCost;
    private int smsCost;
    private int minCost;
    private Integer clientTypeId; // 1-USER, 2-COMPANY
}
