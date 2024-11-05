package dat.dtos;

import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TripDTO {
    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String startLocation;
    private String name;
    private int price;
    private Category category;
    private GuideDTO guide;

    public TripDTO(LocalDateTime startTime, LocalDateTime endTime, String startLocation, String name, int price, Category category) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLocation = startLocation;
        this.name = name;
        this.price = price;
        this.category = category;
    }


    public TripDTO(Trip trip){
        this.id = trip.getId();
        this.startTime = trip.getStartTime();
        this.endTime = trip.getEndTime();
        this.startLocation = trip.getStartLocation();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.category = trip.getCategory();
        this.guide = new GuideDTO(trip.getGuide());

    }


    public TripDTO(long id, LocalDateTime startTime, LocalDateTime endTime, String startLocation, String name, int price, Category category) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLocation = startLocation;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
