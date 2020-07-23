package com.example.pulsar_client;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.*;

public class ProducerDemo {
    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = null;
        Producer<String> producer = null;
        try {
            client = PulsarClient.builder()
                    .serviceUrl("pulsar://localhost:6650")
                    .build();

            producer = client.newProducer(Schema.STRING)
                    .topic("my-topic")
                    .create();

                producer.send("Hello from Producer!" );


            System.out.println("The message has been sent to the topic");

        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
            finally {
            client.close();
        }




    }
}
