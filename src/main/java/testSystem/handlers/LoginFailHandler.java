package testSystem.handlers;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//        httpServletResponse.getWriter().write("false");
//        httpServletResponse.getWriter().flush();
//        httpServletResponse.getWriter().close();
        httpServletResponse.sendRedirect("/autorisation?error");
        // httpServletResponse.sendError(1,"bad credits");
        //httpServletResponse.sendRedirect(httpServletRequest.getRequestURI());
    }
}
