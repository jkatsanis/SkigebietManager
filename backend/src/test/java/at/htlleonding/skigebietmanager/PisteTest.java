package at.htlleonding.skigebietmanager;

import at.htlleonding.skigebietmanager.entities.Piste;
import at.htlleonding.skigebietmanager.entities.SkiLift;
import at.htlleonding.skigebietmanager.repositories.PisteRepository;
import at.htlleonding.skigebietmanager.repositories.SkiLiftRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PisteTest {

    @Inject
    PisteRepository pisteRepository;

    @Inject
    SkiLiftRepository skiLiftRepository;

    @Test
    @Transactional
    public void testCreateAndFindPiste() {
        // Create a new ski lift first
        String uniqueName = "Test Lift " + UUID.randomUUID();
        SkiLift skiLift = new SkiLift(uniqueName, "Sessellift", 1000);
        skiLiftRepository.persist(skiLift);

        // Create a new piste
        String uniquePisteName = "Test Piste " + UUID.randomUUID();
        Piste piste = new Piste(uniquePisteName, "Mittel", 2000.0, skiLift);
        pisteRepository.persist(piste);

        // Find the piste by ID
        Piste foundPiste = pisteRepository.findById(piste.getId());
        assertNotNull(foundPiste);
        assertEquals(uniquePisteName, foundPiste.getName());
        assertEquals(2000.0, foundPiste.getLaenge());
        assertEquals("Mittel", foundPiste.getSchwierigkeitsgrad());
        assertEquals(skiLift.getId(), foundPiste.getSkiLift().getId());
    }

    @Test
    @Transactional
    public void testUpdatePiste() {
        // Create a new ski lift
        String uniqueName = "Test Lift " + UUID.randomUUID();
        SkiLift skiLift = new SkiLift(uniqueName, "Sessellift", 1000);
        skiLiftRepository.persist(skiLift);

        // Create a new piste
        String uniquePisteName = "Test Piste " + UUID.randomUUID();
        Piste piste = new Piste(uniquePisteName, "Mittel", 2000.0, skiLift);
        pisteRepository.persist(piste);

        // Update the piste
        String updatedName = "Updated Piste " + UUID.randomUUID();
        piste.setName(updatedName);
        piste.setLaenge(2500.0);
        piste.setSchwierigkeitsgrad("Schwer");
        pisteRepository.persist(piste);

        // Find and verify the updated piste
        Piste updatedPiste = pisteRepository.findById(piste.getId());
        assertNotNull(updatedPiste);
        assertEquals(updatedName, updatedPiste.getName());
        assertEquals(2500.0, updatedPiste.getLaenge());
        assertEquals("Schwer", updatedPiste.getSchwierigkeitsgrad());
    }

    @Test
    @Transactional
    public void testDeletePiste() {
        // Create a new ski lift
        String uniqueName = "Test Lift " + UUID.randomUUID();
        SkiLift skiLift = new SkiLift(uniqueName, "Sessellift", 1000);
        skiLiftRepository.persist(skiLift);

        // Create a new piste
        String uniquePisteName = "Test Piste " + UUID.randomUUID();
        Piste piste = new Piste(uniquePisteName, "Mittel", 2000.0, skiLift);
        pisteRepository.persist(piste);
        Long pisteId = piste.getId();

        // Delete the piste
        boolean deleted = pisteRepository.deleteById(pisteId);
        assertTrue(deleted);

        // Verify the piste is deleted
        Piste deletedPiste = pisteRepository.findById(pisteId);
        assertNull(deletedPiste);
    }

    @Test
    @Transactional
    public void testFindPistenBySkiLiftId() {
        // Create a new ski lift
        String uniqueName = "Test Lift " + UUID.randomUUID();
        SkiLift skiLift = new SkiLift(uniqueName, "Sessellift", 1000);
        skiLiftRepository.persist(skiLift);

        // Create multiple pistes for the same ski lift
        String uniquePisteName1 = "Test Piste 1 " + UUID.randomUUID();
        String uniquePisteName2 = "Test Piste 2 " + UUID.randomUUID();
        Piste piste1 = new Piste(uniquePisteName1, "Mittel", 2000.0, skiLift);
        Piste piste2 = new Piste(uniquePisteName2, "Schwer", 3000.0, skiLift);
        pisteRepository.persist(piste1);
        pisteRepository.persist(piste2);

        // Get pistes by ski lift ID
        List<Piste> pistes = pisteRepository.findBySkiLiftId(skiLift.getId());
        assertEquals(2, pistes.size());
        assertTrue(pistes.stream().anyMatch(p -> p.getName().equals(uniquePisteName1)));
        assertTrue(pistes.stream().anyMatch(p -> p.getName().equals(uniquePisteName2)));
    }
} 