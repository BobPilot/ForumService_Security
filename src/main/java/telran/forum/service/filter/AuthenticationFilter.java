package telran.forum.service.filter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.forum.configuration.AccountConfiguration;
import telran.forum.configuration.AccountUserCredential;
import telran.forum.dao.UserAccountRepository;
import telran.forum.domain.UserAccount;

@Service
@Order(2)
public class AuthenticationFilter implements Filter {

	@Autowired
	AccountConfiguration userConfiguration;

	@Autowired
	UserAccountRepository accountRepository;

	@Override
	public void doFilter(ServletRequest reqs, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) reqs;
		HttpServletResponse response = (HttpServletResponse) resp;

        if(!request.getServletPath().contains("register")){

            AccountUserCredential userCredential = userConfiguration.tokenDecode(request.getHeader("Authorization"));
            UserAccount user = accountRepository.findById(userCredential.getLogin()).orElse(null);

            if(user == null){
                response.sendError(401, "User is not found");
                return;
            }

			if (!BCrypt.checkpw(userCredential.getPassword(), user.getPassword())) {
			    response.sendError(403, "Forbidden");
                return;
			}

			if(user.getExpDate().isBefore(LocalDateTime.now())
                && request.getHeader("Account-upd") == null){

                response.sendError(409, "Need to change password");
                return;
            }

		}

		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
