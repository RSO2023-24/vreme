package si.fri.rso.vreme.version.api.v1.resources;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.tika.io.IOException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import si.fri.rso.vreme.version.services.beans.WeatherBean;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VremeResource {

    @Inject
    private WeatherBean weatherBean;

    @GET
    @Path("/current")
    @Operation(description = "Get current weather for a location.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Current weather data.")
    })
        public Response getWeather(@QueryParam("lat") double latitude, @QueryParam("lon") double longitude) throws IOException {
        JsonNode weatherData = weatherBean.getWeather(latitude, longitude);
        return Response.ok(weatherData).build();
        }
}