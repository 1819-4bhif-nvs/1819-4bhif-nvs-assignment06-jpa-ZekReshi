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

public class SensorEndpointIT {

    private Client client;
    private WebTarget tut;

    @Before
    public void initClient() {
        this.client = ClientBuilder.newClient();
        this.tut = client.target("http://localhost:8080/homeautomation/API/sensors");
    }

    @Test
    public void getSensors() {
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        JsonArray payload = response.readEntity(JsonArray.class);
        assertThat(payload.get(0).asJsonObject().getString("name"), is("TEMP1"));
    }

    @Test
    public void getSensor() {
        Response response = this.tut
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        assertThat(payload.getString("name"), is("TEMP1"));
    }

    @Test
    public void postSensor() {
        JsonObject sensor = Json.createObjectBuilder()
                .add("name", "PRESS1")
                .add("location", Json.createObjectBuilder()
                    .add("id", 2))
                .build();
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(sensor));
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        assertThat(payload.getString("name"), is("PRESS1"));
    }

    @Test
    public void deleteSensor() {
        JsonObject sensor = Json.createObjectBuilder()
                .add("name", "PRESS1")
                .add("location", Json.createObjectBuilder()
                        .add("id", 1))
                .build();
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(sensor));
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
    public void putSensor() {
        JsonObject sensor = Json.createObjectBuilder()
                .add("name", "PRESS1")
                .add("location", Json.createObjectBuilder()
                        .add("id", 1))
                .build();
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(sensor));
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        JsonObject sensorToUpdate = Json.createObjectBuilder()
                .add("id", payload.getInt("id"))
                .add("name", payload.getString("name"))
                .add("location", Json.createObjectBuilder()
                        .add("id", 2))
                .build();
        response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(sensorToUpdate));
        assertThat(response.getStatus(), is(200));
        payload = response.readEntity(JsonObject.class);
        assertThat(payload.getJsonObject("location").getInt("id"), is(2));
    }

}