package ru.itis.flavorful_book.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.itis.flavorful_book.util.validation.PasswordsMatch;

@Data
@PasswordsMatch
public class SignupForm {

    @NotBlank
    @Size(min = 3, max = 40)
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank
    private String passwordRepeat;
}
