package at.htlleonding.skigebietmanager.services;

import at.htlleonding.skigebietmanager.entities.Piste;
import at.htlleonding.skigebietmanager.repositories.PisteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PisteService {
    @Inject
    PisteRepository pisteRepository;

    public List<Piste> getAllPisten() {
        return pisteRepository.listAll();
    }

    public Piste getPisteById(Long id) {
        return pisteRepository.findById(id);
    }

    public List<Piste> getPistenBySkiLiftId(Long skiLiftId) {
        return pisteRepository.findBySkiLiftId(skiLiftId);
    }

    @Transactional
    public Piste createPiste(Piste piste) {
        pisteRepository.persist(piste);
        return piste;
    }

    @Transactional
    public Piste updatePiste(Long id, Piste piste) {
        Piste existingPiste = pisteRepository.findById(id);
        if (existingPiste != null) {
            existingPiste.setName(piste.getName());
            existingPiste.setSchwierigkeitsgrad(piste.getSchwierigkeitsgrad());
            existingPiste.setLaenge(piste.getLaenge());
            existingPiste.setSkiLift(piste.getSkiLift());
            pisteRepository.persist(existingPiste);
            return existingPiste;
        }
        return null;
    }

    @Transactional
    public boolean deletePiste(Long id) {
        return pisteRepository.deleteById(id);
    }
} 