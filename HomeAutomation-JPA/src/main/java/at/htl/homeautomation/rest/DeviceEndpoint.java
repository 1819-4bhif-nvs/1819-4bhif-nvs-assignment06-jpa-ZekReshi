package at.htl.homeautomation.rest;

import at.htl.homeautomation.model.Device;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("devices")
@Stateless
public class DeviceEndpoint {

    @PersistenceContext
    EntityManager em;

    @GET
    public Response findAll() {
        TypedQuery<Device> query = em.createNamedQuery("Device.findAll", Device.class);
        List<Device> devices = query.getResultList();
        return Response.ok().entity(devices).build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") long id) {
        Device device = em.find(Device.class, id);
        if (device == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(device).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") long id) {
        Device device = em.find(Device.class, id);
        if (device == null) {
            return Response.status(404).build();
        }
        em.remove(device);
        return Response.noContent().build();
    }

}
