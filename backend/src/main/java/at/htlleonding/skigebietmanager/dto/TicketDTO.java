package at.htlleonding.skigebietmanager.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Ticket data transfer object")
public class TicketDTO {
    @Schema(description = "Unique identifier of the ticket", example = "1")
    private Long id;

    @Schema(description = "Type of the ticket", example = "Ganztages", required = true)
    private String ticketType;

    @Schema(description = "Date of the ticket", example = "2024-03-20", required = true)
    private LocalDate date;

    @Schema(description = "Valid from time", example = "08:00", required = true)
    private LocalTime validFrom;

    @Schema(description = "Valid until time", example = "16:00", required = true)
    private LocalTime validUntil;

    @Schema(description = "Associated user information")
    private UserDTO user;

    // Default constructor
    public TicketDTO() {
    }

    // Constructor with fields
    public TicketDTO(Long id, String ticketType, LocalDate date, LocalTime validFrom, LocalTime validUntil, UserDTO user) {
        this.id = id;
        this.ticketType = ticketType;
        this.date = date;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
} 