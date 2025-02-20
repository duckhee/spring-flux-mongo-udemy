package kr.co.won.springfluxmongoudemy.service;

import kr.co.won.springfluxmongoudemy.model.Project;
import kr.co.won.springfluxmongoudemy.model.Task;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
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

    /**
     * Mongo Database Aggregate match, count
     */
    Mono<Long> findNoOfProjectsCostGreaterThan(Long cost);

    /**
     * Mongo Database Aggregate group, sort
     */
    Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan(Long cost);

    /**
     * Mongo Database Join and selected specific field
     */
    Flux<ResultProjectTasks> findAllProjectTasks();

    /**
     * Mongo Database Transaction
     * => need to replicaset
     */
    Mono<Void> saveProjectAndTask(Mono<Project> project, Mono<Task> task);

    /**
     * file 을 gridFS 로 데이터 베이스에 저장
     */
    Mono<Void> chunkAndSaveProject(Project project);

    /**
     * file data 를 chunk 로 쪼개져 있는 것을 해당 값 형태로 가져오기
     */
    Mono<Project> loadProjectFromGridFS(String projectId);

    /**
     * file 로 저장된 데이터를 삭제하는 기능
     */
    Mono<Void> deletedProjectFromGridFS(String projectId);

    /**
     * index 를 이용해서 데이터를 찾는 기능
     */
    Flux<Project> findNameDescriptionForMatchingTerm(String term);

    /**
     * 단어를 가지고 검색하는 기능
     * => like 와 동일하게 동작한ㄷ.
     */
    public Flux<Project> findNameDescriptionFormMatchingAny(String... words);
}
