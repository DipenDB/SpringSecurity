package com.spring.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.spring.security.db.User;
import com.spring.security.db.UserPrincipal;
import com.spring.security.db.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        //Get Header with token i.e Authorization = Bearer sajbsahas456asbncayu98skcaskckn
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        //If header is null or don't starts with Arun_ then return nothing
        if(header==null ||!header.startsWith(JwtProperties.TOKEN_PREFIX) ){
            chain.doFilter(request,response);
            return;
        }

        //If header is present, try to grab User Principal from database and perform Authorization
        //Self made method to extract user information from database
        Authentication authentication = getUsernameAndPasswordAuthentication(request);  // Request is token
        SecurityContextHolder.getContext().setAuthentication(authentication);  //Receive token2 with authorities list

        //Continue filter execution
        chain.doFilter(request,response);


    }

    private Authentication getUsernameAndPasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING);

        if(token!=null){
            String username = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET.getBytes()))
                                  .build()
                                    .verify(token.replace(JwtProperties.TOKEN_PREFIX,""))
                                    .getSubject();

            if (username!=null) {
                User user = userRepository.findByUsername(username);
                UserPrincipal userPrincipal = new UserPrincipal(user);
                UsernamePasswordAuthenticationToken token2 = new UsernamePasswordAuthenticationToken(username, null, userPrincipal.getAuthorities());
                return token2;
            }
            return null;  // else return null
        }
        return null;     // else return null
    }
}
