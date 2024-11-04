package dat.entities;

import dat.dtos.GuideDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "years_of_experience")
    private double yearsOfExperience;
    @OneToMany(mappedBy = "guide", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Trip> trips;


    public Guide(GuideDTO guideDTO) {
        this.id = guideDTO.getId();
        this.firstName = guideDTO.getFirstName();
        this.lastName = guideDTO.getLastName();
        this.email = guideDTO.getEmail();
        this.phoneNumber = guideDTO.getPhoneNumber();
        this.yearsOfExperience = guideDTO.getYearsOfExperience();
        this.trips = guideDTO.getTrips() != null ? guideDTO.getTrips().stream().map(tripDTO -> {
            Trip trip = new Trip(tripDTO);
            trip.setGuide(this);
            return trip;
        }).collect(Collectors.toList()) : new ArrayList<>();
    }

    public Guide(long id, String firstName, String lastName, String email, String phoneNumber, double yearsOfExperience) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.yearsOfExperience = yearsOfExperience;
    }
}