package pojos;

import lombok.Data;

@Data
public class AuthDto {
    private final String password;
    private final String username;
}
