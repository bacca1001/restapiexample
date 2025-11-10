package com.example.restapi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private String method;
    private String path;
    private Map<String, String> parameters;
    private String body;
    private Map<String, String> headers;

    public RequestParser(String requestLine, BufferedReader reader) throws IOException {
        parseRequestLine(requestLine);
        parseHeaders(reader);
        parseBody(reader);
    }

    private void parseRequestLine(String requestLine) {
        String[] parts = requestLine.split(" ");
        this.method = parts[0];
        this.path = parts[1];
        this.parameters = new HashMap<>();

        // Parse query parameters
        if (path.contains("?")) {
            String[] pathParts = path.split("\\?");
            this.path = pathParts[0];
            String query = pathParts[1];
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    private void parseHeaders(BufferedReader reader) throws IOException {
        this.headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] parts = line.split(": ", 2);
            if (parts.length == 2) {
                headers.put(parts[0], parts[1]);
            }
        }
    }

    private void parseBody(BufferedReader reader) throws IOException {
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] bodyChars = new char[contentLength];
            reader.read(bodyChars);
            this.body = new String(bodyChars);
        } else {
            this.body = "";
        }
    }

    // Getters
    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }
}
