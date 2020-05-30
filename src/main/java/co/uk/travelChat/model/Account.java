package co.uk.travelChat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Document
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    private String nickname;
    private String name;
    @NotNull
    //@UniqueElements
    private List<String> trips;
    //TODO add for user experience
    //private List<String> invitations;// list of trips account has been invited too
    //private List<String> friends;//list of mutual friends for quick reference

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("nickname='").append(nickname).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", trips=").append(trips);
        sb.append('}');
        return sb.toString();
    }
}
