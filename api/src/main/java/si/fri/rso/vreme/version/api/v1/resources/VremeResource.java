package si.fri.rso.vreme.version.api.v1.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import si.fri.rso.vreme.version.api.v1.interceptor.LogContextInterceptor;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Interceptors(LogContextInterceptor.class)
public class VremeResource {

    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current=temperature_2m,relative_humidity_2m,precipitation,cloud_cover,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m&hourly=temperature_2m&elevation=%s";

    @GET
    @Path("/current")
    @Operation(description = "Get current weather for a location.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Current weather data.")
    })
    public Response getWeather(@QueryParam("lat") double latitude, @QueryParam("lon") double longitude, @QueryParam("elevation") double elevation) {
        try (AsyncHttpClient client = Dsl.asyncHttpClient()) {
            String url = String.format(WEATHER_API_URL, latitude, longitude, elevation);

            CompletableFuture<org.asynchttpclient.Response> future = client.prepareGet(url)
                    .execute()
                    .toCompletableFuture();

            org.asynchttpclient.Response response = future.get();

            String json = response.getResponseBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            return Response.status(Response.Status.OK).entity(rootNode.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}