package kr.co.won.springfluxmongoudemy.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "task")
public class Task {

    @Id
    private String _id;

    @Field(name = "pid")
    private String projectId;

    private String name;

    @Field(name = "desc")
    private String description;

    private String ownerName;

    private long cost;

    @Version
    private Long version;

}
