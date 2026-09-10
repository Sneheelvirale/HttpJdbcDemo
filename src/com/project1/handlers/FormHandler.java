package com.project1.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FormHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(),StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(reader);
			StringBuilder formData = new StringBuilder();
			String line ;
			while((line = bufferedReader.readLine())!=null) {
				formData.append(line);
			}
			Map<String,String> params = parseFormData(formData.toString());
			String name = params.get("name");
			String email = params.get("email");
			String password = params.get("password");
			
			boolean isSaved = DataHandler.saveUser(name, email, password);
			
			String response;
			if(isSaved) {
				response = "<h1>Success !!</h1><p>Account created for"+name+"</p>";
			}else {
				response = "<h1>Failed</h1><p>Could not able to store infor to database</p>";
			}
			exchange.getResponseHeaders().set("Content-Type","text/html; charset=UTF-8"); 
			exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes(StandardCharsets.UTF_8));
			os.close();
		}else {
			exchange.sendResponseHeaders(405, -1);
			
		}
	}
	
	private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException{
		Map<String, String> map = new HashMap<>();
		String[] pairs = formData.split("&");
		for(String pair : pairs) {
			String[] keyValue = pair.split("=");
			if(keyValue.length>1) {
				String key = URLDecoder.decode(keyValue[0],StandardCharsets.UTF_8.name());
				String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
				map.put(key, value);
			}
		}
		return map;
	}

	

}
