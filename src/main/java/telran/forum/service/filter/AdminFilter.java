package telran.forum.service.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import telran.forum.configuration.AccountConfiguration;
import telran.forum.configuration.AccountUserCredential;
import telran.forum.dao.UserAccountRepository;
import telran.forum.domain.UserAccount;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Service
@Order(4)
public class AdminFilter implements Filter {

    @Autowired
    AccountConfiguration accountConfiguration;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(request.getServletPath().contains("/change_role")){

            AccountUserCredential userCredential = accountConfiguration.tokenDecode(request.getHeader("Authorization"));
            UserAccount user = userAccountRepository.findById(userCredential.getLogin()).orElse(null);

            if(!user.getRoles().contains("Admin")){
                response.sendError(403, "Admin only");
                return;
            }
        }

        filterChain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
    public void destroy() {

    }
}
