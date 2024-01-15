package si.fri.rso.vreme.version.services.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.asynchttpclient.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WeatherBean {

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String API_KEY = "0a2a5e4ebbmshbb4dcbbf827d3f3p1b165ajsn37521aec4877";
    private static final String WEATHER_API_URL = "https://open-weather13.p.rapidapi.com/city/latlon/%s/%s";

    public JsonNode getWeather(double latitude, double longitude) {
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            String url = String.format(WEATHER_API_URL, latitude, longitude);

            CompletableFuture<Response> future = client.prepareGet(url)
                    .addHeader("X-RapidAPI-Key", API_KEY)
                    .addHeader("X-RapidAPI-Host", "open-weather13.p.rapidapi.com")
                    .execute()
                    .toCompletableFuture();

            client.close();

            Response response = future.join(); // Use join() instead of get()

            String json = response.getResponseBody();

            JsonNode rootNode = objectMapper.readTree(json);

            JsonNode mainNode = rootNode.path("main");
            JsonNode visibilityNode = rootNode.path("visibility");
            JsonNode cloudsNode = rootNode.path("clouds");
            JsonNode windNode = rootNode.path("wind");

            // Create a new JSON object with only the fields you want to keep
            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.set("main", mainNode);
            resultNode.set("visibility", visibilityNode);
            resultNode.set("clouds", cloudsNode);
            resultNode.set("wind", windNode);

            return resultNode;
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return null;
        }
    }
}