package org.ogz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "message-collection")
public class Message {
    @Id
    private String id;
    private String fromUser;
    private String message;
    private String toUser;
    private String toGroup;
    private LocalDateTime sendDate;

    private String communicationId;

}
