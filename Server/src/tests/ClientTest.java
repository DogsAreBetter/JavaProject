package tests;

import classes.Server;
import controller.ClientSocket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    public void ClientShouldNotConnectToIncorrectPort() {
        ClientSocket client = new ClientSocket();
        client.setArgs("test", "localhost", -1);
        Assertions.assertThrows(Exception.class, client::network);
    }

    @Test
    public void ClientShouldConnectToActiveServer() {
        Server server = new Server();
        server.Port = 30000;
        server.server_start();

        ClientSocket client = new ClientSocket();
        client.setArgs("test", "localhost", 30000);
        client.network();
        client.connection();
    }
}
