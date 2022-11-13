package kanBan.web;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private String apiToken;
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request;

    public KVTaskClient(String url){
        this.url = url;

        try {
            request = HttpRequest.newBuilder()
                                .uri(URI.create(url + "register"))
                                .header("Accept", "application/json")
                                .GET()
                                .build();

            this.apiToken = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка регистрации!");
            throw new RuntimeException();
        }
    }

    public KVTaskClient(String url, String apiToken) {
        this.url = url;
        this.apiToken = apiToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void put(String key, String json) {
        try {
            request = HttpRequest.newBuilder()
                                .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                                .header("Accept", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(json))
                                .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка сохранения!");
            throw new RuntimeException();
        }
    }

    public String load(String key) {
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка загрузки данных!");
            throw new RuntimeException();
        }
    }

}
