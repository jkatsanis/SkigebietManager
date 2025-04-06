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
    @Transactional
    public Response addPiste(Piste piste) {
        pisteRepository.persist(piste); // Save the piste to the database
        return Response.status(Response.Status.CREATED).entity(piste).build(); // Return the created piste with a 201 status
    }

    @GET
    @Path("/{id}") // URL path will be /piste/{id}
    public Response getPiste(@PathParam("id") Long id) {
        Piste piste = pisteRepository.findById(id); // Use the repository to find a Piste by ID

        if (piste == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Piste not found").build(); // Return 404 if Piste not found
        }

        return Response.ok(piste).build(); // Return the Piste if found
    }

    @GET
    public Response getAllPisten() {
        List<Piste> pisten = pisteRepository.listAll(); // Use the repository to fetch all Pisten


        return Response.ok(pisten).build(); // Return the list of Pisten if found
    }
}
