package co.uk.travelChat.model;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Document(collection="tripTable")
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Trip {

    //todo add in journey as parent that contains multiple trips
        //todo add in layover for gaps in journey timeline
    //todo add in company, sometimes applicable, but we'll see

    @Id
    private String id;
    private LocalDateTime leaving;//leaving time for trip
    private LocalDateTime arriving;//arriving at time
    private Location departing;//departing location
    private Location destination;//destination trip end
    private ModeOfTransport mode;
    private Boolean discoverable; //  not implemented
    private List<String> attending;
    //todo add in who is going on this trip for query optimization
//    public Trip(LocalDateTime leaving, Location departing, ModeOfTransport mode) {
//        this.leaving = leaving;
//        this.departing = departing;
//        this.mode = mode;
//    }

    public Trip(LocalDateTime leaving, LocalDateTime arriving,
                Location departing, Location destination, ModeOfTransport mode) {
        this.leaving = leaving;
        this.arriving = arriving;
        this.departing = departing;
        this.destination = destination;
        this.mode = mode;
    }


}
