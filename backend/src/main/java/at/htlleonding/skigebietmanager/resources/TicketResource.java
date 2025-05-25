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

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketResource {

    @Inject
    TicketRepository ticketRepository;

    @Inject
    UserRepository userRepository;

    @GET
    @Transactional
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.listAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getTicketById(@PathParam("id") Long id) {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(ticket)).build();
    }

    @GET
    @Path("/user/{userId}")
    @Transactional
    public Response getTicketsByUser(@PathParam("userId") Long userId) {
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
    public Response createTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
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
        return Response.status(Response.Status.CREATED).entity(convertToDTO(ticket)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateTicket(@PathParam("id") Long id, TicketDTO ticketDTO) {
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
    public Response deleteTicket(@PathParam("id") Long id) {
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