package com.spring.security.jwt;



public class JwtProperties {
    public static final String SECRET ="SecretKeyAsYouWishToEncode";
//    public static final long EXPIRATION_TIME= TimeUnit.DAYS.toSeconds(21);
    public static final long EXPIRATION_TIME= 86400000;
    public static final String TOKEN_PREFIX="Bearer ";
    public static final String HEADER_STRING="Authorization";
}
