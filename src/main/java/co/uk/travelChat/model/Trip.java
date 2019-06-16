package co.uk.travelChat.model;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection="tripTable")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trip {

    //todo add in journey as parent that contains multiple trips
        //todo add in layover for gaps in journey timeline
    //todo add in company, sometimes applicable, but we'll see

    @Id
    private String id;
    private LocalDateTime leaving;
    private LocalDateTime arriving;
    private Location departing;
    private Location destination;
    private ModeOfTransport mode;
    private Boolean discoverable;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Trip{");
        sb.append("id='").append(id).append('\'');
        sb.append(", leaving=").append(leaving);
        sb.append(", arriving=").append(arriving);
        sb.append(", departing=").append(departing);
        sb.append(", destination=").append(destination);
        sb.append(", mode=").append(mode);
        sb.append(", discoverable=").append(discoverable);
        sb.append('}');
        return sb.toString();
    }
}
