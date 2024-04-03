package kr.co.won.springfluxmongoudemy.repository;

import kr.co.won.springfluxmongoudemy.model.Project;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectRepository extends ReactiveMongoRepository<Project, String> {

    Mono<Project> findBy_id(String id);

    Mono<Void> deleteBy_id(String id);

    /**
     * db.project.find({"name":name})
     * => find data match name field value name
     */
    Flux<Project> findByName(String name);

    /**
     * db.project.find({"name":{"$ne":name}})
     * => find data not match name field value name
     */
    Flux<Project> findByNameNot(String name);

    /**
     * db.project.find({"cost:{"$gt":cost}})
     * => find data cost field value > cost
     */
    Flux<Project> findByEstimatedCostGreaterThan(Long cost);

    /**
     * db.project.find({"cost":{"$gt":from, "$lt":to}})
     * => find data cost field value > from and cost field value < to
     */
    Flux<Project> findByEstimatedCostBetween(Long from, Long to);

    /**
     * db.project.find({"name":/name/})
     * => find data name field value like name
     */
    Flux<Project> findByNameLike(String name);

    /**
     * db.project.find({"name":{"$regex":name}})
     * => find data name field value regex
     */
    Flux<Project> findByNameRegex(String name);

    /**
     * db.project.find({"name": name})
     * => find data name field value match
     * using Mongo Query Language
     */
    @Query(value = "{'name': ?0}")
    Flux<Project> findProjectByNameQuery(String name);

    /**
     * db.project.find({"name":name, "cost":cost})
     * => find data name field with cost field match
     * using Mongo Query Language
     */
    @Query(value = "{'name': ?0, 'cost': ?1}")
    Flux<Project> findProjectByNameAndCostQuery(String name, Long cost);

    /**
     * db.project.find({cost:{$lt:from, $gt:to}, {sort}}
     * => find data cost field between
     * using Mongo Query Language
     */
    @Query(value = "{cost: {$lt: ?0, $gt: ?1}}")
    Flux<Project> findProjectByEstimatedCostBetweenQuery(Long from, Long to, Sort sort);

    /**
     * db.project.find({name:{$regex:reg}})
     * => find data name field reg and get name field, cost field, _id field
     * using Mongo Query Language
     */
    @Query(value = "{'name': {$regex: ?0}}", fields = "{'name': 1, 'cost': 1}")
    Flux<Project> findByNameRegexQuery(String regexp);
}
