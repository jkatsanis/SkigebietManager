package at.htlleonding.ressource;


import at.htlleonding.entities.Piste;
import at.htlleonding.entities.Skilift;
import at.htlleonding.repositories.PisteRepository;
import at.htlleonding.repositories.SkiliftRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/skilift")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SkiliftController {
    @Inject
    SkiliftRepository skiliftRepository;

    @POST
    @Transactional
    public Response addSkiLift(Skilift piste) {
        skiliftRepository.persist(piste); // Save the piste to the database
        return Response.status(Response.Status.CREATED).entity(piste).build(); // Return the created piste with a 201 status
    }

    @GET
    public Response getAllLifte() {
        List<Skilift> pisten = skiliftRepository.listAll(); // Use the repository to fetch all Pisten


        return Response.ok(pisten).build(); // Return the list of Pisten if found
    }
}
