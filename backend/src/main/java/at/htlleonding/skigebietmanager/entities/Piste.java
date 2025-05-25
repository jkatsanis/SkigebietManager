package at.htlleonding.skigebietmanager.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pisten")
public class Piste {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "piste_seq")
    @SequenceGenerator(name = "piste_seq", sequenceName = "piste_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String schwierigkeitsgrad;

    @Column(nullable = false)
    private Double laenge;

    @ManyToOne
    @JoinColumn(name = "ski_lift_id", nullable = false)
    private SkiLift skiLift;

    // Constructors
    public Piste() {
    }

    public Piste(String name, String schwierigkeitsgrad, Double laenge, SkiLift skiLift) {
        this.name = name;
        this.schwierigkeitsgrad = schwierigkeitsgrad;
        this.laenge = laenge;
        this.skiLift = skiLift;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchwierigkeitsgrad() {
        return schwierigkeitsgrad;
    }

    public void setSchwierigkeitsgrad(String schwierigkeitsgrad) {
        this.schwierigkeitsgrad = schwierigkeitsgrad;
    }

    public Double getLaenge() {
        return laenge;
    }

    public void setLaenge(Double laenge) {
        this.laenge = laenge;
    }

    public SkiLift getSkiLift() {
        return skiLift;
    }

    public void setSkiLift(SkiLift skiLift) {
        this.skiLift = skiLift;
    }
}