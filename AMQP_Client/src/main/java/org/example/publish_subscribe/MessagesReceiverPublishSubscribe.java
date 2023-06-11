package org.example.publish_subscribe;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class MessagesReceiverPublishSubscribe {
    private static final String HOST = "localhost";
    private static final String EXCHANGE_NAME = "test_exchange";

    public static void main(String[] args) {
        establishConnection();
    }

    public static void establishConnection(){
        ConnectionFactory connFactory = new ConnectionFactory();
        connFactory.setHost(HOST);

        try(Connection connection = connFactory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.basicQos(1); // accept only one unack-ed message at a time
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            createConsumer(channel);

            new Scanner(System.in).next(); // don't close receiver too fast; wait for user input to make sure all messages were received

            channel.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    private static void createConsumer(Channel channel) throws IOException {
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received message: " + msg);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        channel.basicConsume(queueName, true, defaultConsumer);
    }
}
