package com.example.JWTtoken;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.*;

public class ProducerUsingJWT {
    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = null;
        Producer<String> producer = null;
        PulsarAdmin admin = null;
        try {
             admin = PulsarAdmin.builder()
                     .authentication(AuthenticationFactory.token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2xlMiIsImlhdCI6MTU5NTM1NDIyOCwiZXhwIjoxNTk1Mzg1NzY0fQ.sB59zTAhSxve2d54J2XtZMMt1sj7sYZWBqI78H6EP3Y"))
                     .serviceHttpUrl("http://localhost:8080")
                    .build();

            client = PulsarClient.builder()
                    .authentication(AuthenticationFactory.token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2xlMiIsImlhdCI6MTU5NTM1NDIyOCwiZXhwIjoxNTk1Mzg1NzY0fQ.sB59zTAhSxve2d54J2XtZMMt1sj7sYZWBqI78H6EP3Y"))
                    .serviceUrl("pulsar://localhost:6650")
                    .build();

            /*if (admin.getClientConfigData().getAuthentication().getAuthData().hasDataForHttp()) {
                System.out.println(admin.getClientConfigData().getAuthParams());
                System.out.println(admin.getClientConfigData().getAuthPluginClassName());
                System.out.println("The authentication in clientconfig is enabled");
            }*/


            producer = client.newProducer(Schema.STRING)
                    //.topic("persistent://tn1/ns1/tp1")
                    .topic("tn4/ns1/tp1")
                    .create();
            if(admin.topics().getPermissions("tn4/ns1/tp1").containsKey("role2")) {
                if (admin.getClientConfigData().getAuthentication().getAuthData().hasDataForHttp()) {

                    producer.send("Hello!");
                }
            }

            System.out.println("The message has been sent to the topic");

        } catch (PulsarClientException | PulsarAdminException e) {
            e.printStackTrace();
        }
            finally {
            client.close();
        }




    }
}
