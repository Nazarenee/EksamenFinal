package dat.entities;

import dat.dtos.PackingItemDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
public class PackingItemsResponse {
    private List<PackingItemDTO> items;

}
