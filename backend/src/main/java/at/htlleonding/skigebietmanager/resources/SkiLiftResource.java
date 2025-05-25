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

@Path("/api/skilifts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Ski Lift Management", description = "Operations for managing ski lifts")
public class SkiLiftResource {

    @Inject
    SkiLiftRepository skiLiftRepository;

    @Inject
    PisteRepository pisteRepository;

    @GET
    @Transactional
    @Operation(summary = "Get all ski lifts", description = "Retrieves a list of all ski lifts in the system")
    @APIResponse(responseCode = "200", description = "List of ski lifts retrieved successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = SkiLiftDTO.class, type = SchemaType.ARRAY)))
    public List<SkiLiftDTO> getAllSkiLifts() {
        return skiLiftRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Get ski lift by ID", description = "Retrieves a specific ski lift by its ID")
    @APIResponse(responseCode = "200", description = "Ski lift found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = SkiLiftDTO.class)))
    @APIResponse(responseCode = "404", description = "Ski lift not found")
    public Response getSkiLiftById(
        @Parameter(description = "Ski lift ID", required = true) @PathParam("id") Long id) {
        SkiLift skiLift = skiLiftRepository.findById(id);
        if (skiLift == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(skiLift)).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Create new ski lift", description = "Creates a new ski lift in the system")
    @APIResponse(responseCode = "201", description = "Ski lift created successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = SkiLiftDTO.class)))
    @APIResponse(responseCode = "400", description = "Invalid input data")
    public Response createSkiLift(
        @Parameter(description = "Ski lift data", required = true) SkiLiftDTO skiLiftDTO) {
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
    @Operation(summary = "Update ski lift", description = "Updates an existing ski lift's information")
    @APIResponse(responseCode = "200", description = "Ski lift updated successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = SkiLiftDTO.class)))
    @APIResponse(responseCode = "404", description = "Ski lift not found")
    public Response updateSkiLift(
        @Parameter(description = "Ski lift ID", required = true) @PathParam("id") Long id,
        @Parameter(description = "Updated ski lift data", required = true) SkiLiftDTO skiLiftDTO) {
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
    @Operation(summary = "Delete ski lift", description = "Deletes a ski lift from the system")
    @APIResponse(responseCode = "204", description = "Ski lift deleted successfully")
    @APIResponse(responseCode = "404", description = "Ski lift not found")
    public Response deleteSkiLift(
        @Parameter(description = "Ski lift ID", required = true) @PathParam("id") Long id) {
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
    @Operation(summary = "Get ski lift pisten", description = "Retrieves all ski slopes associated with a specific ski lift")
    @APIResponse(responseCode = "200", description = "List of pisten retrieved successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PisteDTO.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "404", description = "Ski lift not found")
    public Response getSkiLiftPisten(
        @Parameter(description = "Ski lift ID", required = true) @PathParam("id") Long id) {
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