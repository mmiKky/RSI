package org.example.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessagesProducerRouting {
    private static final String HOST = "localhost";
    private static final String EXCHANGE_NAME = "test_exchange_routing";
    private static final int TRANSMISSION_COUNT = 5;
    private static final int MESSAGES_PER_TRANSMISSION = 10;

    public static void main(String[] args) {
        establishConnection();
    }

    public static void establishConnection(){
        ConnectionFactory connFactory = new ConnectionFactory();
        connFactory.setHost(HOST);
        String message = "test_msg";

        try(Connection connection = connFactory.newConnection()) {
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
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
                String infoMessage = "[" + transmissionCounter + i + "]" + message;
                String errorMessage = "on error";

                channel.basicPublish(EXCHANGE_NAME, "info", null, infoMessage.getBytes());
                channel.basicPublish(EXCHANGE_NAME, "error", null, errorMessage.getBytes());

                System.out.println("Sent: " + infoMessage);
            }
        }
    }
}
