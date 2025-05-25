package at.htlleonding.skigebietmanager.ressource;

import at.htlleonding.skigebietmanager.entities.Ticket;
import at.htlleonding.skigebietmanager.repositories.TicketRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Path("/api/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketResource {
    @Inject
    TicketRepository ticketRepository;

    @GET
    public List<Ticket> getAllTickets() {
        return ticketRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getTicketById(@PathParam("id") Long id) {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket != null) {
            return Response.ok(ticket).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/user/{userId}")
    public List<Ticket> getTicketsByUserId(@PathParam("userId") Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    @POST
    public Response createTicket(Ticket ticket) {
        // Set default values based on ticket type
        if (ticket.getDate() == null) {
            ticket.setDate(LocalDate.now());
        }
        if (ticket.getValidFrom() == null) {
            ticket.setValidFrom(ticket.getTicketType().equals("Ganztages") ? 
                LocalTime.of(8, 0) : LocalTime.of(12, 0));
        }
        if (ticket.getValidUntil() == null) {
            ticket.setValidUntil(LocalTime.of(16, 0));
        }

        ticketRepository.persist(ticket);
        return Response.status(Response.Status.CREATED).entity(ticket).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTicket(@PathParam("id") Long id, Ticket ticket) {
        Ticket existingTicket = ticketRepository.findById(id);
        if (existingTicket != null) {
            existingTicket.setTicketType(ticket.getTicketType());
            existingTicket.setDate(ticket.getDate());
            existingTicket.setValidFrom(ticket.getValidFrom());
            existingTicket.setValidUntil(ticket.getValidUntil());
            existingTicket.setUser(ticket.getUser());
            ticketRepository.persist(existingTicket);
            return Response.ok(existingTicket).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTicket(@PathParam("id") Long id) {
        boolean deleted = ticketRepository.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
} 