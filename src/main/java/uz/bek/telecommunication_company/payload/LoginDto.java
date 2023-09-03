package uz.bek.telecommunication_company.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull
    @Email
    private String username;

    @NotNull
    private String password;
}
