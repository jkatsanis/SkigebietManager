package at.htlleonding.skigebietmanager.services;

import at.htlleonding.skigebietmanager.entities.SkiLift;
import at.htlleonding.skigebietmanager.repositories.SkiLiftRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class SkiLiftService {
    @Inject
    SkiLiftRepository skiLiftRepository;

    public List<SkiLift> getAllSkiLifts() {
        return skiLiftRepository.listAll();
    }

    public SkiLift getSkiLiftById(Long id) {
        return skiLiftRepository.findById(id);
    }

    @Transactional
    public SkiLift createSkiLift(SkiLift skiLift) {
        skiLiftRepository.persist(skiLift);
        return skiLift;
    }

    @Transactional
    public SkiLift updateSkiLift(Long id, SkiLift skiLift) {
        SkiLift existingSkiLift = skiLiftRepository.findById(id);
        if (existingSkiLift != null) {
            existingSkiLift.setName(skiLift.getName());
            existingSkiLift.setTyp(skiLift.getTyp());
            existingSkiLift.setKapazitaet(skiLift.getKapazitaet());
            skiLiftRepository.persist(existingSkiLift);
            return existingSkiLift;
        }
        return null;
    }

    @Transactional
    public boolean deleteSkiLift(Long id) {
        return skiLiftRepository.deleteById(id);
    }
} 