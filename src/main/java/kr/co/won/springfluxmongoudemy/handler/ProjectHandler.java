package kr.co.won.springfluxmongoudemy.handler;

import kr.co.won.springfluxmongoudemy.model.Project;
import kr.co.won.springfluxmongoudemy.model.Task;
import kr.co.won.springfluxmongoudemy.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ConcurrentModificationException;
import java.util.Optional;

@Slf4j
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
//                .onErrorResume(error -> {
//                    if (error instanceof OptimisticLockingFailureException) {
//                        log.warn("optimistic locking error : {}", error.toString());
//                        return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
//                    }
//                    log.warn("create project error : ${}", error.toString());
//                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//                });
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

    public Mono<ServerResponse> findByName(ServerRequest serverRequest) {
        Optional<String> getParameter = serverRequest.queryParam("name");
        if (getParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByName(getParameter.get()), Project.class).log()
                .switchIfEmpty(Mono.empty());
    }

    public Mono<ServerResponse> findByNameNot(ServerRequest serverRequest) {
        Optional<String> getParameter = serverRequest.queryParam("name");
        if (getParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameNot(getParameter.get()), Project.class).log()
                .switchIfEmpty(Mono.empty());
    }

    public Mono<ServerResponse> findByEstimatedCostGreaterThan(ServerRequest serverRequest) {
        Optional<String> getParameter = serverRequest.queryParam("cost");
        if (getParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByEstimatedCostGreaterThan(Long.valueOf(getParameter.get())), Project.class).log()
                .switchIfEmpty(Mono.empty());
    }

    public Mono<ServerResponse> findByEstimatedCostGreaterBetween(ServerRequest serverRequest) {
        Optional<String> getRangeStartParameter = serverRequest.queryParam("from");
        Optional<String> getRangeEndParameter = serverRequest.queryParam("to");
        // parameter null checking
        if (getRangeStartParameter.isEmpty() || getRangeEndParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByEstimatedCostBetween(Long.valueOf(getRangeStartParameter.get()), Long.valueOf(getRangeEndParameter.get())), Project.class).log()
                .switchIfEmpty(Mono.empty());
    }

    public Mono<ServerResponse> findByNameLike(ServerRequest serverRequest) {
        Optional<String> getParameter = serverRequest.queryParam("name");
        if (getParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameLike(getParameter.get()), Project.class).log();
    }

    public Mono<ServerResponse> findByNameRegex(ServerRequest serverRequest) {
        Optional<String> getParameter = serverRequest.queryParam("name");
        if (getParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameRegex("^" + getParameter.get() + ""), Project.class).log();
    }
}
