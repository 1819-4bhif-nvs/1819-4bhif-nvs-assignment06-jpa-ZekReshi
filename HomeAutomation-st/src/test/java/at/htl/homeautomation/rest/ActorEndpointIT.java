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

public class ActorEndpointIT {

    private Client client;
    private WebTarget tut;

    @Before
    public void initClient() {
        this.client = ClientBuilder.newClient();
        this.tut = client.target("http://localhost:8080/homeautomation/API/actors");
    }

    @Test
    public void getActors() {
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        JsonArray payload = response.readEntity(JsonArray.class);
        assertThat(payload.get(0).asJsonObject().getString("name"), is("Heizung"));
    }

    @Test
    public void getActor() {
        Response response = this.tut
                .path("5")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        assertThat(payload.getString("name"), is("Heizung"));
    }

    @Test
    public void postActor() {
        JsonObject actor = Json.createObjectBuilder()
                .add("name", "ENGINE1")
                .add("location", Json.createObjectBuilder()
                        .add("id", 1))
                .build();
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(actor));
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        assertThat(payload.getString("name"), is("ENGINE1"));
    }

    @Test
    public void deleteActor() {
        JsonObject actor = Json.createObjectBuilder()
                .add("name", "ENGINE1")
                .add("location", Json.createObjectBuilder()
                        .add("id", 1))
                .build();
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(actor));
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
    public void putActor() {
        JsonObject actor = Json.createObjectBuilder()
                .add("name", "ENGINE1")
                .add("location", Json.createObjectBuilder()
                        .add("id", 1))
                .build();
        Response response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(actor));
        assertThat(response.getStatus(), is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        JsonObject actorToUpdate = Json.createObjectBuilder()
                .add("id", payload.getInt("id"))
                .add("name", payload.getString("name"))
                .add("location", Json.createObjectBuilder()
                        .add("id", 2))
                .build();
        response = this.tut
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actorToUpdate));
        assertThat(response.getStatus(), is(200));
        payload = response.readEntity(JsonObject.class);
        assertThat(payload.getJsonObject("location").getInt("id"), is(2));
    }

}
