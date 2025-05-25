package at.htlleonding.skigebietmanager.ressource;

import at.htlleonding.skigebietmanager.entities.SkiLift;
import at.htlleonding.skigebietmanager.services.SkiLiftService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/skilifts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SkiLiftResource {
    @Inject
    SkiLiftService skiLiftService;

    @GET
    public List<SkiLift> getAllSkiLifts() {
        return skiLiftService.getAllSkiLifts();
    }

    @GET
    @Path("/{id}")
    public Response getSkiLiftById(@PathParam("id") Long id) {
        SkiLift skiLift = skiLiftService.getSkiLiftById(id);
        if (skiLift != null) {
            return Response.ok(skiLift).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createSkiLift(SkiLift skiLift) {
        SkiLift created = skiLiftService.createSkiLift(skiLift);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSkiLift(@PathParam("id") Long id, SkiLift skiLift) {
        SkiLift updated = skiLiftService.updateSkiLift(id, skiLift);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSkiLift(@PathParam("id") Long id) {
        boolean deleted = skiLiftService.deleteSkiLift(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
} 