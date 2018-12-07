package testSystem.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import testSystem.Enum.Role;
import testSystem.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class Securityhandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        System.out.println(user.toString());
        List<Role> authorities = (List<Role>) user.getAuthorities();
        if (authorities.get(0).name().equals("ROLE_USER"))
            httpServletResponse.sendRedirect("/testPage?id=" + user.getId());
        else
            httpServletResponse.sendRedirect("/admin");
    }
}
