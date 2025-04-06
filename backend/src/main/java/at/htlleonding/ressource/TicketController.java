package at.htlleonding.ressource;

import at.htlleonding.entities.Ticket;
import at.htlleonding.repositories.TicketRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/ticket")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TicketController {

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
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ticket).build();
    }

    @POST
    @Transactional
    public Response addTicket(Ticket ticket) {
        ticketRepository.persist(ticket);
        return Response.status(Response.Status.CREATED).entity(ticket).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateTicket(@PathParam("id") Long id, Ticket updatedTicket) {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        ticket.setPreis(updatedTicket.getPreis());
        
        ticketRepository.persist(ticket);
        return Response.ok(ticket).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTicket(@PathParam("id") Long id) {
        boolean deleted = ticketRepository.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
} 