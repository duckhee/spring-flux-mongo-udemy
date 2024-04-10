package kr.co.won.springfluxmongoudemy.service.impl;

import com.mongodb.client.model.Aggregates;
import kr.co.won.springfluxmongoudemy.model.Project;
import kr.co.won.springfluxmongoudemy.model.Task;
import kr.co.won.springfluxmongoudemy.repository.ProjectRepository;
import kr.co.won.springfluxmongoudemy.repository.TaskRepository;
import kr.co.won.springfluxmongoudemy.service.ProjectService;
import kr.co.won.springfluxmongoudemy.service.ResultByStartDateAndCost;
import kr.co.won.springfluxmongoudemy.service.ResultCount;
import kr.co.won.springfluxmongoudemy.service.ResultProjectTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * db.project.aggregate([{$lookup:{from:"task", localField:"_id", foreignField:"pid", as: "ProjectTasks"}}, {$unwind:"$ProjectTasks"}, {$project:{_id:1, name:1,taskName:"$ProjectTasks.name", taskOwnerName:"$ProjectTasks.owername"}}])
     * => Join 을 사용할 때, $lookup 을 이용한다.
     * => 배열의 요소를 분리할 때 사용할 때 $unwind 를 사용해서 데이터를 분리한다.
     * => 필드에 대한 값을 선택해서 가져올 때 $project 를 사용한다.
     */
    @Override
    public Flux<ResultProjectTasks> findAllProjectTasks() {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("task") // 조인을 진행할 collection 의 이름을 넣어 준다.
                .localField("_id") // 현재 collection 에서 외래키가 되는 대상에 대한 field 값을 넣어준다.
                .foreignField("pid") // 외래키로 정의된 field 값을 넣어준다.
                .as("ProjectTasks"); // 별칭을 지정한다.
        UnwindOperation unwindOperation = Aggregation.unwind("projectTasks"); // 배열의 요소를 분리할 때 사용하는 aggregate
        ProjectionOperation projectionOperation = Aggregation.project()
                .andExpression("_id") // 가져올 특정 값
                .as("_id") // 가져올 때 보여주기 위한 별칭
                .andExpression("name") // 가져올 특정 값
                .as("name") // 가져올 때 보여주기 위한 별칭
                .andExpression("ProjectTasks.name") // 가져올 특정 값
                .as("taskName")// 가져올 때 보여주기 위한 별칭
                .andExpression("ProjectTasks.owername") // 가져올 특정 값
                .as("taskOwnerName"); // 가져올 때 보여주기 위한 별칭
        /** aggregate 에 대한 조합을 묶어서 전달해준다. */
        Aggregation aggregation = Aggregation.newAggregation(lookupOperation, unwindOperation, projectionOperation);
        return reactiveMongoTemplate.aggregate(aggregation, Project.class, ResultProjectTasks.class);
    }

    /**
     *
     */
    @Transactional
    @Override
    public Mono<Void> saveProjectAndTask(Mono<Project> project, Mono<Task> task) {

        return project.flatMap(projectRepository::save)
                .then(task).flatMap(taskRepository::save).then();

    }
}
