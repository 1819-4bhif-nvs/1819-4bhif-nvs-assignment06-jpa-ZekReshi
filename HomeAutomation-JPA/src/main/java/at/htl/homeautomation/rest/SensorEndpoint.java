package at.htl.homeautomation.rest;

import at.htl.homeautomation.model.Sensor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("sensors")
@Stateless
public class SensorEndpoint {

    @PersistenceContext
    EntityManager em;

    @GET
    public Response findAll() {
        TypedQuery<Sensor> query = em.createNamedQuery("Sensor.findAll", Sensor.class);
        List<Sensor> sensors = query.getResultList();
        return Response.ok().entity(sensors).build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") long id) {
        Sensor sensor = em.find(Sensor.class, id);
        if (sensor == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(sensor).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") long id) {
        Sensor sensor = em.find(Sensor.class, id);
        if (sensor == null) {
            return Response.status(404).build();
        }
        em.remove(sensor);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(Sensor sensor) {
        em.persist(sensor);
        return Response.ok().entity(sensor).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response put(@PathParam("id") long id, Sensor sensor) {
        Sensor oldSensor = em.find(Sensor.class, id);
        if (oldSensor == null) {
            return Response.status(404).build();
        }
        sensor.setId(oldSensor.getId());
        em.merge(sensor);
        return Response.ok().entity(sensor).build();
    }

}
