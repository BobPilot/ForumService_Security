package telran.forum.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserProfileDto {
	String id;
	String firstName;
	String lastName;

}
