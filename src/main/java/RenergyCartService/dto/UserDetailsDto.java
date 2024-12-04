package RenergyCartService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDetailsDto {
    private Long userId;
    private String username;
    private List<String> roles;
    private String email;           // Optional additional field
    private String firstName;       // Optional additional field
    private String lastName;        // Optional additional field
}
