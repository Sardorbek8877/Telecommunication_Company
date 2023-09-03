package uz.bek.telecommunication_company.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimCardDto {
    private String name;

    private String code;
}
