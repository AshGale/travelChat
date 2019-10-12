package co.uk.travelChat.model;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Document(collection = "tripTable")
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
    private LocalDateTime leaving;//leaving time for trip//2019-01-01T00:00:00.000+00:00//"2019-01-01T23:00:00"
    private LocalDateTime arriving;//arriving at time
    private Location departing;//departing location
    private Location destination;//destination trip end
    private ModeOfTransport mode;
    private Boolean discoverable; //  not implemented
    private List<String> attending;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Trip(LocalDateTime leaving, LocalDateTime arriving,
                Location departing, Location destination, ModeOfTransport mode) {
        this.leaving = leaving;
        this.arriving = arriving;
        this.departing = departing;
        this.destination = destination;
        this.mode = mode;
    }

    //todo move out to util class\
    //Deprecated
    public static String formatToSting(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);

    }

    public static DateTimeFormatter getFormatter() {
        return formatter;
    }

}
