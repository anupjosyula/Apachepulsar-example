package com.example.JWTtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.pulsar.shade.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.Date;

public class Generate_ValidateJWT_Sample {

    private static String SECRET_KEY = "DjG/Cn1X06T6wbCjKW81rkjtKbLJu6W7n1NZBNrhcqo=";

    public static String createJWT(String subject) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        LocalTime localTime = LocalTime.now();
        System.out.println("Second is " + localTime.getSecond());
        //setting expirty to one year.
        long expMillis = nowMillis + 31536000;

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .claim("groups", new String[] { "user", "admin" })
                .setIssuedAt(now)
                .setSubject(subject).setExpiration(new Date(expMillis))
                .signWith(signatureAlgorithm, signingKey);



        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public static  void main(String[] args) throws JsonProcessingException, NoSuchAlgorithmException {
       String token = Generate_ValidateJWT_Sample.createJWT("admin");
        ObjectMapper mapper = new ObjectMapper();
        Generate_ValidateJWT_Sample.decodeJWT(token);
        System.out.println("JWT token is " + token);
        System.out.println(Generate_ValidateJWT_Sample.decodeJWT(token));
        //System.out.println(new ObjectMapper(mapper.writeValue(JWT_generation.decodeJWT(token));));

      /*  KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();
        System.out.println("publicKey is " + publicKey);
        System.out.println("privateKey is " + privateKey);*/



    }

}
