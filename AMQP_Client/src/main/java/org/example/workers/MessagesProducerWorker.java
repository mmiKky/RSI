package org.example.workers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessagesProducerWorker {
    private static final String HOST = "localhost";
    private static final String QUEUE_NAME = "test_queue_worker";
    private static final int TRANSMISSION_COUNT = 5;
    private static final int MESSAGES_PER_TRANSMISSION = 10;

    public static void main(String[] args) {
        establishConnection();
    }

    public static void establishConnection(){
        ConnectionFactory connFactory = new ConnectionFactory();
        connFactory.setHost(HOST);
        String message = "test_msg_worker";

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
        while(transmissionCounter++ < TRANSMISSION_COUNT) {

            for(int i = 0; i < MESSAGES_PER_TRANSMISSION; ++i) {
                String msgToSend = "[" + transmissionCounter + i + "]" + message;
                channel.basicPublish("", QUEUE_NAME, null, msgToSend.getBytes());
                System.out.println("Sent: " + msgToSend);
            }
        }
    }
}
