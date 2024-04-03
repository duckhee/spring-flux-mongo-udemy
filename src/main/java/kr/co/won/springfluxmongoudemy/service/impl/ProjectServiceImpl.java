package kr.co.won.springfluxmongoudemy.service.impl;

import kr.co.won.springfluxmongoudemy.model.Project;
import kr.co.won.springfluxmongoudemy.model.Task;
import kr.co.won.springfluxmongoudemy.repository.ProjectRepository;
import kr.co.won.springfluxmongoudemy.repository.TaskRepository;
import kr.co.won.springfluxmongoudemy.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;


    public ProjectServiceImpl(@Autowired ProjectRepository projectRepository, @Autowired TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
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
}
