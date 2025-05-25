package at.htlleonding.skigebietmanager;

import at.htlleonding.skigebietmanager.entities.Ticket;
import at.htlleonding.skigebietmanager.entities.User;
import at.htlleonding.skigebietmanager.repositories.TicketRepository;
import at.htlleonding.skigebietmanager.repositories.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TicketTest {

    @Inject
    TicketRepository ticketRepository;

    @Inject
    UserRepository userRepository;

    @Test
    @Transactional
    public void testCreateAndFindTicket() {
        // Create a user with unique email
        User user = new User("Test User", UUID.randomUUID() + "@example.com");
        userRepository.persist(user);

        // Create a new ticket
        Ticket ticket = new Ticket(
            user,
            "Ganztages",
            LocalDate.of(2024, 3, 22),
            LocalTime.of(9, 0),
            LocalTime.of(17, 0)
        );
        ticketRepository.persist(ticket);

        // Find the ticket
        Ticket foundTicket = ticketRepository.findById(ticket.getId());
        assertNotNull(foundTicket);
        assertEquals("Ganztages", foundTicket.getTicketType());
        assertEquals(LocalDate.of(2024, 3, 22), foundTicket.getDate());
        assertEquals(LocalTime.of(9, 0), foundTicket.getValidFrom());
        assertEquals(LocalTime.of(17, 0), foundTicket.getValidUntil());
        assertEquals(user.getId(), foundTicket.getUser().getId());
    }

    @Test
    @Transactional
    public void testUpdateTicket() {
        // Create a user with unique email
        User user = new User("Test User", UUID.randomUUID() + "@example.com");
        userRepository.persist(user);

        // Create a ticket
        Ticket ticket = new Ticket(
            user,
            "Ganztages",
            LocalDate.of(2024, 3, 22),
            LocalTime.of(9, 0),
            LocalTime.of(17, 0)
        );
        ticketRepository.persist(ticket);

        // Update the ticket
        ticket.setTicketType("Halbtages");
        ticket.setValidFrom(LocalTime.of(13, 0));
        ticket.setValidUntil(LocalTime.of(18, 0));
        ticketRepository.persist(ticket);

        // Find and verify the updated ticket
        Ticket updatedTicket = ticketRepository.findById(ticket.getId());
        assertNotNull(updatedTicket);
        assertEquals("Halbtages", updatedTicket.getTicketType());
        assertEquals(LocalTime.of(13, 0), updatedTicket.getValidFrom());
        assertEquals(LocalTime.of(18, 0), updatedTicket.getValidUntil());
    }

    @Test
    @Transactional
    public void testDeleteTicket() {
        // Create a user with unique email
        User user = new User("Test User", UUID.randomUUID() + "@example.com");
        userRepository.persist(user);

        // Create a ticket
        Ticket ticket = new Ticket(
            user,
            "Ganztages",
            LocalDate.of(2024, 3, 22),
            LocalTime.of(9, 0),
            LocalTime.of(17, 0)
        );
        ticketRepository.persist(ticket);

        // Delete the ticket
        ticketRepository.delete(ticket);

        // Verify the ticket is deleted
        Ticket deletedTicket = ticketRepository.findById(ticket.getId());
        assertNull(deletedTicket);
    }

    @Test
    @Transactional
    public void testFindTicketsByUserId() {
        // Create a user with unique email
        User user = new User("Test User", UUID.randomUUID() + "@example.com");
        userRepository.persist(user);

        // Create multiple tickets for the user
        Ticket ticket1 = new Ticket(
            user,
            "Ganztages",
            LocalDate.of(2024, 3, 22),
            LocalTime.of(9, 0),
            LocalTime.of(17, 0)
        );
        Ticket ticket2 = new Ticket(
            user,
            "Halbtages",
            LocalDate.of(2024, 3, 23),
            LocalTime.of(13, 0),
            LocalTime.of(18, 0)
        );
        ticketRepository.persist(ticket1);
        ticketRepository.persist(ticket2);

        // Find tickets by user ID
        List<Ticket> userTickets = ticketRepository.findByUserId(user.getId());
        assertEquals(2, userTickets.size());
        assertTrue(userTickets.stream().anyMatch(t -> t.getTicketType().equals("Ganztages")));
        assertTrue(userTickets.stream().anyMatch(t -> t.getTicketType().equals("Halbtages")));
    }
} 