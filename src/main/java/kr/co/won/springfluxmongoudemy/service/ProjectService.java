package kr.co.won.springfluxmongoudemy.service;

import kr.co.won.springfluxmongoudemy.model.Project;
import kr.co.won.springfluxmongoudemy.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectService {

    Mono<Project> createProject(Project project);

    Mono<Task> createTask(Task task);

    Flux<Project> findAllProject();

    Mono<Project> findById(String id);

    Mono<Void> deleteById(String id);
}
