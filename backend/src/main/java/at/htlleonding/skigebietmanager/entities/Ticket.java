package at.htlleonding.skigebietmanager.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    @SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String ticketType;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime validFrom;

    @Column(nullable = false)
    private LocalTime validUntil;

    // Constructors
    public Ticket() {
    }

    public Ticket(User user, String ticketType, LocalDate date, LocalTime validFrom, LocalTime validUntil) {
        this.user = user;
        this.ticketType = ticketType;
        this.date = date;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalTime validUntil) {
        this.validUntil = validUntil;
    }
}