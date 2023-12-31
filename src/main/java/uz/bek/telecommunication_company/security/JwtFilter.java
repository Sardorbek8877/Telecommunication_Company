package uz.bek.telecommunication_company.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.bek.telecommunication_company.service.AuthService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthService authService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            String header = httpServletRequest.getHeader("Client");
            boolean client = header!=null &&  header.equals("Client");
            String userName = jwtProvider.getUsernameFromToken(authorization.substring(7));
            if (userName != null) {
                UserDetails userDetails = client?authService.loadClientByUsernameFromSimCard(userName):
                        authService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } else if (authorization != null && authorization.startsWith("Basic")) {
            String basic = authorization.substring(6);
            byte[] bytes = Base64
                    .getDecoder()
                    .decode(basic);
            String variable = new String(bytes, StandardCharsets.UTF_8);
            String[] split = variable.split(":");
            String userName = split[0];
            String pinCode = split[1];
            UserDetails userDetails = authService.loadClientByUsernameFromSimCard(userName);
            if (passwordEncoder.matches(pinCode,userDetails.getPassword())){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
