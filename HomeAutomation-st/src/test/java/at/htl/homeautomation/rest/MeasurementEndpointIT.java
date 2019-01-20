package at.htl.homeautomation.rest;

import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MeasurementEndpointIT {

    private Client client;
    private WebTarget tut;

    @Before
    public void initClient() {
        this.client = ClientBuilder.newClient();
        this.tut = client.target("http://localhost:8080/homeautomation/API/measurements");
    }

    @Test
    public void getMeasurements() {
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        JsonArray payload = response.readEntity(JsonArray.class);
        assertThat(payload.get(0).asJsonObject().getString("unit"), is("degC"));
    }

    @Test
    public void getMeasurement() {
        Response response = this.tut
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        assertThat(payload.getString("unit"), is("degC"));
    }

    @Test
    public void postMeasurement() {
        JsonObject measurement = Json.createObjectBuilder()
                .add("value", 22.5)
                .add("unit", "degC")
                .add("sensor", Json.createObjectBuilder()
                    .add("id", 1))
                .add("time", "2018-12-08T12:04:32")
                .build();
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(measurement));
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        assertThat(payload.getString("unit"), is("degC"));
    }

    @Test
    public void deleteMeasurement() {
        JsonObject location = Json.createObjectBuilder()
                .add("value", 22.5)
                .add("unit", "degC")
                .add("sensor", Json.createObjectBuilder()
                        .add("id", 1))
                .add("time", "2018-12-08T12:04:32")
                .build();
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(location));
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        response = this.tut
                .path("" + payload.getInt("id"))
                .request(MediaType.APPLICATION_JSON)
                .delete();
        assertThat(response.getStatus(), is(204));
        response = this.tut
                .path("" + payload.getInt("id"))
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void putMeasurement() {
        JsonObject measurement = Json.createObjectBuilder()
                .add("value", 22.5)
                .add("unit", "degC")
                .add("sensor", Json.createObjectBuilder()
                        .add("id", 1))
                .add("time", "2018-12-08T12:04:32")
                .build();
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(measurement));
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        JsonObject measurementToUpdate = Json.createObjectBuilder()
                .add("id", payload.getInt("id"))
                .add("value", payload.getJsonNumber("value").doubleValue())
                .add("unit", payload.getString("unit"))
                .add("sensor", Json.createObjectBuilder()
                        .add("id", 2))
                .add("time", payload.getString("time"))
                .build();
        response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(measurementToUpdate));
        assertThat(response.getStatus(), is(200));
        payload = response.readEntity(JsonObject.class);
        assertThat(payload.getJsonObject("sensor").getInt("id"), is(2));
    }

}
