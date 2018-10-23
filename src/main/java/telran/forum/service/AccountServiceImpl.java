package telran.forum.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.forum.configuration.AccountConfiguration;
import telran.forum.configuration.AccountUserCredential;
import telran.forum.dao.UserAccountRepository;
import telran.forum.domain.UserAccount;
import telran.forum.dto.UserProfileDto;
import telran.forum.dto.UserProfileRolesDto;
import telran.forum.dto.UserRegisterDto;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	UserAccountRepository userRepository;

	@Autowired
	AccountConfiguration accountConfiguration;

	@Override
	public UserProfileDto addUser(UserRegisterDto userRegDto, String auth) {
		AccountUserCredential credentials = accountConfiguration.tokenDecode(auth);
		if (userRepository.existsById(credentials.getLogin())) {
			throw new UserExistException();
		}
		String hashPassword = BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt());
		UserAccount userAccount = UserAccount.builder()
				.id(credentials.getLogin())
				.password(hashPassword)
				.firstName(userRegDto.getFirstName())
				.lastName(userRegDto.getLastName())
				.role("User")
				.expDate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()))
				.build();
		userRepository.save(userAccount);
		return new UserProfileDto(credentials.getLogin(),
				userRegDto.getFirstName(), userRegDto.getLastName());
	}

	@Override
	public UserProfileDto editUser(UserRegisterDto userRegDto, String auth) {
		AccountUserCredential credentials = accountConfiguration.tokenDecode(auth);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		userAccount.setFirstName(userRegDto.getFirstName());
		userAccount.setLastName(userRegDto.getLastName());
		userRepository.save(userAccount);
		return new UserProfileDto(credentials.getLogin(),
				userRegDto.getFirstName(), userRegDto.getLastName());
	}

	@Override
	public UserProfileDto removeUser(String id, String auth) {
		AccountUserCredential credentials = accountConfiguration.tokenDecode(auth);

		if(!isOwnerAdminModerator(id, credentials)) {
			throw new ForbiddenException();
		}
		UserAccount userAccount = userRepository.findById(id).orElse(null);

		userRepository.delete(userAccount);
		return new UserProfileDto(userAccount.getId(), userAccount.getFirstName(), userAccount.getLastName());
	}

	@Override
	public UserProfileRolesDto setModerator(String id) {

		UserAccount user = userRepository.findById(id).orElse(null);
		UserProfileRolesDto userProfile = null;

		if(user != null){
			Set<String> roles = user.getRoles();
			 if(roles.contains("Moderator")){
				 roles.remove("Moderator");
			 } else {
				roles.add("Moderator");
			 }
			 userRepository.save(user);
			 userProfile = UserProfileRolesDto.builder()
					 .firstName(user.getFirstName())
					 .lastName(user.getLastName())
					 .id(user.getId())
					 .roles(user.getRoles())
					 .build();
		}

		return userProfile;
	}

	@Override
	public UserProfileDto changePassword(String id, String auth, String upd) {

		UserAccount user = userRepository.findById(accountConfiguration.tokenDecode(auth).getLogin()).orElse(null);

		if(!user.getId().equalsIgnoreCase(id)){
			throw new ForbiddenException();
		}

		String pass = BCrypt.hashpw(accountConfiguration.decode(upd), BCrypt.gensalt());
		user.setPassword(pass);
		user.setExpDate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()));
		userRepository.save(user);

		return UserProfileDto.builder()
				.id(user.getId())
				.lastName(user.getLastName())
				.firstName(user.getFirstName())
				.build();
	}

	private boolean isOwnerAdminModerator(String id, AccountUserCredential credentials){

		UserAccount user = userRepository.findById(credentials.getLogin()).get();

		return user.getId().equalsIgnoreCase(id)
				|| user.getRoles().stream()
				.anyMatch(s -> "Admin".equals(s) || "Moderator".equals(s));
	}

}
