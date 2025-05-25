package at.htlleonding.skigebietmanager.repositories;

import at.htlleonding.skigebietmanager.entities.Piste;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PisteRepository implements PanacheRepository<Piste> {
    public List<Piste> findBySkiLiftId(Long skiLiftId) {
        return find("skiLift.id", skiLiftId).list();
    }
}

