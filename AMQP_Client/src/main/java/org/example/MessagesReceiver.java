package org.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class MessagesReceiver {
    private static final String HOST = "localhost";
    private static final String QUEUE_NAME = "test_queue";

    public static void main(String[] args) {
        establishConnection();
    }

    public static void establishConnection(){
        ConnectionFactory connFactory = new ConnectionFactory();
        connFactory.setHost(HOST);

        try(Connection connection = connFactory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            createConsumer(channel);

            new Scanner(System.in).next(); // don't close receiver too fast; wait for user input to make sure all messages were received

            channel.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    private static void createConsumer(Channel channel) throws IOException {
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received message: " + msg);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }
}
