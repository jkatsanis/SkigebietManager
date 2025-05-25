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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/pisten")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Piste Management", description = "Operations for managing ski slopes (pisten)")
public class PisteResource {

    @Inject
    PisteRepository pisteRepository;

    @Inject
    SkiLiftRepository skiLiftRepository;

    @GET
    @Transactional
    @Operation(summary = "Get all pisten", description = "Retrieves a list of all ski slopes in the system")
    @APIResponse(responseCode = "200", description = "List of pisten retrieved successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PisteDTO.class, type = SchemaType.ARRAY)))
    public List<PisteDTO> getAllPisten() {
        return pisteRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Get piste by ID", description = "Retrieves a specific ski slope by its ID")
    @APIResponse(responseCode = "200", description = "Piste found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PisteDTO.class)))
    @APIResponse(responseCode = "404", description = "Piste not found")
    public Response getPisteById(
        @Parameter(description = "Piste ID", required = true) @PathParam("id") Long id) {
        Piste piste = pisteRepository.findById(id);
        if (piste == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(piste)).build();
    }

    @GET
    @Path("/skilift/{skiLiftId}")
    @Transactional
    @Operation(summary = "Get pisten by ski lift", description = "Retrieves all ski slopes associated with a specific ski lift")
    @APIResponse(responseCode = "200", description = "List of pisten retrieved successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PisteDTO.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "404", description = "Ski lift not found")
    public Response getPistenBySkiLift(
        @Parameter(description = "Ski lift ID", required = true) @PathParam("skiLiftId") Long skiLiftId) {
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
    @Operation(summary = "Create new piste", description = "Creates a new ski slope in the system")
    @APIResponse(responseCode = "201", description = "Piste created successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PisteDTO.class)))
    @APIResponse(responseCode = "400", description = "Invalid input data")
    public Response createPiste(
        @Parameter(description = "Piste data", required = true) PisteDTO pisteDTO) {
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
    @Operation(summary = "Update piste", description = "Updates an existing ski slope's information")
    @APIResponse(responseCode = "200", description = "Piste updated successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PisteDTO.class)))
    @APIResponse(responseCode = "404", description = "Piste not found")
    public Response updatePiste(
        @Parameter(description = "Piste ID", required = true) @PathParam("id") Long id,
        @Parameter(description = "Updated piste data", required = true) PisteDTO pisteDTO) {
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
    @Operation(summary = "Delete piste", description = "Deletes a ski slope from the system")
    @APIResponse(responseCode = "204", description = "Piste deleted successfully")
    @APIResponse(responseCode = "404", description = "Piste not found")
    public Response deletePiste(
        @Parameter(description = "Piste ID", required = true) @PathParam("id") Long id) {
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