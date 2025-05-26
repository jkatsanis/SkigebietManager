package at.htlleonding.skigebietmanager.dto;

public class PisteDTO {
    private Long id;
    private String name;
    private String schwierigkeitsgrad;
    private Double laenge;
    private SkiLiftDTO skiLift;

    // Default constructor
    public PisteDTO() {
    }

    // Constructor with fields
    public PisteDTO(Long id, String name, String schwierigkeitsgrad, Double laenge, SkiLiftDTO skiLift) {
        this.id = id;
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

    public SkiLiftDTO getSkiLift() {
        return skiLift;
    }

    public void setSkiLift(SkiLiftDTO skiLift) {
        this.skiLift = skiLift;
    }
} 