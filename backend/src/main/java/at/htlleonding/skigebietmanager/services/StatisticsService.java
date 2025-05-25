package at.htlleonding.skigebietmanager.services;

import at.htlleonding.skigebietmanager.entities.*;
import at.htlleonding.skigebietmanager.repositories.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class StatisticsService {
    @Inject
    UserRepository userRepository;

    @Inject
    TicketRepository ticketRepository;

    @Inject
    PisteRepository pisteRepository;

    @Inject
    SkiLiftRepository skiLiftRepository;

    public Map<String, Object> getAllStatistics() {
        List<User> users = userRepository.listAll();
        List<Ticket> tickets = ticketRepository.listAll();
        List<Piste> pisten = pisteRepository.listAll();
        List<SkiLift> skiLifts = skiLiftRepository.listAll();

        // Calculate tickets by type
        Map<String, Long> ticketsByType = tickets.stream()
            .collect(Collectors.groupingBy(
                Ticket::getTicketType,
                Collectors.counting()
            ));

        // Calculate tickets per user
        List<Map<String, Object>> ticketsPerUser = users.stream()
            .map(user -> {
                List<Ticket> userTickets = tickets.stream()
                    .filter(t -> t.getUser().getId().equals(user.getId()))
                    .collect(Collectors.toList());

                Map<String, Long> ticketTypes = userTickets.stream()
                    .collect(Collectors.groupingBy(
                        Ticket::getTicketType,
                        Collectors.counting()
                    ));

                return Map.of(
                    "userId", user.getId(),
                    "userName", user.getName(),
                    "totalTickets", userTickets.size(),
                    "ticketTypes", ticketTypes
                );
            })
            .collect(Collectors.toList());

        // Calculate total ski lift capacity
        int totalSkiLiftCapacity = skiLifts.stream()
            .mapToInt(SkiLift::getKapazitaet)
            .sum();

        return Map.of(
            "totalUsers", users.size(),
            "totalTickets", tickets.size(),
            "totalPisten", pisten.size(),
            "totalSkiLifts", skiLifts.size(),
            "skiLiftCapacity", totalSkiLiftCapacity,
            "ticketsByType", ticketsByType,
            "ticketsPerUser", ticketsPerUser
        );
    }
} 