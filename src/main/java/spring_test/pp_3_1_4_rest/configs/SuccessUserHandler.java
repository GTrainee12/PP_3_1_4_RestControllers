package spring_test.pp_3_1_4_rest.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import spring_test.pp_3_1_4_rest.entity.User;
import spring_test.pp_3_1_4_rest.service.UserService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    private final UserService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities())
                .stream()
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toSet());
        if (roles.contains("ADMIN")) {

            response.sendRedirect("/admin/users");

        } else if (roles.contains("USER")) {

            User user = userService.findByEmail(authentication.getName());
            if (user != null) {
                response.sendRedirect("/user/profile?id=" + user.getId());
            } else {
                response.sendRedirect("/login");
            }
        } else {
            response.sendRedirect("/access-denied");
        }

    }
}