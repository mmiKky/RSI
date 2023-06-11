package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessagesProducer {
    private static final String HOST = "localhost";
    private static final String QUEUE_NAME = "test_queue";
    private static final int TRANSMISSION_TIME_IN_SECONDS = 5;
    private static final int MESSAGES_PER_SECOND_NR = 10;

    public static void main(String[] args) {
        establishConnection();
    }

    public static void establishConnection(){
        ConnectionFactory connFactory = new ConnectionFactory();
        connFactory.setHost(HOST);
        String message = "test_msg";

        try(Connection connection = connFactory.newConnection()) {
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            sendMessage(message, channel);

            channel.close();
        } catch (IOException | TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendMessage(String message, Channel channel) throws IOException, InterruptedException {
        int transmissionCounter = 0;
        while(transmissionCounter++ < TRANSMISSION_TIME_IN_SECONDS) {

            for(int i = 0; i < MESSAGES_PER_SECOND_NR; ++i) {
                String msgToSend = "[" + transmissionCounter + i + "]" + message;
                channel.basicPublish("", QUEUE_NAME, null, msgToSend.getBytes());
                System.out.println("Sent: " + msgToSend);
            }
            Thread.sleep(1000);
        }
    }
}
