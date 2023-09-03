package uz.bek.telecommunication_company.payload;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.bek.telecommunication_company.entity.SimCard;
import uz.bek.telecommunication_company.entity.enums.ActionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDto {

    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    private SimCard simCard;
    private Float amount;
}
