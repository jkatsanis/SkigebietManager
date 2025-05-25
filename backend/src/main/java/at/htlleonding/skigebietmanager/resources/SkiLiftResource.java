package at.htlleonding.skigebietmanager.resources;

import at.htlleonding.skigebietmanager.dto.PisteDTO;
import at.htlleonding.skigebietmanager.dto.SkiLiftDTO;
import at.htlleonding.skigebietmanager.entities.Piste;
import at.htlleonding.skigebietmanager.entities.SkiLift;
import at.htlleonding.skigebietmanager.repositories.PisteRepository;
import at.htlleonding.skigebietmanager.repositories.SkiLiftRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/skilifts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SkiLiftResource {

    @Inject
    SkiLiftRepository skiLiftRepository;

    @Inject
    PisteRepository pisteRepository;

    @GET
    @Transactional
    public List<SkiLiftDTO> getAllSkiLifts() {
        return skiLiftRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getSkiLiftById(@PathParam("id") Long id) {
        SkiLift skiLift = skiLiftRepository.findById(id);
        if (skiLift == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(skiLift)).build();
    }

    @POST
    @Transactional
    public Response createSkiLift(SkiLiftDTO skiLiftDTO) {
        SkiLift skiLift = new SkiLift();
        skiLift.setName(skiLiftDTO.getName());
        skiLift.setTyp(skiLiftDTO.getTyp());
        skiLift.setKapazitaet(skiLiftDTO.getKapazitaet());
        skiLiftRepository.persist(skiLift);
        return Response.status(Response.Status.CREATED).entity(convertToDTO(skiLift)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateSkiLift(@PathParam("id") Long id, SkiLiftDTO skiLiftDTO) {
        SkiLift skiLift = skiLiftRepository.findById(id);
        if (skiLift == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        skiLift.setName(skiLiftDTO.getName());
        skiLift.setTyp(skiLiftDTO.getTyp());
        skiLift.setKapazitaet(skiLiftDTO.getKapazitaet());
        skiLiftRepository.persist(skiLift);
        return Response.ok(convertToDTO(skiLift)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteSkiLift(@PathParam("id") Long id) {
        SkiLift skiLift = skiLiftRepository.findById(id);
        if (skiLift == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        skiLiftRepository.delete(skiLift);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/pisten")
    @Transactional
    public Response getSkiLiftPisten(@PathParam("id") Long id) {
        SkiLift skiLift = skiLiftRepository.findById(id);
        if (skiLift == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<PisteDTO> pisten = skiLift.getPisten().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Response.ok(pisten).build();
    }

    private SkiLiftDTO convertToDTO(SkiLift skiLift) {
        return new SkiLiftDTO(
            skiLift.getId(),
            skiLift.getName(),
            skiLift.getTyp(),
            skiLift.getKapazitaet()
        );
    }

    private PisteDTO convertToDTO(Piste piste) {
        return new PisteDTO(
            piste.getId(),
            piste.getName(),
            piste.getSchwierigkeitsgrad(),
            piste.getLaenge(),
            convertToDTO(piste.getSkiLift())
        );
    }
} 