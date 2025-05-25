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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepository userRepository;

    @Inject
    TicketRepository ticketRepository;

    @GET
    @Transactional
    public List<UserDTO> getAllUsers() {
        return userRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getUserById(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(user)).build();
    }

    @GET
    @Path("/email/{email}")
    @Transactional
    public Response getUserByEmail(@PathParam("email") String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(userOpt.get())).build();
    }

    @POST
    @Transactional
    public Response createUser(UserDTO userDTO) {
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

            // Check if user with this email already exists
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
            userRepository.flush(); // Ensure the entity is persisted

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
    public Response updateUser(@PathParam("id") Long id, UserDTO userDTO) {
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
    public Response deleteUser(@PathParam("id") Long id) {
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
    public Response getUserTickets(@PathParam("id") Long id) {
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