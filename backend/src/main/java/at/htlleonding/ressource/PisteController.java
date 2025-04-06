package at.htlleonding.ressource;

import at.htlleonding.entities.Piste;
import at.htlleonding.repositories.PisteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/piste")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PisteController {

    @Inject
    PisteRepository pisteRepository;

    @POST
    public Response addPiste(Piste piste) {
        pisteRepository.persist(piste); // Save the piste to the database
        return Response.status(Response.Status.CREATED).entity(piste).build(); // Return the created piste with a 201 status
    }
}
