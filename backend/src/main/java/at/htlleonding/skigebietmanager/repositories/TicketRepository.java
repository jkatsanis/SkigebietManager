package at.htlleonding.skigebietmanager.repositories;

import at.htlleonding.skigebietmanager.entities.Ticket;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class TicketRepository implements PanacheRepository<Ticket> {

    // Gesamteinnahmen pro Tickettyp
    public List<Object[]> getTotalRevenueByTicketType() {
        return getEntityManager().createQuery(
            "SELECT TYPE(t), SUM(t.preis) as totalRevenue " +
            "FROM Ticket t " +
            "GROUP BY TYPE(t) " +
            "ORDER BY totalRevenue DESC"
        ).getResultList();
    }

    // Anzahl der verkauften Tickets pro Monat
    public List<Object[]> getTicketSalesByMonth() {
        return getEntityManager().createQuery(
            "SELECT MONTH(t.createdAt), COUNT(t) as ticketCount " +
            "FROM Ticket t " +
            "GROUP BY MONTH(t.createdAt) " +
            "ORDER BY MONTH(t.createdAt)"
        ).getResultList();
    }

    public List<Ticket> findByUserId(Long userId) {
        return find("user.id", userId).list();
    }

    public Map<String, Long> countByTicketType() {
        List<Object[]> results = getEntityManager().createQuery(
            "SELECT t.ticketType, COUNT(t) as count " +
            "FROM Ticket t " +
            "GROUP BY t.ticketType"
        ).getResultList();

        Map<String, Long> ticketCounts = new HashMap<>();
        for (Object[] result : results) {
            String ticketType = (String) result[0];
            Long count = (Long) result[1];
            if (ticketType != null) {
                ticketCounts.put(ticketType, count);
            }
        }
        return ticketCounts;
    }

    public List<Map<String, Object>> getTicketsPerUser() {
        return getEntityManager().createQuery("""
            SELECT 
                u.id as userId, 
                u.name as userName, 
                COUNT(t) as totalTickets,
                SUM(CASE WHEN t.ticketType = 'Ganztages' THEN 1 ELSE 0 END) as ganztagesCount,
                SUM(CASE WHEN t.ticketType = 'Halbtages' THEN 1 ELSE 0 END) as halbtagesCount
            FROM User u
            LEFT JOIN u.tickets t
            GROUP BY u.id, u.name
            """, Object[].class)
            .getResultList()
            .stream()
            .map(result -> {
                Map<String, Object> userStats = new HashMap<>();
                userStats.put("userId", result[0]);
                userStats.put("userName", result[1]);
                userStats.put("totalTickets", result[2]);
                userStats.put("ganztagesCount", result[3]);
                userStats.put("halbtagesCount", result[4]);
                return userStats;
            })
            .collect(Collectors.toList());
    }
} 