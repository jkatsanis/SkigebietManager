package at.htlleonding.entities;
import at.htlleonding.skigebietmanager.entities.Ticket;
import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public abstract class Benutzer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @ManyToMany
    @JoinTable(name = "benutzer_ticket",
            joinColumns = @JoinColumn(name = "benutzer_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    private List<Ticket> tickets;

}

@Getter
@Setter
@Entity
@DiscriminatorValue("Gast")
class Gast extends Benutzer {
    
}
@Getter
@Setter
@Entity
@DiscriminatorValue("Mitarbeiter")
class Mitarbeiter extends Benutzer {}
@Getter
@Setter
@Entity
@DiscriminatorValue("Admin")
class Admin extends Benutzer {}