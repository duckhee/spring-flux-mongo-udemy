package kr.co.won.springfluxmongoudemy.handler;

import kr.co.won.springfluxmongoudemy.model.Project;
import kr.co.won.springfluxmongoudemy.model.Task;
import kr.co.won.springfluxmongoudemy.service.ProjectService;
import kr.co.won.springfluxmongoudemy.service.ResultByStartDateAndCost;
import kr.co.won.springfluxmongoudemy.service.ResultCount;
import kr.co.won.springfluxmongoudemy.service.ResultProjectTasks;
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

    public Mono<ServerResponse> findByNameQuery(ServerRequest serverRequest) {
        Optional<String> getParameter = serverRequest.queryParam("name");
        if (getParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameQuery(getParameter.get()), Project.class).log();
    }

    public Mono<ServerResponse> findByNameAndCostQuery(ServerRequest serverRequest) {
        Optional<String> nameParameter = serverRequest.queryParam("name");
        Optional<String> costParameter = serverRequest.queryParam("cost");
        if (nameParameter.isEmpty() || costParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameAndCostQuery(nameParameter.get(), Long.valueOf(costParameter.get())), Project.class).log();
    }

    public Mono<ServerResponse> findByEstimatedCostBetweenQuery(ServerRequest serverRequest) {
        Optional<String> fromParameter = serverRequest.queryParam("from");
        Optional<String> toParameter = serverRequest.queryParam("to");
        if (fromParameter.isEmpty() || toParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByCostBetweenQuery(Long.valueOf(fromParameter.get()), Long.valueOf(toParameter.get())), Project.class)
                .log();
    }

    public Mono<ServerResponse> findByNameRegexQuery(ServerRequest serverRequest) {
        Optional<String> nameParameter = serverRequest.queryParam("name");
        if (nameParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByRegexGetNameAndCostQuery("^" + nameParameter.get() + ""), Project.class)
                .log();
    }

    public Mono<ServerResponse> findByNameQueryTemplate(ServerRequest serverRequest) {
        Optional<String> nameParameter = serverRequest.queryParam("name");
        if (nameParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findProjectByNameQueryWithTemplate(nameParameter.get()), Project.class)
                .log();
    }

    public Mono<ServerResponse> findByCostQueryWithTemplate(ServerRequest serverRequest) {
        Optional<String> fromParameter = serverRequest.queryParam("from");
        Optional<String> toParameter = serverRequest.queryParam("to");
        if (fromParameter.isEmpty() || toParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByCostQueryWithTemplate(Long.valueOf(fromParameter.get()), Long.valueOf(toParameter.get())), Project.class)
                .log();
    }

    public Mono<ServerResponse> findByNameRegexQueryWithTemplate(ServerRequest serverRequest) {
        Optional<String> nameParameter = serverRequest.queryParam("name");
        if (nameParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameRegexQueryWithTemplate("^" + nameParameter.get() + ""), Project.class)
                .log();
    }

    public Mono<ServerResponse> upsertCostWithCriteriaTemplate(ServerRequest serverRequest) {
        Optional<String> idParameter = serverRequest.queryParam("id");
        Optional<String> costParameter = serverRequest.queryParam("cost");
        if (idParameter.isEmpty() || costParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.upsertCostWithCriteriaTemplate(idParameter.get(), Long.valueOf(costParameter.get())), Void.class)
                .log();
    }

    public Mono<ServerResponse> deleteWithCriteriaTemplate(ServerRequest serverRequest) {
        Optional<String> idParameter = serverRequest.queryParam("id");
        if (idParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.deleteWithCriteriaTemplate(idParameter.get()), Void.class)
                .log();
    }

    public Mono<ServerResponse> findNoOfProjectCostGreaterThan(ServerRequest serverRequest) {
        Optional<String> costParameter = serverRequest.queryParam("cost");
        if (costParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findNoOfProjectsCostGreaterThan(Long.valueOf(costParameter.get())), Long.class)
                .log();
    }

    public Mono<ServerResponse> findCostsGroupByStartDateForProjectsCostGreaterThan(ServerRequest serverRequest) {
        Optional<String> costParameter = serverRequest.queryParam("cost");
        if (costParameter.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findCostsGroupByStartDateForProjectsCostGreaterThan(Long.valueOf(costParameter.get())), ResultByStartDateAndCost.class)
                .log();
    }

    public Mono<ServerResponse> findAllProjectTasks(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findAllProjectTasks(), ResultProjectTasks.class)
                .log();
    }

    public Mono<ServerResponse> saveProjectAndTasks(ServerRequest serverRequest) {
        Project project = new Project();
        project.set_id("6");
        project.setName("project6");
        Task task = new Task();
        task.set_id("10");
        task.setProjectId("6");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.saveProjectAndTask(Mono.just(project), Mono.just(task)), Void.class)
                .log();
    }

    public Mono<ServerResponse> chunkAndSaveProject(ServerRequest serverRequest) {
        Project project = new Project();
        project.set_id("20");
        project.setName("ProjectGridFS");

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.chunkAndSaveProject(project), Void.class).log();
    }

    public Mono<ServerResponse> loadProjectFromGrid(ServerRequest serverRequest) {
        Optional<String> pidQuery = serverRequest.queryParam("pid");
        if (pidQuery.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.loadProjectFromGridFS(pidQuery.get()), Project.class)
                .log();
    }

    public Mono<ServerResponse> deleteProjectFromGridFS(ServerRequest serverRequest) {
        Optional<String> pidQuery = serverRequest.queryParam("pid");
        if (pidQuery.isPresent()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.deletedProjectFromGridFS(pidQuery.get()), Void.class)
                .log();
    }

    public Mono<ServerResponse> findNameDescriptionForMatchingTerm(ServerRequest serverRequest) {
        Optional<String> termQuery = serverRequest.queryParam("term");
        if (termQuery.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findNameDescriptionForMatchingTerm(termQuery.get()), Project.class)
                .log();
    }

    public Mono<ServerResponse> findNameDescriptionForMatchingAny(ServerRequest serverRequest) {
        Mono<String[]> requestBody = serverRequest.bodyToMono(String[].class);
        Flux<Project> resultProject = requestBody.flatMapMany(projectService::findNameDescriptionFormMatchingAny);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resultProject, Project.class)
                .log();
    }
}
