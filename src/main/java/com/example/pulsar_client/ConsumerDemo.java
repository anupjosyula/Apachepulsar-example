package com.example.pulsar_client;

import org.apache.pulsar.client.api.*;

public class ConsumerDemo {
    public static void main(String[] args) throws PulsarClientException {

        org.apache.pulsar.client.api.Consumer<String> consumer = null;
        PulsarClient client = null;

        try {
            client = PulsarClient.builder()
                    .serviceUrl("pulsar://localhost:6650")
                    .build();

            consumer = client.newConsumer(Schema.STRING)
                    .topic("my-topic")
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
