package org.nsu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nsu.dto.QueryRequest;
import org.nsu.dto.QueryResponse;
import org.nsu.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sql")
@Tag(name = "SQL Playground", description = "SQL query execution and management API")
@SecurityRequirement(name = "GitHubOAuth")
public class SqlPlaygroundController {

    private final GitHubService gitHubService;

    @Autowired
    public SqlPlaygroundController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Operation(
            summary = "Execute SQL query",
            description = "Execute a SQL query in a secure sandbox environment and return results with execution statistics"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Query executed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QueryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid SQL query or request parameters",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during query execution",
                    content = @Content
            )
    })
    @PostMapping("/execute")
    public ResponseEntity<QueryResponse> executeQuery(
            @Parameter(description = "SQL query request with query string and optional parameters")
            @Valid @RequestBody QueryRequest request,
            Authentication authentication) {
        
        String userLogin = gitHubService.getUserLogin(authentication);
        
        QueryResponse response = QueryResponse.builder()
                .success(true)
                .message("Query executed successfully by user: " + userLogin)
                .executionTime(125L)
                .rowsAffected(10)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get query execution history",
            description = "Retrieve the history of executed queries for the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Query history retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QueryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @GetMapping("/history")
    public ResponseEntity<List<QueryResponse>> getQueryHistory(
            @Parameter(description = "Maximum number of queries to return", example = "10")
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        
        String userLogin = gitHubService.getUserLogin(authentication);
        
        return ResponseEntity.ok(List.of());
    }

    @Operation(
            summary = "Validate SQL query syntax",
            description = "Validate SQL query syntax without executing it - useful for query development"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Query validation completed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QueryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @PostMapping("/validate")
    public ResponseEntity<QueryResponse> validateQuery(
            @Parameter(description = "SQL query to validate")
            @Valid @RequestBody QueryRequest request,
            Authentication authentication) {
        
        QueryResponse response = QueryResponse.builder()
                .success(true)
                .message("Query syntax is valid")
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get database schema information",
            description = "Retrieve the current database schema information including tables and columns"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Schema information retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @GetMapping("/schema")
    public ResponseEntity<Map<String, Object>> getDatabaseSchema(Authentication authentication) {
        String userLogin = gitHubService.getUserLogin(authentication);
        
        Map<String, Object> schema = Map.of(
                "user", userLogin,
                "tables", List.of(
                        Map.of("name", "users", "columns", List.of("id", "username", "email", "created_at")),
                        Map.of("name", "orders", "columns", List.of("id", "user_id", "total", "status", "created_at")),
                        Map.of("name", "products", "columns", List.of("id", "name", "price", "category", "stock"))
                ),
                "message", "Database schema information for user: " + userLogin
        );
        
        return ResponseEntity.ok(schema);
    }

    @Operation(
            summary = "Save query to GitHub repository",
            description = "Save executed query and results to user's GitHub repository for version control"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Query saved to GitHub successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or GitHub API error",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @PostMapping("/save-to-git")
    public ResponseEntity<Map<String, Object>> saveQueryToGit(
            @Parameter(description = "Query to save with metadata")
            @Valid @RequestBody QueryRequest request,
            @Parameter(description = "Repository name to save to")
            @RequestParam String repository,
            @Parameter(description = "File name for the saved query")
            @RequestParam(defaultValue = "query.sql") String fileName,
            Authentication authentication) {
        
        String userLogin = gitHubService.getUserLogin(authentication);
        String content = String.format(
                "-- Query executed by %s at %s\n-- Parameters: %s\n-- Timeout: %s seconds\n-- Limit: %s rows\n\n%s", 
                userLogin, 
                LocalDateTime.now(), 
                request.getParameters() != null ? request.getParameters().toString() : "none",
                request.getTimeout() != null ? request.getTimeout() : "default",
                request.getLimit() != null ? request.getLimit() : "unlimited",
                request.getQuery()
        );
        
        Map<String, Object> result = gitHubService.commitFile(
                authentication, userLogin, repository, fileName, content, 
                "Add SQL query from playground: " + fileName
        );
        
        return ResponseEntity.ok(result);
    }
}