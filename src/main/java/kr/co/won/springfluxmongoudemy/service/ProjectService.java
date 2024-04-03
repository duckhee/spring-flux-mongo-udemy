package kr.co.won.springfluxmongoudemy.service;

import kr.co.won.springfluxmongoudemy.model.Project;
import kr.co.won.springfluxmongoudemy.model.Task;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectService {

    Mono<Project> createProject(Project project);

    Mono<Task> createTask(Task task);

    Flux<Project> findAllProject();

    Mono<Project> findById(String id);

    Mono<Void> deleteById(String id);

    Flux<Project> findByName(String name);

    Flux<Project> findByNameNot(String name);

    Flux<Project> findByEstimatedCostGreaterThan(Long cost);

    Flux<Project> findByEstimatedCostBetween(Long from, Long to);

    Flux<Project> findByNameLike(String name);

    Flux<Project> findByNameRegex(String name);

    Flux<Project> findByNameQuery(String name);

    Flux<Project> findByNameAndCostQuery(String name, Long cost);

    Flux<Project> findByCostBetweenQuery(Long from, Long to);

    Flux<Project> findByRegexGetNameAndCostQuery(String regex);

    Flux<Project> findProjectByNameQueryWithTemplate(String name);

    Flux<Project> findByCostQueryWithTemplate(Long from, Long to);

    Flux<Project> findByNameRegexQueryWithTemplate(String regex);

    Mono<Void> upsertCostWithCriteriaTemplate(String id, Long cost);

    Mono<Void> deleteWithCriteriaTemplate(String id);
}
