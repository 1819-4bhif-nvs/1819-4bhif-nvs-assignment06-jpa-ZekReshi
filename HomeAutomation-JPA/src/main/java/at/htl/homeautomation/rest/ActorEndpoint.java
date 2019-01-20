package at.htl.homeautomation.rest;

import at.htl.homeautomation.model.Actor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("actors")
@Stateless
public class ActorEndpoint {

    @PersistenceContext
    EntityManager em;

    @GET
    public Response findAll() {
        TypedQuery<Actor> query = em.createNamedQuery("Actor.findAll", Actor.class);
        List<Actor> actors = query.getResultList();
        return Response.ok().entity(actors).build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") long id) {
        Actor actor = em.find(Actor.class, id);
        if (actor == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(actor).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") long id) {
        Actor actor = em.find(Actor.class, id);
        if (actor == null) {
            return Response.status(404).build();
        }
        em.remove(actor);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(Actor actor) {
        em.persist(actor);
        return Response.ok().entity(actor).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Actor actor) {
        Actor oldActor = em.find(Actor.class, actor.getId());
        if (oldActor == null) {
            return Response.status(404).build();
        }
        actor.setId(oldActor.getId());
        em.merge(actor);
        return Response.ok().entity(actor).build();
    }

}
