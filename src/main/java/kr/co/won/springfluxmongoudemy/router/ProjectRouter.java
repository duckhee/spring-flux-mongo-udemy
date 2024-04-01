package kr.co.won.springfluxmongoudemy.router;

import kr.co.won.springfluxmongoudemy.handler.ProjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class ProjectRouter {

    @Bean
    RouterFunction<ServerResponse> routerProject(ProjectHandler projectHandler) {
        return RouterFunctions
                .route(RequestPredicates.POST("/api/project/create")
                        .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::createProject)
                .andRoute(RequestPredicates.GET("/api/project/{id}").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findById)
                .andRoute(RequestPredicates.GET("/api/project").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findAllProject)
                .andRoute(RequestPredicates.DELETE("/api/project/{id}").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::deleteById)
                .andRoute(RequestPredicates.POST("/api/task/create")
                        .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::createTask);

    }
}
