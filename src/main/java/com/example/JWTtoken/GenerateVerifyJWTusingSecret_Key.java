package com.example.JWTtoken;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.common.policies.data.AuthAction;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class GenerateVerifyJWTusingSecret_Key {


    AuthAction authAction ;
    // Secret key is generated in pulsar cli and is copied here, It can be generated here as well
    String SECRET_KEY = "r51KLy5XQdyUc7zhfOcc2kTxFlKUW1KGv5vprHnMg7Q=";

    public  String generate_JWT(String subject) {
        // fetching the current date and time
        LocalDateTime current = LocalDateTime.now();
        System.out.println("current Date time is  " + current);

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        LocalTime localTime = LocalTime.now();
        System.out.println("Second is " + localTime.getSecond());
        //setting expiry
        long expMillis = nowMillis + 31536000;

        // generate the secret key
        //SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


        //encode the secretkey
       // String secretString = Encoders.BASE64.encode(key.getEncoded());


        System.out.println("Adding Expiry time of 8 hours  " + (current.getSecond() + 31556952));


        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

// generate the JWT based on subject, Issued time, Expiry time, and Algorithm
        String jws = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(expMillis))
                .signWith(signatureAlgorithm, signingKey).compact();
        return jws;

    }

    public void verify_JWT_usingsecretkey(String jws){

        Jws signedJWT = null;
        try {
            signedJWT = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).build().parseClaimsJws(jws);
            signedJWT.getSignature();
        }
        catch(JwtException ex)
        {
            System.out.println("JWT was not signed");
        }
        System.out.println("signed and verified JWT using secret key");
        System.out.println(signedJWT);
        System.out.println(signedJWT.getSignature());

    }

    public static void main(String[] args) throws PulsarClientException, PulsarAdminException {
            GenerateVerifyJWTusingSecret_Key jwtGeneratedForPulsar = new GenerateVerifyJWTusingSecret_Key();
            String generatedtoken =  jwtGeneratedForPulsar.generate_JWT("role2");
            System.out.println("the token generated for role2 is " + generatedtoken);
            System.out.println("Passing this generated token to producer_pulsar ");
            //jwtGeneratedForPulsar.producer_pulsar(token);
            jwtGeneratedForPulsar.verify_JWT_usingsecretkey(generatedtoken);

            }
}
