package at.htlleonding.skigebietmanager;

import at.htlleonding.skigebietmanager.entities.SkiLift;
import at.htlleonding.skigebietmanager.repositories.SkiLiftRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SkiLiftTest {

    @Inject
    SkiLiftRepository skiLiftRepository;

    @Test
    @Transactional
    public void testCreateAndFindSkiLift() {
        // Create a new ski lift
        SkiLift skiLift = new SkiLift("Test Lift", "Sessellift", 1000);
        skiLiftRepository.persist(skiLift);

        // Find the ski lift by ID
        SkiLift foundSkiLift = skiLiftRepository.findById(skiLift.getId());
        assertNotNull(foundSkiLift);
        assertEquals("Test Lift", foundSkiLift.getName());
        assertEquals("Sessellift", foundSkiLift.getTyp());
        assertEquals(1000, foundSkiLift.getKapazitaet());

        // Find the ski lift by name
        SkiLift skiLiftByName = skiLiftRepository.findByName("Test Lift");
        assertNotNull(skiLiftByName);
        assertEquals(skiLift.getId(), skiLiftByName.getId());
    }

    @Test
    @Transactional
    public void testUpdateSkiLift() {
        // Create a new ski lift
        SkiLift skiLift = new SkiLift("Original Lift", "Schlepplift", 800);
        skiLiftRepository.persist(skiLift);

        // Update the ski lift
        skiLift.setName("Updated Lift");
        skiLift.setTyp("Sessellift");
        skiLift.setKapazitaet(1200);
        skiLiftRepository.persist(skiLift);

        // Find and verify the updated ski lift
        SkiLift updatedSkiLift = skiLiftRepository.findById(skiLift.getId());
        assertNotNull(updatedSkiLift);
        assertEquals("Updated Lift", updatedSkiLift.getName());
        assertEquals("Sessellift", updatedSkiLift.getTyp());
        assertEquals(1200, updatedSkiLift.getKapazitaet());
    }

    @Test
    @Transactional
    public void testDeleteSkiLift() {
        // Create a new ski lift
        SkiLift skiLift = new SkiLift("Delete Test", "Schlepplift", 800);
        skiLiftRepository.persist(skiLift);
        Long skiLiftId = skiLift.getId();

        // Delete the ski lift
        boolean deleted = skiLiftRepository.deleteById(skiLiftId);
        assertTrue(deleted);

        // Verify the ski lift is deleted
        SkiLift deletedSkiLift = skiLiftRepository.findById(skiLiftId);
        assertNull(deletedSkiLift);
    }

    @Test
    @Transactional
    public void testListAllSkiLifts() {
        // Create multiple ski lifts
        SkiLift skiLift1 = new SkiLift("Lift 1", "Sessellift", 1000);
        SkiLift skiLift2 = new SkiLift("Lift 2", "Schlepplift", 800);
        skiLiftRepository.persist(skiLift1);
        skiLiftRepository.persist(skiLift2);

        // Get all ski lifts
        List<SkiLift> skiLifts = skiLiftRepository.listAll();
        assertTrue(skiLifts.size() >= 2);
        assertTrue(skiLifts.stream().anyMatch(l -> l.getName().equals("Lift 1")));
        assertTrue(skiLifts.stream().anyMatch(l -> l.getName().equals("Lift 2")));
    }
} 