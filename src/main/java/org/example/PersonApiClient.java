package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class PersonApiClient {
    private static final String BASE_URL = "http://localhost:8080/api/persons";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== PersonAPI Client ===");
            System.out.println("1. Add person");
            System.out.println("2. List all persons");
            System.out.println("3. Get person by ID");
            System.out.println("4. Update person");
            System.out.println("5. Delete person");
            System.out.println("0. Exit");
            System.out.println("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addPerson();
                case "2" -> listAllPersons();
                case "3" -> getPersonById();
                case "4" -> updatePerson();
                case "5" -> deletePerson();
                case "0" -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addPerson() {
        System.out.print("ID: ");
        long id = Long.parseLong(scanner.nextLine());
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        String json = String.format("{\"id\": %d, \"name\": \"%s\", \"age\": %d}", id, name, age);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        sendAndPrintResponse(request);
    }

    private static void listAllPersons() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        sendAndPrintResponse(request);
    }

    private static void getPersonById() {
        System.out.print("ID: ");
        long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        sendAndPrintResponse(request);
    }

    private static void updatePerson() {
        System.out.print("ID of person to update: ");
        long id = Long.parseLong(scanner.nextLine());
        System.out.print("New name: ");
        String name = scanner.nextLine();
        System.out.print("New age: ");
        int age = Integer.parseInt(scanner.nextLine());

        String json = String.format("{\"name\": \"%s\", \"age\": %d}", name, age);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        sendAndPrintResponse(request);
    }

    private static void deletePerson() {
        System.out.print("ID of person to delete: ");
        long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        sendAndPrintResponse(request);
    }

    private static void sendAndPrintResponse(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status code: " + response.statusCode());
            System.out.println("Response: " + response.body());
        } catch (Exception e) {
            System.out.println("Error communication with API: " + e.getMessage());
        }
    }
}
