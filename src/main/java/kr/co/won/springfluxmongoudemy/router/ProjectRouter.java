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
                .andRoute(RequestPredicates.GET("/api/project/find/by-name").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByName)
                .andRoute(RequestPredicates.GET("/api/project/find/not-name").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByNameNot)
                .andRoute(RequestPredicates.GET("/api/project/find/by-estimatedCost-greater-than").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByEstimatedCostGreaterThan)
                .andRoute(RequestPredicates.GET("/api/project/find/by-estimatedCost-between").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByEstimatedCostGreaterBetween)
                .andRoute(RequestPredicates.GET("/api/project/find/by-name-link").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByNameLike)
                .andRoute(RequestPredicates.GET("/api/project/find/by-name-regex").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByNameRegex)
                .andRoute(RequestPredicates.GET("/api/project/find/query/by-name").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByNameQuery)
                .andRoute(RequestPredicates.GET("/api/project/find/query/by-name-and-cost").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByNameAndCostQuery)
                .andRoute(RequestPredicates.GET("/api/project/find/query/by-cost-between").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByEstimatedCostBetweenQuery)
                .andRoute(RequestPredicates.GET("/api/project/find/query/by-name-regex").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByNameRegexQuery)
                .andRoute(RequestPredicates.GET("/api/project/find/template/by-name").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByNameQueryTemplate)
                .andRoute(RequestPredicates.GET("/api/project/find/template/by-name-regex").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByNameRegexQueryWithTemplate)
                .andRoute(RequestPredicates.GET("/api/project/find/template/by-cost-between").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findByCostQueryWithTemplate)
                .andRoute(RequestPredicates.POST("/api/project/find/template/update-cost").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::upsertCostWithCriteriaTemplate)
                .andRoute(RequestPredicates.POST("/api/project/find/template/deleted").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::deleteWithCriteriaTemplate)
                .andRoute(RequestPredicates.GET("/api/project/aggregate/template/find-no-of-project-cost-greater-than").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findNoOfProjectCostGreaterThan)
                .andRoute(RequestPredicates.GET("/api/project/aggregate/template/find-cost-group-by-start-date-greater-than").and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::findCostsGroupByStartDateForProjectsCostGreaterThan)
                .andRoute(RequestPredicates.POST("/api/task/create")
                        .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), projectHandler::createTask);

    }
}
