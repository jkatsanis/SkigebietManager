package at.htlleonding.entities;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Skilift {
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