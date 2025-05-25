package at.htlleonding.skigebietmanager.services;

import at.htlleonding.skigebietmanager.entities.Ticket;
import at.htlleonding.skigebietmanager.entities.User;
import at.htlleonding.skigebietmanager.repositories.TicketRepository;
import at.htlleonding.skigebietmanager.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class TicketService {
    @Inject
    TicketRepository ticketRepository;

    @Inject
    UserRepository userRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.listAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Transactional
    public Ticket createTicket(Ticket ticket) {
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
        return ticket;
    }

    @Transactional
    public Ticket updateTicket(Long id, Ticket ticket) {
        Ticket existingTicket = ticketRepository.findById(id);
        if (existingTicket != null) {
            existingTicket.setTicketType(ticket.getTicketType());
            existingTicket.setDate(ticket.getDate());
            existingTicket.setValidFrom(ticket.getValidFrom());
            existingTicket.setValidUntil(ticket.getValidUntil());
            existingTicket.setUser(ticket.getUser());
            ticketRepository.persist(existingTicket);
            return existingTicket;
        }
        return null;
    }

    @Transactional
    public boolean deleteTicket(Long id) {
        return ticketRepository.deleteById(id);
    }
} 