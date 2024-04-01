package kr.co.won.springfluxmongoudemy.repository;

import kr.co.won.springfluxmongoudemy.model.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
}
