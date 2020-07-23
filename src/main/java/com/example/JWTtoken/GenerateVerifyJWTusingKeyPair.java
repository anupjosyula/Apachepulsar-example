package com.example.JWTtoken;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;

import java.security.KeyPair;

public class GenerateVerifyJWTusingKeyPair {

    KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);


    public void generate_JWt_KeyPair() {



        String jws = Jwts.builder()

                .setSubject("role2")

                .signWith(keyPair.getPrivate())

                .compact();

        System.out.println("The token genereated is " + jws);

        verify_JWT_usingKeypair(jws);

    }

    public void verify_JWT_usingKeypair(String jws){

        Jws signedJWT = null;
        try {
            signedJWT = Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(jws);
        }
        catch(JwtException ex)
        {
            System.out.println("JWT was not signed");
        }
        System.out.println("signed and verified JWT");
        System.out.println(keyPair.getPublic());
        System.out.println(signedJWT);

    }

    public static void main(String[] args) throws PulsarClientException, PulsarAdminException {

        GenerateVerifyJWTusingKeyPair generateJWTusingKeyPair = new GenerateVerifyJWTusingKeyPair();
        generateJWTusingKeyPair.generate_JWt_KeyPair();


    }




}
