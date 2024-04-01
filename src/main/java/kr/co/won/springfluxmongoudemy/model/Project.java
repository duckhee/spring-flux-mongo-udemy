package kr.co.won.springfluxmongoudemy.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "project")
public class Project {
    @Id
    private String _id;
    private String name;
    private String code;
    @Field(name = "desc")
    private String description;

    private String startDate;

    private String endDate;

    @Field(name = "cost")
    private long estimatedCost;

    private List<String> countryList;

    @Version
    private Long version;
}
