package at.htlleonding.skigebietmanager.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "User data transfer object")
public class UserDTO {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Name of the user", example = "Max Mustermann", required = true)
    private String name;

    @Schema(description = "Email address of the user", example = "max@example.com", required = true)
    private String email;

    // Default constructor
    public UserDTO() {
    }

    // Constructor with fields
    public UserDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} 