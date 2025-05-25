package at.htlleonding.skigebietmanager.repositories;

import at.htlleonding.skigebietmanager.entities.SkiLift;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SkiLiftRepository implements PanacheRepository<SkiLift> {
    // Basic CRUD operations are automatically provided by PanacheRepository
    public SkiLift findByName(String name) {
        return find("name", name).firstResult();
    }
}
