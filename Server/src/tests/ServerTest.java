package tests;

import classes.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
    @Test
    public void testPortNumberCannotBeBlank() {
        Server server = new Server();
        server.Port = -1;
        Assertions.assertThrows(Exception.class, server::server_start);
    }

    @Test
    public void testCorrectPortNumberConnectsCorrectly() {
        Server server = new Server();
        server.Port = 30000;
        server.server_start();
    }
}
