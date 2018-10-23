package telran.forum.configuration;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

@Configuration
@ManagedResource
public class AccountConfiguration {

	@Value("${exp.value}")
	int expPeriod;

	@ManagedAttribute
	public int getExpPeriod() {
		return expPeriod;
	}

	@ManagedAttribute
	public void setExpPeriod(int expPeriod) {
		this.expPeriod = expPeriod;
	}

	public AccountUserCredential tokenDecode(String auth) {
		int pos = auth.indexOf(" ");
		String token = auth.substring(pos + 1);
		String credential = decode(token);
		String[] credentials = credential.split(":");
		return new AccountUserCredential(credentials[0], credentials[1]);
	}

	public String decode(String str){

		return new String(Base64.getDecoder().decode(str));

	}
	
}
