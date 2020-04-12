package co.uk.travelChat.model;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;


@Document(collection = "tripTable")
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Trip {

    //todo add in journey as parent that contains multiple trips
    //todo add in layover for gaps in journey timeline
    //todo add in company, sometimes applicable, but we'll see

    @Id
    private String id;
    @FutureOrPresent
    private Calendar leaving;//leaving time for trip//2019-01-01T00:00:00.000+00:00//"2019-01-01T23:00:00"
    @FutureOrPresent
    private Calendar arriving;//arriving at time
    @NotNull
    private Location departing;//departing location
    @NotNull
    private Location destination;//destination trip end
    private ModeOfTransport mode;
    private Boolean discoverable; //  not implemented
    // @UniqueElements
    private List<String> attending;
    //TODO add for stream line user experience
    //private List<String> invited;

//    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy HH:ii:ss");
//    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Trip(String id, Calendar leaving, Calendar arriving,
                Location departing, Location destination,
                ModeOfTransport mode, boolean discoverable, List attending) {
        this.id = id;
        this.leaving = leaving;
        this.arriving = arriving;
        this.departing = departing;
        this.destination = destination;
        this.mode = mode;
        this.discoverable = discoverable;
        this.attending = attending;
    }

    public Trip(String id, long leaving, long arriving,
                Location departing, Location destination,
                ModeOfTransport mode, boolean discoverable, List attending) {
        this.id = id;
        this.leaving = longToCalendar(leaving);
        this.arriving = longToCalendar(arriving);
        this.departing = departing;
        this.destination = destination;
        this.mode = mode;
        this.discoverable = discoverable;
        this.attending = attending;
    }

    public Trip(String id, Calendar leaving, Calendar arriving,
                Location departing, Location destination,
                ModeOfTransport mode) {
        this.id = id;
        this.leaving = leaving;
        this.arriving = arriving;
        this.departing = departing;
        this.destination = destination;
        this.mode = mode;
    }

    public Trip(String id, long leaving, long arriving,
                Location departing, Location destination,
                ModeOfTransport mode) {
        this.id = id;
        this.leaving = longToCalendar(leaving);
        this.arriving = longToCalendar(arriving);
        this.departing = departing;
        this.destination = destination;
        this.mode = mode;
    }

    public Trip(Calendar leaving, Calendar arriving,
                Location departing, Location destination, ModeOfTransport mode) {
        this.leaving = leaving;
        this.arriving = arriving;
        this.departing = departing;
        this.destination = destination;
        this.mode = mode;
    }

    public Trip(long leaving, long arriving,
                Location departing, Location destination, ModeOfTransport mode) {
        this.leaving = longToCalendar(leaving);
        this.arriving = longToCalendar(arriving);
        this.departing = departing;
        this.destination = destination;
        this.mode = mode;
    }

    public Calendar longToCalendar(Long epoch) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(epoch);
        return calendar;
    }

}

//    leaving=java.util.GregorianCalendar[
//        time=1585396800000,
//        areFieldsSet=true,
//        areAllFieldsSet=true,
//        lenient=true,
//        zone=sun.util.calendar.ZoneInfo[
//        id="Europe/London",
//        offset=0,
//        dstSavings=3600000,
//        useDaylight=true,
//        transitions=242,
//        lastRule=java.util.SimpleTimeZone[
//        id=Europe/London,
//        offset=0,
//        dstSavings=3600000,
//        useDaylight=true,
//        startYear=0,
//        startMode=2,
//        startMonth=2,
//        startDay=-1,
//        startDayOfWeek=1,
//        startTime=3600000,
//        startTimeMode=2,
//        endMode=2,
//        endMonth=9,
//        endDay=-1,
//        endDayOfWeek=1,
//        endTime=3600000,
//        endTimeMode=2
//        ]
//        ],
//        firstDayOfWeek=2,
//        minimalDaysInFirstWeek=4,
//        ERA=1,
//        YEAR=2020,
//        MONTH=2,
//        WEEK_OF_YEAR=13,
//        WEEK_OF_MONTH=4,
//        DAY_OF_MONTH=28,
//        DAY_OF_YEAR=88,
//        DAY_OF_WEEK=7,
//        DAY_OF_WEEK_IN_MONTH=4,
//        AM_PM=1,
//        HOUR=0,
//        HOUR_OF_DAY=12,
//        MINUTE=0,SECOND=0,
//        MILLISECOND=0,
//        ZONE_OFFSET=0,
//        DST_OFFSET=0
//    ]
