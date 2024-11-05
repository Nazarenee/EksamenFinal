package dat.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PackingService {
    public static String fetchPackingList(String category) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://packingapi.cphbusinessapps.dk/packinglist/" + category))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to fetch packing list.\"}";
        }
    }


    public double calculatePackingListWeight(String category) throws JsonProcessingException {
        String packingListResponse = fetchPackingList(category);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode packingListNode = objectMapper.readTree(packingListResponse);

        double totalWeight = 0.0;
        if (packingListNode.has("items")) {
            for (JsonNode item : packingListNode.get("items")) {
                totalWeight += item.path("weightInGrams").asDouble();
            }
        }
        return totalWeight;
    }
}
