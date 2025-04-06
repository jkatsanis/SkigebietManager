package at.htlleonding.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ticket_typ", discriminatorType = DiscriminatorType.STRING)
public abstract class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double preis;
}
@Getter
@Setter
@Entity
@DiscriminatorValue("Tageskarte")
class Tageskarte extends Ticket {}
@Getter
@Setter
@Entity
@DiscriminatorValue("Wochenkarte")
class Wochenkarte extends Ticket {}
@Getter
@Setter
@Entity
@DiscriminatorValue("Saisonkarte")
class Saisonkarte extends Ticket {}