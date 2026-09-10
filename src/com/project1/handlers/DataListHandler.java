package com.project1.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DataListHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Only respond to GET — this endpoint reads data, it shouldn't
        // accept POST/PUT/DELETE. This mirrors the REST convention we
        // discussed: GET = read, POST = write.
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        List<String> users = DataHandler.getAllUsers();
        // Join the individual JSON objects into a JSON array string: [ {...}, {...} ]
        String responseBody = "[" + String.join(",", users) + "]";

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}