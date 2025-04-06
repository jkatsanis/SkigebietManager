package at.htlleonding.ressource;

import at.htlleonding.entities.Benutzer;
import at.htlleonding.repositories.BenutzerRepository;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/benutzer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BenutzerController {

    @Inject
    BenutzerRepository benutzerRepository;

    @GET
    public List<Benutzer> getAllBenutzer() {
        return benutzerRepository.listAll();
    }

    @GET
    @Path("/email/{email}")
    public Response getBenutzerByEmail(@PathParam("email") String email) {
        Benutzer benutzer = benutzerRepository.findByEmail(email);
        if (benutzer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(benutzer).build();
    }

    @POST
    @Transactional
    public Response addBenutzer(Benutzer benutzer) {
        benutzerRepository.persist(benutzer);
        return Response.status(Response.Status.CREATED).entity(benutzer).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateBenutzer(@PathParam("id") Long id, Benutzer updatedBenutzer) {
        Benutzer benutzer = benutzerRepository.findById(id);
        if (benutzer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        benutzer.setName(updatedBenutzer.getName());
        benutzer.setEmail(updatedBenutzer.getEmail());

        benutzerRepository.persist(benutzer);
        return Response.ok(benutzer).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteBenutzer(@PathParam("id") Long id) {
        boolean deleted = benutzerRepository.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}