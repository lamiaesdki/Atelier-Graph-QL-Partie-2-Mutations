package ma.xproce.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoRequest {
    private String name;
    private String url;
    private String description;  // Description is optional and may not be saved in the Video entity
    private String datePublication;  // String to accept the date in the input
    private CreatorRequest creator;  // Nested creator field
}