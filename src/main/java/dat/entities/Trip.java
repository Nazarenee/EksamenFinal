package dat.entities;

import dat.dtos.TripDTO;
import dat.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trip {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "start_location")
    private String startLocation;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "guide_id", nullable = true)
    private Guide guide;

    public Trip(TripDTO tripDTO){
        this.id = tripDTO.getId();
        this.startTime = tripDTO.getStartTime();
        this.endTime = tripDTO.getEndTime();
        this.startLocation = tripDTO.getStartLocation();
        this.name = tripDTO.getName();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();
        this.guide = new Guide(tripDTO.getGuide());
    }


    public Trip(int id, LocalDateTime now, LocalDateTime localDateTime, String startlocation, String cityTour, int price, Category category) {
        this.id=id;
        this.startTime = now;
        this.endTime = localDateTime;
        this.startLocation = startlocation;
        this.name = cityTour;
        this.price = price;
        this.category = category;
    }
}
