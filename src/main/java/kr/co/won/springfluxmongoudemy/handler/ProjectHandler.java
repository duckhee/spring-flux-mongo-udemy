package kr.co.won.springfluxmongoudemy.handler;

import kr.co.won.springfluxmongoudemy.model.Project;
import kr.co.won.springfluxmongoudemy.model.Task;
import kr.co.won.springfluxmongoudemy.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProjectHandler {

    private final ProjectService projectService;

    public ProjectHandler(@Autowired ProjectService projectService) {
        this.projectService = projectService;
    }

    public Mono<ServerResponse> createProject(ServerRequest serverRequest) {
        final Mono<Project> project = serverRequest.bodyToMono(Project.class);
        return project.flatMap(projectService::createProject)
                .flatMap(data -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(data));
    }

    public Mono<ServerResponse> createTask(ServerRequest serverRequest) {
        final Mono<Task> task = serverRequest.bodyToMono(Task.class);
        return task.flatMap(projectService::createTask)
                .flatMap(data -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(data));
    }

    public Mono<ServerResponse> findAllProject(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findAllProject(), Project.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return projectService.findById(id)
                .flatMap(data -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(data)
                        .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.deleteById(id), Void.class).log();
    }
}
