package org.example.publish_subscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessagesProducerPublishSubscribe {
    private static final String HOST = "localhost";
    private static final String EXCHANGE_NAME = "test_exchange";
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

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
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
                channel.basicPublish(EXCHANGE_NAME, "", null, msgToSend.getBytes());
                System.out.println("Sent: " + msgToSend);
            }
        }
    }
}
