package advisor;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class LocalHttpServer implements AutoCloseable {
    private final HttpServer server;

    LocalHttpServer(HttpHandler handler) {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/", handler);
            server.start();
        } catch (IOException exception) {
            throw new RuntimeException("Cannot start local HTTP Server! " +
                                       "Error message: " + exception.getMessage());
        }
    }

    void stop() {
        this.server.stop(1);
    }

    @Override
    public void close() {
        this.stop();
    }
}
