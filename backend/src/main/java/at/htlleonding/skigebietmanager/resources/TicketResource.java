package at.htlleonding.skigebietmanager.resources;

import at.htlleonding.skigebietmanager.dto.TicketDTO;
import at.htlleonding.skigebietmanager.dto.UserDTO;
import at.htlleonding.skigebietmanager.entities.Ticket;
import at.htlleonding.skigebietmanager.entities.User;
import at.htlleonding.skigebietmanager.repositories.TicketRepository;
import at.htlleonding.skigebietmanager.repositories.UserRepository;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Ticket Management", description = "Operations for managing ski tickets")
public class TicketResource {

    @Inject
    TicketRepository ticketRepository;

    @Inject
    UserRepository userRepository;

    @GET
    @Transactional
    @Operation(summary = "Get all tickets", description = "Retrieves a list of all tickets in the system")
    @APIResponse(responseCode = "200", description = "List of tickets retrieved successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = TicketDTO.class, type = SchemaType.ARRAY)))
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Get ticket by ID", description = "Retrieves a specific ticket by its ID")
    @APIResponse(responseCode = "200", description = "Ticket found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = TicketDTO.class)))
    @APIResponse(responseCode = "404", description = "Ticket not found")
    public Response getTicketById(
        @Parameter(description = "Ticket ID", required = true) @PathParam("id") Long id) {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(ticket)).build();
    }

    @GET
    @Path("/user/{userId}")
    @Transactional
    @Operation(summary = "Get tickets by user", description = "Retrieves all tickets associated with a specific user")
    @APIResponse(responseCode = "200", description = "List of tickets retrieved successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = TicketDTO.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "404", description = "User not found")
    public Response getTicketsByUser(
        @Parameter(description = "User ID", required = true) @PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .build();
        }
        
        List<TicketDTO> tickets = user.getTickets().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return Response.ok(tickets).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Create new ticket", description = "Creates a new ticket in the system")
    @APIResponse(responseCode = "201", description = "Ticket created successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = TicketDTO.class)))
    @APIResponse(responseCode = "400", description = "Invalid input data")
    public Response createTicket(
        @Parameter(description = "Ticket data", required = true) TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setTicketType(ticketDTO.getTicketType());
        
        // Set default values for required fields if not provided
        if (ticketDTO.getDate() == null) {
            ticket.setDate(LocalDate.now());
        } else {
            ticket.setDate(ticketDTO.getDate());
        }
        
        if (ticketDTO.getValidFrom() == null) {
            ticket.setValidFrom(ticketDTO.getTicketType().equals("Ganztages") ? 
                LocalTime.of(8, 0) : LocalTime.of(12, 0));
        } else {
            ticket.setValidFrom(ticketDTO.getValidFrom());
        }
        
        if (ticketDTO.getValidUntil() == null) {
            ticket.setValidUntil(LocalTime.of(16, 0));
        } else {
            ticket.setValidUntil(ticketDTO.getValidUntil());
        }
        
        if (ticketDTO.getUser() != null) {
            User user = userRepository.findById(ticketDTO.getUser().getId());
            if (user != null) {
                ticket.setUser(user);
            }
        }
        
        ticketRepository.persist(ticket);
        return Response.status(Response.Status.CREATED).entity(convertToDTO(ticket)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update ticket", description = "Updates an existing ticket's information")
    @APIResponse(responseCode = "200", description = "Ticket updated successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = TicketDTO.class)))
    @APIResponse(responseCode = "404", description = "Ticket not found")
    public Response updateTicket(
        @Parameter(description = "Ticket ID", required = true) @PathParam("id") Long id,
        @Parameter(description = "Updated ticket data", required = true) TicketDTO ticketDTO) {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        ticket.setTicketType(ticketDTO.getTicketType());
        ticket.setDate(ticketDTO.getDate());
        ticket.setValidFrom(ticketDTO.getValidFrom());
        ticket.setValidUntil(ticketDTO.getValidUntil());
        
        if (ticketDTO.getUser() != null) {
            User user = userRepository.findById(ticketDTO.getUser().getId());
            if (user != null) {
                ticket.setUser(user);
            }
        }
        
        ticketRepository.persist(ticket);
        return Response.ok(convertToDTO(ticket)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete ticket", description = "Deletes a ticket from the system")
    @APIResponse(responseCode = "204", description = "Ticket deleted successfully")
    @APIResponse(responseCode = "404", description = "Ticket not found")
    public Response deleteTicket(
        @Parameter(description = "Ticket ID", required = true) @PathParam("id") Long id) {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ticketRepository.delete(ticket);
        return Response.noContent().build();
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        UserDTO userDTO = null;
        if (ticket.getUser() != null) {
            userDTO = new UserDTO(
                ticket.getUser().getId(),
                ticket.getUser().getName(),
                ticket.getUser().getEmail()
            );
        }
        
        return new TicketDTO(
            ticket.getId(),
            ticket.getTicketType(),
            ticket.getDate(),
            ticket.getValidFrom(),
            ticket.getValidUntil(),
            userDTO
        );
    }
} 