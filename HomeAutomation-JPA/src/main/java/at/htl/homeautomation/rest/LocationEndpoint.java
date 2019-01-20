package at.htl.homeautomation.rest;

import at.htl.homeautomation.model.Location;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("locations")
@Stateless
public class LocationEndpoint {

    @PersistenceContext
    EntityManager em;

    @GET
    public Response findAll() {
        TypedQuery<Location> query = em.createNamedQuery("Location.findAll", Location.class);
        List<Location> locations = query.getResultList();
        return Response.ok().entity(locations).build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") long id) {
        Location location = em.find(Location.class, id);
        if (location == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(location).build();
    }

    @GET
    @Path("{id}/devices")
    public Response findDevicesById(@PathParam("id") long id) {
        Location location = em.find(Location.class, id);
        if (location == null || location.getDevices().isEmpty()) {
            return Response.status(404).build();
        }
        return Response.ok().entity(location.getDevices()).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") long id) {
        Location location = em.find(Location.class, id);
        if (location == null) {
            return Response.status(404).build();
        }
        em.remove(location);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(Location location) {
        em.persist(location);
        return Response.ok().entity(location).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Location location) {
        Location oldLocation = em.find(Location.class, location.getId());
        if (oldLocation == null) {
            return Response.status(404).build();
        }
        location.setId(oldLocation.getId());
        em.merge(location);
        return Response.ok().entity(location).build();
    }

}
