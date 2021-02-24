package br.com.systemsgs.springbooot.jwt;

import br.com.systemsgs.springbooot.service.ImplementacaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService;

    public JWTAuthenticationFilter(JWTService jwtService, ImplementacaoUserDetailsService implementacaoUserDetailsService) {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if(authorization != null && authorization.startsWith("Bearer")){

            String token = authorization.split(" ")[1];
            boolean isTokenValido = jwtService.tokenValido(token);

            if(isTokenValido){
               String loginUsuario = jwtService.obterLoginUsuario(token);
               UserDetails usuario = implementacaoUserDetailsService.loadUserByUsername(loginUsuario);
               UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
               user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(user);
            }

        }

        filterChain.doFilter(request, response);

    }
}
