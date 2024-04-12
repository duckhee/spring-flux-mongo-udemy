package kr.co.won.springfluxmongoudemy.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.util.List;

/**
 * index 생성 하는 방법
 * => db.project.createIndex({name:"text", desc:"text"}, {name:"namedesctextindex"})
 */
@Data
@Document(collection = "project")
public class Project {
    @Id
    private String _id;

    @TextIndexed(weight = 10)
    private String name;
    private String code;

    @TextIndexed
    @Field(name = "desc")
    private String description;

    private String startDate;

    private String endDate;

    @Field(name = "cost")
    private long estimatedCost;

    private List<String> countryList;

    @Version
    private Long version;

    /**
     * text 에서 값을 찾을 때 점수를 저장하기 위한 변수 및 annotation
     */
    @TextScore
    private Float score;
}