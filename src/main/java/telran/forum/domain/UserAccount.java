package telran.forum.domain;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = { "id" })
@Document(collection = "forum_users")
public class UserAccount {
	@Id
	String id;
	String password;
	String firstName;
	String lastName;
	LocalDateTime expDate;
	@Singular
	Set<String> roles;

}
