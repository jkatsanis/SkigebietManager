package at.htlleonding.skigebietmanager.dto;

public class SkiLiftDTO {
    private Long id;
    private String name;
    private String typ;
    private Integer kapazitaet;

    // Default constructor
    public SkiLiftDTO() {
    }

    // Constructor with fields
    public SkiLiftDTO(Long id, String name, String typ, Integer kapazitaet) {
        this.id = id;
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
} 