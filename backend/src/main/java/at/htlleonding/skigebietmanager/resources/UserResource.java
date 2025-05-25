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
import jakarta.persistence.PersistenceException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User Management", description = "Operations for managing users")
public class UserResource {

    @Inject
    UserRepository userRepository;

    @Inject
    TicketRepository ticketRepository;

    @GET
    @Transactional
    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system")
    @APIResponse(responseCode = "200", description = "List of users retrieved successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = UserDTO.class, type = SchemaType.ARRAY)))
    public List<UserDTO> getAllUsers() {
        return userRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
    @APIResponse(responseCode = "200", description = "User found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = UserDTO.class)))
    @APIResponse(responseCode = "404", description = "User not found")
    public Response getUserById(
        @Parameter(description = "User ID", required = true) @PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(user)).build();
    }

    @GET
    @Path("/email/{email}")
    @Transactional
    @Operation(summary = "Get user by email", description = "Retrieves a specific user by their email address")
    @APIResponse(responseCode = "200", description = "User found",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = UserDTO.class)))
    @APIResponse(responseCode = "404", description = "User not found")
    public Response getUserByEmail(
        @Parameter(description = "User email", required = true) @PathParam("email") String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(userOpt.get())).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Create new user", description = "Creates a new user in the system")
    @APIResponse(responseCode = "201", description = "User created successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = UserDTO.class)))
    @APIResponse(responseCode = "400", description = "Invalid input data")
    @APIResponse(responseCode = "409", description = "User with this email already exists")
    public Response createUser(
        @Parameter(description = "User data", required = true) UserDTO userDTO) {
        try {
            if (userDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("User data cannot be null")
                        .build();
            }

            if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Email is required")
                        .build();
            }

            Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
            if (existingUser.isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("User with this email already exists")
                        .build();
            }

            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());

            userRepository.persist(user);
            userRepository.flush();

            return Response.status(Response.Status.CREATED)
                    .entity(convertToDTO(user))
                    .build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating user: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @APIResponse(responseCode = "200", description = "User updated successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = UserDTO.class)))
    @APIResponse(responseCode = "404", description = "User not found")
    public Response updateUser(
        @Parameter(description = "User ID", required = true) @PathParam("id") Long id,
        @Parameter(description = "Updated user data", required = true) UserDTO userDTO) {
        User user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        userRepository.persist(user);
        return Response.ok(convertToDTO(user)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete user", description = "Deletes a user from the system")
    @APIResponse(responseCode = "204", description = "User deleted successfully")
    @APIResponse(responseCode = "404", description = "User not found")
    public Response deleteUser(
        @Parameter(description = "User ID", required = true) @PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userRepository.delete(user);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/tickets")
    @Transactional
    @Operation(summary = "Get user tickets", description = "Retrieves all tickets for a specific user")
    @APIResponse(responseCode = "200", description = "Tickets retrieved successfully",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = TicketDTO.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "404", description = "User not found")
    public Response getUserTickets(
        @Parameter(description = "User ID", required = true) @PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<TicketDTO> tickets = user.getTickets().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Response.ok(tickets).build();
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        return new TicketDTO(
            ticket.getId(),
            ticket.getTicketType(),
            ticket.getDate(),
            ticket.getValidFrom(),
            ticket.getValidUntil(),
            convertToDTO(ticket.getUser())
        );
    }
} 