package telran.forum.service.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import telran.forum.configuration.AccountConfiguration;
import telran.forum.configuration.AccountUserCredential;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Order(1)
public class HeaderFilter implements Filter {


    @Autowired
    AccountConfiguration userConfiguration;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            response.sendError(401, "Missing authorization header");
            return;
        }

        AccountUserCredential userCredential = userConfiguration.tokenDecode(authHeader);

        if (userCredential == null) {
            response.sendError(400, "Bad header format");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
}

