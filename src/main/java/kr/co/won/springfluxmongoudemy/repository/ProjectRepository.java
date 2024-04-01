package kr.co.won.springfluxmongoudemy.repository;

import kr.co.won.springfluxmongoudemy.model.Project;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProjectRepository extends ReactiveMongoRepository<Project, String> {

    Mono<Project> findBy_id(String id);

    Mono<Void> deleteBy_id(String id);
}
