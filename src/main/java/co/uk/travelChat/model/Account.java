package co.uk.travelChat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="accountTable")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    private String id;
    private String name;
    private Double value;

    // getters and setters
}
