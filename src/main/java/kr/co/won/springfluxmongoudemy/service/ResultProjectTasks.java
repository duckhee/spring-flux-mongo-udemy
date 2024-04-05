package kr.co.won.springfluxmongoudemy.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultProjectTasks {

    private String _id;

    private String name;

    private String taskName;

    private String taskOwnerName;
}
