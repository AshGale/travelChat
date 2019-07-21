package co.uk.travelChat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection="accountTable")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    private String id;
    private String name;
    private String nickname;
    private Map<Integer, Trip> trips;//TODO could add as list of keys, not full obj's

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", nickname='").append(nickname).append('\'');
        sb.append(", trips=").append(trips);
        sb.append('}');
        return sb.toString();
    }
}
