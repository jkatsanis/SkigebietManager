package at.htlleonding.skigebietmanager.repositories;

import at.htlleonding.skigebietmanager.entities.Piste;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class PisteRepository implements PanacheRepository<Piste> {
    public List<Piste> findBySkiLiftId(Long skiLiftId) {
        return find("skiLift.id", skiLiftId).list();
    }

    public Map<String, Long> countByDifficulty() {
        List<Object[]> results = getEntityManager().createQuery(
            "SELECT p.schwierigkeitsgrad, COUNT(p) as count " +
            "FROM Piste p " +
            "GROUP BY p.schwierigkeitsgrad"
        ).getResultList();

        Map<String, Long> difficultyCounts = new HashMap<>();
        for (Object[] result : results) {
            String difficulty = (String) result[0];
            Long count = (Long) result[1];
            if (difficulty != null) {
                difficultyCounts.put(difficulty, count);
            }
        }
        return difficultyCounts;
    }

    public List<Map<String, Object>> getPistenWithSkiLiftInfo() {
        return getEntityManager().createQuery("""
            SELECT 
                p.id, p.name, p.schwierigkeitsgrad, p.laenge,
                s.id, s.name, s.kapazitaet
            FROM Piste p
            LEFT JOIN p.skiLift s
            """, Object[].class)
            .getResultList()
            .stream()
            .map(result -> {
                Map<String, Object> pisteInfo = new HashMap<>();
                pisteInfo.put("id", result[0]);
                pisteInfo.put("name", result[1]);
                pisteInfo.put("schwierigkeitsgrad", result[2]);
                pisteInfo.put("laenge", result[3]);
                pisteInfo.put("skiLiftId", result[4]);
                pisteInfo.put("skiLiftName", result[5]);
                pisteInfo.put("kapazitaet", result[6]);
                return pisteInfo;
            })
            .collect(Collectors.toList());
    }
}

