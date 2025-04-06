package at.htlleonding.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Piste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String schwierigkeitsgrad;
    private double laenge;

    @ManyToOne
    @JoinColumn(name = "skilift_id")
    private Skilift skiLift;

}