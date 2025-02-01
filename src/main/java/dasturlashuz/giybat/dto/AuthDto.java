package dasturlashuz.giybat.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

   }
