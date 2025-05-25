package at.htlleonding.skigebietmanager.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ski_lifts")
public class SkiLift {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ski_lift_seq")
    @SequenceGenerator(name = "ski_lift_seq", sequenceName = "ski_lift_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String typ;

    @Column(nullable = false)
    private Integer kapazitaet;

    @OneToMany(mappedBy = "skiLift", cascade = CascadeType.ALL)
    private List<Piste> pisten;

    // Constructors
    public SkiLift() {
    }

    public SkiLift(String name, String typ, Integer kapazitaet) {
        this.name = name;
        this.typ = typ;
        this.kapazitaet = kapazitaet;
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

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public Integer getKapazitaet() {
        return kapazitaet;
    }

    public void setKapazitaet(Integer kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    public List<Piste> getPisten() {
        return pisten;
    }

    public void setPisten(List<Piste> pisten) {
        this.pisten = pisten;
    }
}