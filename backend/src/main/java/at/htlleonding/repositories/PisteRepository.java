package at.htlleonding.repositories;

import at.htlleonding.entities.Piste;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class PisteRepository implements PanacheRepository<Piste> {
}

