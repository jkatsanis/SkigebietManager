package at.htlleonding.skigebietmanager.resources;

import at.htlleonding.skigebietmanager.repositories.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/api/statistics")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsResource {
    @Inject
    TicketRepository ticketRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    PisteRepository pisteRepository;

    @Inject
    SkiLiftRepository skiLiftRepository;

    @GET
    @Transactional
    public Response getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // Basic counts
        statistics.put("totalUsers", userRepository.count());
        statistics.put("totalTickets", ticketRepository.count());
        statistics.put("totalPisten", pisteRepository.count());
        statistics.put("totalSkiLifts", skiLiftRepository.count());

        // Ticket statistics
        statistics.put("ticketsByType", ticketRepository.countByTicketType());
        statistics.put("ticketsPerUser", ticketRepository.getTicketsPerUser());

        // Piste statistics
        statistics.put("pistenByDifficulty", pisteRepository.countByDifficulty());
        statistics.put("pistenWithSkiLiftInfo", pisteRepository.getPistenWithSkiLiftInfo());

        // Ski lift statistics
        statistics.put("skiLiftCapacity", skiLiftRepository.getTotalCapacity());
        statistics.put("skiLiftsWithPisteInfo", skiLiftRepository.getSkiLiftsWithPisteCount());

        return Response.ok(statistics).build();
    }
} 