package si.fri.rso.vreme.version.services.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@ApplicationScoped
public class WeatherBean {

    @Inject
    private ObjectMapper objectMapper;

    private static final String API_KEY = "your_api_key";
    private static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";

    public JsonNode getWeather(double latitude, double longitude) throws IOException {
        Client client = ClientBuilder.newClient();
        String url = String.format(WEATHER_API_URL, latitude, longitude, API_KEY);

        Response response = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .get();

        String json = response.readEntity(String.class);
        client.close();

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
    }
}