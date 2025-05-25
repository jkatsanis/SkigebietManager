package at.htlleonding.repositories;

import at.htlleonding.entities.Benutzer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class BenutzerRepository implements PanacheRepository<Benutzer> {
    public Benutzer findByEmail(String email) {
        return find("email", email).firstResult();
    }

    // Anzahl der Tickets pro Benutzer
    public List<Object[]> getTicketCountPerUser() {
        return getEntityManager().createQuery(
            "SELECT b.id, b.name, COUNT(t) as ticketCount " +
            "FROM Benutzer b " +
            "LEFT JOIN b.tickets t " +
            "GROUP BY b.id, b.name " +
            "ORDER BY ticketCount DESC"
        ).getResultList();
    }

} 