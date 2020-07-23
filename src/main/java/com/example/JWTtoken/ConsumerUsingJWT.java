package com.example.JWTtoken;

import org.apache.pulsar.client.api.*;

public class ConsumerUsingJWT {
    public static void main(String[] args) throws PulsarClientException {

        Consumer<String> consumer = null;
        PulsarClient client = null;

        try {
            client = PulsarClient.builder().authentication(AuthenticationFactory.token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2xlMiIsImlhdCI6MTU5NTM1NDIyOCwiZXhwIjoxNTk1Mzg1NzY0fQ.sB59zTAhSxve2d54J2XtZMMt1sj7sYZWBqI78H6EP3Y"))
                    .serviceUrl("pulsar://localhost:6650").build();

            consumer = client.newConsumer(Schema.STRING)
                    //.topic("persistent://tn1/ns1/tp1")
                    .topic("persistent://tn4/ns1/tp1")
                    .subscriptionName("my-subscription")
                    .subscriptionType(SubscriptionType.Shared)
                    .subscribe();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }

        while (true) {
            Message<String> msg = consumer.receive();
            try {
                System.out.println("Processing message: " + msg.getValue());
                consumer.acknowledge(msg);

            } catch (PulsarClientException e) {
                e.printStackTrace();
                System.err.println("Failed to process message: " + msg.getValue());
                consumer.negativeAcknowledge(msg);
            }
            finally {
                client.close();

            }
        }
    }
}
