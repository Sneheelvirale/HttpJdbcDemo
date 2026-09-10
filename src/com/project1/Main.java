package com.project1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.project1.handlers.DataListHandler;
import com.project1.handlers.FormHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {

	public static void main(String[] args) throws IOException {
	    int port = 8080;
	    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
	    server.createContext("/submit", new FormHandler());
	    server.createContext("/data", new DataListHandler()); // NEW route
	    server.createContext("/", new StaticFileHandler());
	    server.setExecutor(null);
	    System.out.println("Server started on http://localhost:" + port);
	    server.start();
	}
	
	static class StaticFileHandler implements HttpHandler{

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String path = exchange.getRequestURI().getPath();
	        if (path.equals("/")) {
	            path = "/index.html";
	        }
			File file = new File("webapp"+path);
			if (file.exists() && !file.isDirectory()) {
	            if (path.endsWith(".css")) {
	                // FIX: was "Context-Type" (typo) — must be "Content-Type"
	                // Without the correct header, browsers may refuse to apply the CSS
	                // as a stylesheet even though the file loads successfully.
	                exchange.getResponseHeaders().set("Content-Type", "text/css");
	            } else {
	                exchange.getResponseHeaders().set("Content-Type", "text/html");
	            }
	            exchange.sendResponseHeaders(200, file.length());

	            // FIX: wrap both streams in try-with-resources so they always
	            // close, even if an exception happens mid-write. Your original
	            // code left both streams open indefinitely — under repeated
	            // requests this leaks file handles until the OS runs out.
	            try (OutputStream os = exchange.getResponseBody();
	                 FileInputStream fs = new FileInputStream(file)) {
	                byte[] buffer = new byte[1024];
	                int count;
	                while ((count = fs.read(buffer)) >= 0) {
	                    os.write(buffer, 0, count);
	                }
	            }
	        } else {
	            String response = "404 Not Found";
	            exchange.sendResponseHeaders(404, response.length());
	            // FIX: same leak here — response stream was never closed
	            try (OutputStream os = exchange.getResponseBody()) {
	                os.write(response.getBytes());
	            }
			
		}
		
	}

	}}
