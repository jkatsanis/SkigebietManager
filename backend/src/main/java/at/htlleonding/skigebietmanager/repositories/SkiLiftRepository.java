package at.htlleonding.skigebietmanager.repositories;

import at.htlleonding.skigebietmanager.entities.SkiLift;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class SkiLiftRepository implements PanacheRepository<SkiLift> {
    // Basic CRUD operations are automatically provided by PanacheRepository
    public SkiLift findByName(String name) {
        return find("name", name).firstResult();
    }

    public int getTotalCapacity() {
        Object result = getEntityManager().createQuery(
            "SELECT COALESCE(SUM(s.kapazitaet), 0) FROM SkiLift s"
        ).getSingleResult();
        return result != null ? ((Number) result).intValue() : 0;
    }

    public List<Map<String, Object>> getSkiLiftsWithPisteCount() {
        return getEntityManager().createQuery("""
            SELECT 
                s.id, s.name, s.typ, s.kapazitaet,
                COUNT(p) as pisteCount,
                COALESCE(AVG(p.laenge), 0) as avgPisteLength
            FROM SkiLift s
            LEFT JOIN s.pisten p
            GROUP BY s.id, s.name, s.typ, s.kapazitaet
            """, Object[].class)
            .getResultList()
            .stream()
            .map(result -> {
                Map<String, Object> skiLiftInfo = new HashMap<>();
                skiLiftInfo.put("id", result[0]);
                skiLiftInfo.put("name", result[1]);
                skiLiftInfo.put("typ", result[2]);
                skiLiftInfo.put("kapazitaet", result[3]);
                skiLiftInfo.put("pisteCount", result[4]);
                skiLiftInfo.put("avgPisteLength", result[5]);
                return skiLiftInfo;
            })
            .collect(Collectors.toList());
    }
}
