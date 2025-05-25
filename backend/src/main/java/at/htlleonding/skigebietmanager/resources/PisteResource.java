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

@Path("/api/pisten")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PisteResource {

    @Inject
    PisteRepository pisteRepository;

    @Inject
    SkiLiftRepository skiLiftRepository;

    @GET
    @Transactional
    public List<PisteDTO> getAllPisten() {
        return pisteRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getPisteById(@PathParam("id") Long id) {
        Piste piste = pisteRepository.findById(id);
        if (piste == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(piste)).build();
    }

    @GET
    @Path("/skilift/{skiLiftId}")
    @Transactional
    public Response getPistenBySkiLift(@PathParam("skiLiftId") Long skiLiftId) {
        SkiLift skiLift = skiLiftRepository.findById(skiLiftId);
        if (skiLift == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ski lift not found")
                    .build();
        }
        
        List<PisteDTO> pisten = skiLift.getPisten().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return Response.ok(pisten).build();
    }

    @POST
    @Transactional
    public Response createPiste(PisteDTO pisteDTO) {
        Piste piste = new Piste();
        piste.setName(pisteDTO.getName());
        piste.setSchwierigkeitsgrad(pisteDTO.getSchwierigkeitsgrad());
        piste.setLaenge(pisteDTO.getLaenge());
        
        if (pisteDTO.getSkiLift() != null) {
            SkiLift skiLift = skiLiftRepository.findById(pisteDTO.getSkiLift().getId());
            if (skiLift != null) {
                piste.setSkiLift(skiLift);
            }
        }
        
        pisteRepository.persist(piste);
        return Response.status(Response.Status.CREATED).entity(convertToDTO(piste)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePiste(@PathParam("id") Long id, PisteDTO pisteDTO) {
        Piste piste = pisteRepository.findById(id);
        if (piste == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        piste.setName(pisteDTO.getName());
        piste.setSchwierigkeitsgrad(pisteDTO.getSchwierigkeitsgrad());
        piste.setLaenge(pisteDTO.getLaenge());
        
        if (pisteDTO.getSkiLift() != null) {
            SkiLift skiLift = skiLiftRepository.findById(pisteDTO.getSkiLift().getId());
            if (skiLift != null) {
                piste.setSkiLift(skiLift);
            }
        }
        
        pisteRepository.persist(piste);
        return Response.ok(convertToDTO(piste)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePiste(@PathParam("id") Long id) {
        Piste piste = pisteRepository.findById(id);
        if (piste == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        pisteRepository.delete(piste);
        return Response.noContent().build();
    }

    private PisteDTO convertToDTO(Piste piste) {
        SkiLiftDTO skiLiftDTO = null;
        if (piste.getSkiLift() != null) {
            skiLiftDTO = new SkiLiftDTO(
                piste.getSkiLift().getId(),
                piste.getSkiLift().getName(),
                piste.getSkiLift().getTyp(),
                piste.getSkiLift().getKapazitaet()
            );
        }
        
        return new PisteDTO(
            piste.getId(),
            piste.getName(),
            piste.getSchwierigkeitsgrad(),
            piste.getLaenge(),
            skiLiftDTO
        );
    }
} 