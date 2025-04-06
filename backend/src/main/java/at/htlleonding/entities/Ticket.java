package at.htlleonding.entities;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ticket_typ", discriminatorType = DiscriminatorType.STRING)
public abstract class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double preis;
}

@Entity
@DiscriminatorValue("Tageskarte")
class Tageskarte extends Ticket {}

@Entity
@DiscriminatorValue("Wochenkarte")
class Wochenkarte extends Ticket {}

@Entity
@DiscriminatorValue("Saisonkarte")
class Saisonkarte extends Ticket {}