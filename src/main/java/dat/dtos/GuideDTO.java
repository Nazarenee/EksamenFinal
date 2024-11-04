package dat.dtos;

import dat.entities.Guide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuideDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private double yearsOfExperience;
    private List<TripDTO> trips = new ArrayList<>();

    public GuideDTO(String firstName, String lastName, String email, String phoneNumber, double yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.yearsOfExperience = yearsOfExperience;
    }

    public GuideDTO(Guide guide){
        this.id = guide.getId();
        this.firstName = guide.getFirstName();
        this.lastName = guide.getLastName();
        this.email = guide.getEmail();
        this.phoneNumber = guide.getPhoneNumber();
        this.yearsOfExperience = guide.getYearsOfExperience();
    }

}
