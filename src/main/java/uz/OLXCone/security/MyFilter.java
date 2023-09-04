package uz.OLXCone.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.OLXCone.utils.AppConstants;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyFilter extends OncePerRequestFilter {
    JwtProvider jwtProvider;
    PasswordEncoder passwordEncoder;
    UserDetailsService userDetailsService;

    @Autowired
    public MyFilter(@Lazy JwtProvider jwtProvider,
                    @Lazy PasswordEncoder passwordEncoder,
                    @Lazy UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("authorization");
        label:
        {
            if (Objects.isNull(authorization))
                break label;
            if (authorization.startsWith(AppConstants.BEARER)) {
                String token = authorization.substring(7);
                String email;
                try {
                    email = jwtProvider.getSubjectFromToken(token);
                } catch (RuntimeException e) {
                    log.warn("Exception on getSubjectFromToken",e);
                    break label;
                }
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (checkCredentials(userDetails))
                    break label;
                setUserToContext(userDetails);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkCredentials(UserDetails userDetails) {
        return !userDetails.isCredentialsNonExpired() ||
                !userDetails.isAccountNonExpired() ||
                !userDetails.isAccountNonLocked() ||
                !userDetails.isEnabled();
    }

    public void setUserToContext(UserDetails user) {
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
