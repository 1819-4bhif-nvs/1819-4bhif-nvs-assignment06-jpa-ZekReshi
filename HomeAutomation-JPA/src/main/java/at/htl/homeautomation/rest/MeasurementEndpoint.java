package at.htl.homeautomation.rest;

import at.htl.homeautomation.model.Measurement;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("measurements")
@Stateless
public class MeasurementEndpoint {

    @PersistenceContext
    EntityManager em;

    @GET
    public Response findAll() {
        TypedQuery<Measurement> query = em.createNamedQuery("Measurement.findAll", Measurement.class);
        List<Measurement> measurements = query.getResultList();
        return Response.ok().entity(measurements).build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") long id) {
        Measurement measurement = em.find(Measurement.class, id);
        if (measurement == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(measurement).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") long id) {
        Measurement measurement = em.find(Measurement.class, id);
        if (measurement == null) {
            return Response.status(404).build();
        }
        em.remove(measurement);
        return Response.noContent().build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(Measurement measurement) {
        em.persist(measurement);
        return Response.ok().entity(measurement).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Measurement measurement) {
        Measurement oldMeasurement = em.find(Measurement.class, measurement.getId());
        if (oldMeasurement == null) {
            return Response.status(404).build();
        }
        measurement.setId(oldMeasurement.getId());
        em.merge(measurement);
        return Response.ok().entity(measurement).build();
    }

}
