package dat.entities;

import dat.dtos.PackingItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PackingItemsResponse {
    private List<PackingItemDTO> items;

}
