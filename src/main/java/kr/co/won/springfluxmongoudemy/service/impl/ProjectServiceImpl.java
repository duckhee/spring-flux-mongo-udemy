package kr.co.won.springfluxmongoudemy.service.impl;

import com.mongodb.client.model.Aggregates;
import kr.co.won.springfluxmongoudemy.model.Project;
import kr.co.won.springfluxmongoudemy.model.Task;
import kr.co.won.springfluxmongoudemy.repository.ProjectRepository;
import kr.co.won.springfluxmongoudemy.repository.TaskRepository;
import kr.co.won.springfluxmongoudemy.service.ProjectService;
import kr.co.won.springfluxmongoudemy.service.ResultByStartDateAndCost;
import kr.co.won.springfluxmongoudemy.service.ResultCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    private final ReactiveMongoTemplate template;
    private final ReactiveMongoTemplate reactiveMongoTemplate;


    public ProjectServiceImpl(@Autowired ProjectRepository projectRepository, @Autowired TaskRepository taskRepository, @Autowired ReactiveMongoTemplate template, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.template = template;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Project> createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Mono<Task> createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Flux<Project> findAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public Mono<Project> findById(String id) {
        return projectRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return projectRepository.deleteById(id);
    }

    @Override
    public Flux<Project> findByName(String name) {
        return projectRepository.findByName(name);
    }

    @Override
    public Flux<Project> findByNameNot(String name) {
        return projectRepository.findByNameNot(name);
    }

    @Override
    public Flux<Project> findByEstimatedCostGreaterThan(Long cost) {
        return projectRepository.findByEstimatedCostGreaterThan(cost);
    }

    @Override
    public Flux<Project> findByEstimatedCostBetween(Long from, Long to) {
        return projectRepository.findByEstimatedCostBetween(from, to);
    }

    @Override
    public Flux<Project> findByNameLike(String name) {
        return projectRepository.findByNameLike(name);
    }

    @Override
    public Flux<Project> findByNameRegex(String name) {
        return projectRepository.findByNameRegex(name);
    }

    @Override
    public Flux<Project> findByNameQuery(String name) {
        return projectRepository.findProjectByNameQuery(name);
    }

    @Override
    public Flux<Project> findByNameAndCostQuery(String name, Long cost) {
        return projectRepository.findProjectByNameAndCostQuery(name, cost);
    }

    @Override
    public Flux<Project> findByCostBetweenQuery(Long from, Long to) {
        return projectRepository.findProjectByEstimatedCostBetweenQuery(from, to, Sort.by(Sort.Direction.DESC, "cost"));
    }

    @Override
    public Flux<Project> findByRegexGetNameAndCostQuery(String regex) {
        return projectRepository.findByNameRegexQuery(regex);
    }

    @Override
    public Flux<Project> findProjectByNameQueryWithTemplate(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return template.find(query, Project.class);
    }

    @Override
    public Flux<Project> findByCostQueryWithTemplate(Long from, Long to) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "cost"));
        query.addCriteria(Criteria.where("cost").lt(to).gt(from));
        return template.find(query, Project.class);
    }

    @Override
    public Flux<Project> findByNameRegexQueryWithTemplate(String regex) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(regex));
        return template.find(query, Project.class);
    }

    @Override
    public Mono<Void> upsertCostWithCriteriaTemplate(String id, Long cost) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("cost", cost);
        return template.upsert(query, update, Project.class).then();
    }

    @Override
    public Mono<Void> deleteWithCriteriaTemplate(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id"));

        return template.remove(query, Project.class).then();
    }

    /**
     * db.project.aggregate([{$match:{"cost":{$gt:cost}}}, {$count:"totalCustomName"}])
     * => 집계 쿼리를 실행하는 함수이다.
     */
    @Override
    public Mono<Long> findNoOfProjectsCostGreaterThan(Long cost) {
        /** 해당 되는 조건에 맞는 경우만 선택하기 위한 조건이다. */
        MatchOperation matchOperation = Aggregation.match(new Criteria("cost").gt(cost));
        /** count 한 값을 as 값으로 필드 생성한다는 의미이다. */
        CountOperation countOperation = Aggregation.count().as("costly_projects");
        /** 집계하기 위한 단계 별 조건에 대한 것을 취합한 하나의 집계 MQL을 만들어 준다. */
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, countOperation);
        /** 집계 관련 쿼리를 Project table 에서 결과 값을 마짐가에 넣어준 class 로 받아준다. */
        Flux<ResultCount> aggregateResult = reactiveMongoTemplate.aggregate(aggregation, Project.class, ResultCount.class);
        /** 결과 값을 하나 가져와서 비동기로 반환 해준다. */
        return aggregateResult.map(mongoQueryResult -> mongoQueryResult.getCostly_projects()).switchIfEmpty(Flux.just(0l)).take(1).single();

    }

    /**
     * db.project.aggregate([{$match:{"cost":{$gt:cost}}}, {$group:{"_id":"$startDate", total:{$sum:"$cost"}}}, {$sort:-1}])
     * => 집계 쿼리를 실행하는 함수이다.
     */
    @Override
    public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan(Long cost) {
        MatchOperation matchOperation = Aggregation.match(new Criteria("cost").gt(cost));
        GroupOperation groupOperation = Aggregation.group("startDate").sum("cost").as("total");
        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "total"));
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, sortOperation);
        return reactiveMongoTemplate.aggregate(aggregation, Project.class, ResultByStartDateAndCost.class);
    }
}
