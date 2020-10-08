package com.spring.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.spring.security.db.UserPrincipal;
import com.spring.security.db.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public JwtAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager=authenticationManager;
    }

    //Triger when we issue POST request to /Login
    //We also pass {"username"="admin","password"="admin"} in request body
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordModel credential=null;
        //Catch username and password from Login Form
        try {
            credential = new ObjectMapper().readValue(request.getInputStream(),UsernamePasswordModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Create token to authenticate if user exist or not
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                credential.getUsername(),
                credential.getPassword(),
                new ArrayList<>()
        );

        //Authenticate user
        Authentication authentication = authenticationManager.authenticate(token);
        //If success then it pass to next method else not
        return authentication;
    }


    // If user exist then this method work
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // Grab All Details of users
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        //Create Token (Token= Subject + Expirate Date + Secret_Key)
        String token = JWT.create()
                        .withSubject(principal.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                        .sign(Algorithm.HMAC256(JwtProperties.SECRET.getBytes()));

        //Authorization = Bearer sajbsahas456asbncayu98skcaskckn
        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX+token);

    }
}
