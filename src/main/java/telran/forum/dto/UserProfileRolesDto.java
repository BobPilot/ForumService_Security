package telran.forum.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserProfileRolesDto {
        String id;
        String firstName;
        String lastName;
        Set<String> roles;
}
