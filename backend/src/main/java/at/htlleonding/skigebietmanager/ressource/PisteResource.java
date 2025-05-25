package at.htlleonding.skigebietmanager.ressource;

import at.htlleonding.skigebietmanager.entities.Piste;
import at.htlleonding.skigebietmanager.services.PisteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/pisten")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PisteResource {
    @Inject
    PisteService pisteService;

    @GET
    public List<Piste> getAllPisten() {
        return pisteService.getAllPisten();
    }

    @GET
    @Path("/{id}")
    public Response getPisteById(@PathParam("id") Long id) {
        Piste piste = pisteService.getPisteById(id);
        if (piste != null) {
            return Response.ok(piste).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/skilift/{skiLiftId}")
    public List<Piste> getPistenBySkiLiftId(@PathParam("skiLiftId") Long skiLiftId) {
        return pisteService.getPistenBySkiLiftId(skiLiftId);
    }

    @POST
    public Response createPiste(Piste piste) {
        Piste created = pisteService.createPiste(piste);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePiste(@PathParam("id") Long id, Piste piste) {
        Piste updated = pisteService.updatePiste(id, piste);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePiste(@PathParam("id") Long id) {
        boolean deleted = pisteService.deletePiste(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
} 