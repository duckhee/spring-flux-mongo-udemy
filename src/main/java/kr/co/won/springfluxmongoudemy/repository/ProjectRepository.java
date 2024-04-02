package kr.co.won.springfluxmongoudemy.repository;

import kr.co.won.springfluxmongoudemy.model.Project;
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
}
