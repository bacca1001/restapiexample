package com.example.restapi;

import com.example.restapi.controller.UserController;
import com.example.restapi.util.RequestParser;
import com.example.restapi.util.JSONUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT = 8080;
    private static final UserController userController = new UserController();
    private static final Map<String, Map<String, HttpHandler>> routes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // Initialize routes
        initializeRoutes();

        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newFixedThreadPool(10));

        // Add default handler
        server.createContext("/", new RequestHandler());

        // Start server
        server.start();
        System.out.println("Server started on port " + PORT);
        System.out.println("API endpoints:");
        System.out.println("GET    /api/users          - Get all users");
        System.out.println("GET    /api/users/{id}     - Get user by ID");
        System.out.println("POST   /api/users          - Create new user");
        System.out.println("PUT    /api/users/{id}     - Update user");
        System.out.println("DELETE /api/users/{id}     - Delete user");
    }

    private static void initializeRoutes() {
        // Initialize GET routes
        Map<String, HttpHandler> getRoutes = new HashMap<>();
        getRoutes.put("/api/users", exchange -> handleGetAllUsers(exchange));
        getRoutes.put("/api/users/\\d+", exchange -> handleGetUserById(exchange));
        routes.put("GET", getRoutes);

        // Initialize POST routes
        Map<String, HttpHandler> postRoutes = new HashMap<>();
        postRoutes.put("/api/users", exchange -> handleCreateUser(exchange));
        routes.put("POST", postRoutes);

        // Initialize PUT routes
        Map<String, HttpHandler> putRoutes = new HashMap<>();
        putRoutes.put("/api/users/\\d+", exchange -> handleUpdateUser(exchange));
        routes.put("PUT", putRoutes);

        // Initialize DELETE routes
        Map<String, HttpHandler> deleteRoutes = new HashMap<>();
        deleteRoutes.put("/api/users/\\d+", exchange -> handleDeleteUser(exchange));
        routes.put("DELETE", deleteRoutes);
    }

    private static class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            System.out.println("Received request: " + method + " " + path);

            // Check if method is supported
            if (!routes.containsKey(method)) {
                sendResponse(exchange, 405, "Method Not Allowed", "text/plain");
                return;
            }

            // Find matching handler
            Map<String, HttpHandler> methodRoutes = routes.get(method);
            boolean handled = false;

            for (Map.Entry<String, HttpHandler> entry : methodRoutes.entrySet()) {
                String pattern = entry.getKey();
                if (path.matches(pattern)) {
                    try {
                        entry.getValue().handle(exchange);
                        handled = true;
                        break;
                    } catch (Exception e) {
                        sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage(), "text/plain");
                        e.printStackTrace();
                        handled = true;
                        break;
                    }
                }
            }

            if (!handled) {
                sendResponse(exchange, 404, "Not Found", "text/plain");
            }
        }
    }

    private static void handleGetAllUsers(HttpExchange exchange) throws IOException {
        String response = userController.getAllUsers();
        sendResponse(exchange, 200, response, "application/json");
    }

    private static void handleGetUserById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);

        String response = userController.getUserById(id);
        if (response != null) {
            sendResponse(exchange, 200, response, "application/json");
        } else {
            sendResponse(exchange, 404, "User not found", "text/plain");
        }
    }

    private static void handleCreateUser(HttpExchange exchange) throws IOException {
        // Read request body
        String requestBody = readRequestBody(exchange);
        
        // Create user
        String response = userController.createUser(requestBody);
        if (response != null) {
            sendResponse(exchange, 201, response, "application/json");
        } else {
            sendResponse(exchange, 400, "Invalid user data", "text/plain");
        }
    }

    private static void handleUpdateUser(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);
        
        // Read request body
        String requestBody = readRequestBody(exchange);
        
        // Update user
        String response = userController.updateUser(id, requestBody);
        if (response != null) {
            sendResponse(exchange, 200, response, "application/json");
        } else {
            sendResponse(exchange, 404, "User not found", "text/plain");
        }
    }

    private static void handleDeleteUser(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);
        
        boolean deleted = userController.deleteUser(id);
        if (deleted) {
            sendResponse(exchange, 204, "", "text/plain");
        } else {
            sendResponse(exchange, 404, "User not found", "text/plain");
        }
    }

    private static int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder requestBody = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        
        return requestBody.toString();
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        
        System.out.println("Sent response: " + statusCode + " " + response);
    }
}
