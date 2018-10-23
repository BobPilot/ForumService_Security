package telran.forum;

import java.time.LocalDateTime;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.forum.dao.UserAccountRepository;
import telran.forum.domain.UserAccount;

@SpringBootApplication
public class ForumServiceApplication implements CommandLineRunner {
	@Autowired
	UserAccountRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(ForumServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!userRepository.existsById("admin")) {
			String hashPassword = BCrypt.hashpw("admin", BCrypt.gensalt());
			UserAccount userAccount = UserAccount.builder()
					.id("admin")
					.password(hashPassword)
					.firstName("Super")
					.lastName("Admin")
					.role("Admin")
					.role("User")
					.role("Moderator")
					.expDate(LocalDateTime.now().plusYears(25))
					.build();
			userRepository.save(userAccount);
		}
	}
}
