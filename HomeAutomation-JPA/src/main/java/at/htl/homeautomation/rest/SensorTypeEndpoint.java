package at.htl.homeautomation.rest;

import at.htl.homeautomation.model.SensorType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("sensortypes")
@Stateless
public class SensorTypeEndpoint {

    @PersistenceContext
    EntityManager em;

    @GET
    public Response findAll() {
        TypedQuery<SensorType> query = em.createNamedQuery("SensorType.findAll", SensorType.class);
        List<SensorType> sensorTypes = query.getResultList();
        return Response.ok().entity(sensorTypes).build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") long id) {
        SensorType sensorType = em.find(SensorType.class, id);
        if (sensorType == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(sensorType).build();
    }

    @GET
    @Path("{id}/sensors")
    public Response findSensorsById(@PathParam("id") long id) {
        SensorType location = em.find(SensorType.class, id);
        if (location == null || location.getSensors().isEmpty()) {
            return Response.status(404).build();
        }
        return Response.ok().entity(location.getSensors()).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") long id) {
        SensorType sensorType = em.find(SensorType.class, id);
        if (sensorType == null) {
            return Response.status(404).build();
        }
        em.remove(sensorType);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(SensorType sensorType) {
        em.persist(sensorType);
        return Response.ok().entity(sensorType).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response put(@PathParam("id") long id, SensorType sensorType) {
        SensorType oldSensorType = em.find(SensorType.class, id);
        if (oldSensorType == null) {
            return Response.status(404).build();
        }
        sensorType.setId(oldSensorType.getId());
        em.merge(sensorType);
        return Response.ok().entity(sensorType).build();
    }

}
