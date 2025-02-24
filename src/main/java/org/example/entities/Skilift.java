package org.example.entities;
import jakarta.persistence.*;
import java.util.List;

@Entity
class SkiLift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String typ; // Sessellift, Schlepplift, Gondel
    private int kapazität;

    @OneToMany(mappedBy = "skiLift")
    private List<Piste> pisten;

    // Getter & Setter
}