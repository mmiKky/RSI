package org.example;

import org.apache.xmlrpc.client.TimingOutCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public class Client {
    public static void main(String[] args) throws Throwable {

        // create configuration
        var config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://127.0.0.1:8080/xmlrpc"));
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);

        // create client and set settings
        var client = new XmlRpcClient();
        client.setTransportFactory(new XmlRpcSunHttpTransportFactory(client));
        client.setConfig(config);

        // synchronous call
        System.out.println("Executing synchronous call...");
        var result = client.execute("MessagesService.echo", List.of("Message One [1]"));
        System.out.println(result);

        // create callbacks
        var callback1 = new TimingOutCallback(1000);    // server waits 3s, so it will be timeout
        var callback2 = new TimingOutCallback(5 * 1000);

        // asynchronous calls
        System.out.println("Executing asynchronous call...");
        client.executeAsync("MessagesService.echoWithDelay", List.of("Message Two [2]"), callback1);

        System.out.println("Executing asynchronous call...");
        client.executeAsync("MessagesService.echoWithDelay", List.of("Message Three [3]"), callback2);

        System.out.println("Executing add method");
        var sum = client.execute("MessagesService.add", List.of(11, 5));
        System.out.println("sum: " + sum);

        System.out.println(getAsyncResponse(callback1));
        System.out.println(getAsyncResponse(callback2));
    }

    public static Optional<Object> getAsyncResponse(TimingOutCallback callback) {
        try {
            return Optional.ofNullable(callback.waitForResponse());
        }catch (TimingOutCallback.TimeoutException e){
            System.out.println("No response from server before timeout.");
        }catch (Throwable e) {
            System.out.println("Server returned an error message.");
        }

        return Optional.empty();
    }
}
