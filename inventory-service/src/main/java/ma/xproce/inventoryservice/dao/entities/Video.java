package ma.xproce.inventoryservice.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Video {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String description;
    private String datePublication;
    @ManyToOne(fetch = FetchType.EAGER)
    private Creator creator;

}
