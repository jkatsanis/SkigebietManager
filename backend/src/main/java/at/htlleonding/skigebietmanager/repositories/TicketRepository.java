package at.htlleonding.skigebietmanager.repositories;

import at.htlleonding.skigebietmanager.entities.Ticket;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
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
        return listAll().stream()
            .collect(Collectors.groupingBy(
                Ticket::getTicketType,
                Collectors.counting()
            ));
    }
} 