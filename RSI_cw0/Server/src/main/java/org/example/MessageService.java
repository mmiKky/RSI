package org.example;

public class MessageService {
    private static final int DELAY_SEC = 3;

    public String echo(String message){
        System.out.println("Message received: " + message);
        System.out.println("Response: sending echo message...");
        return "Echo: " + message;
    }

    public String echoWithDelay(String message) throws InterruptedException {
        System.out.println("Message received: " + message);
        System.out.println("Waiting for %d seconds before sending response...");
        // delay to test time-out
        Thread.sleep(DELAY_SEC * 1000);
        System.out.println("Response: sending echo message...");
        return "Echo after delay: " + message;
    }

    public int add(int num1, int num2) {
        return num1 + num2;
    }
}
