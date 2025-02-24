package org.example.entities;
import jakarta.persistence.*;

import java.util.List;

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

    // Getter & Setter
}


@Entity
@DiscriminatorValue("Gast")
class Gast extends Benutzer {}

@Entity
@DiscriminatorValue("Mitarbeiter")
class Mitarbeiter extends Benutzer {}

@Entity
@DiscriminatorValue("Admin")
class Admin extends Benutzer {}
