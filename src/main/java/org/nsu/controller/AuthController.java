package org.nsu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nsu.service.GitHubService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication & Git Integration", description = "User authentication and GitHub repository management")
@RequiredArgsConstructor
public class AuthController {

    private final GitHubService gitHubService;

    @Operation(
            summary = "Get current user profile",
            description = "Retrieve authenticated user's GitHub profile information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User profile retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "GitHubOAuth")
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(Authentication authentication) {
        String login = gitHubService.getUserLogin(authentication);
        String email = gitHubService.getUserEmail(authentication);
        String avatarUrl = gitHubService.getUserAvatarUrl(authentication);

        Map<String, Object> profile = Map.of(
                "login", login != null ? login : "unknown",
                "email", email != null ? email : "not provided",
                "avatar_url", avatarUrl != null ? avatarUrl : "",
                "authenticated", authentication != null && authentication.isAuthenticated()
        );

        return ResponseEntity.ok(profile);
    }

    @Operation(
            summary = "Get user's GitHub repositories",
            description = "Retrieve list of authenticated user's GitHub repositories"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Repositories retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient GitHub permissions",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "GitHubOAuth")
    @GetMapping("/repositories")
    public ResponseEntity<List<Map<String, Object>>> getUserRepositories(Authentication authentication) {
        List<Map<String, Object>> repositories = gitHubService.getUserRepositories(authentication);
        return ResponseEntity.ok(repositories);
    }

    @Operation(
            summary = "Create new GitHub repository",
            description = "Create a new repository in user's GitHub account for storing SQL queries"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Repository created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid repository name or GitHub API error",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient GitHub permissions",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "GitHubOAuth")
    @PostMapping("/repositories")
    public ResponseEntity<Map<String, Object>> createRepository(
            @Parameter(description = "Repository name", example = "sql-playground-queries")
            @RequestParam String name,
            @Parameter(description = "Repository description", example = "My SQL queries from playground")
            @RequestParam(required = false) String description,
            Authentication authentication) {

        Map<String, Object> result = gitHubService.createRepository(authentication, name, description);
        
        if (result.containsKey("error")) {
            return ResponseEntity.badRequest().body(result);
        }
        
        return ResponseEntity.status(201).body(result);
    }

    @Operation(
            summary = "Commit file to GitHub repository",
            description = "Save a file (SQL query) to specified GitHub repository"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "File committed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or GitHub API error",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient repository permissions",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Repository not found",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "GitHubOAuth")
    @PostMapping("/repositories/{owner}/{repo}/files")
    public ResponseEntity<Map<String, Object>> commitFile(
            @Parameter(description = "Repository owner (username)")
            @PathVariable String owner,
            @Parameter(description = "Repository name")
            @PathVariable String repo,
            @Parameter(description = "File path in repository", example = "queries/my-query.sql")
            @RequestParam String path,
            @Parameter(description = "File content")
            @RequestParam String content,
            @Parameter(description = "Commit message", example = "Add new SQL query")
            @RequestParam(defaultValue = "Add file from SQL Playground") String message,
            Authentication authentication) {

        Map<String, Object> result = gitHubService.commitFile(
                authentication, owner, repo, path, content, message
        );

        if (result.containsKey("error")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Check authentication status",
            description = "Verify if user is properly authenticated with GitHub OAuth"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication status retrieved"
            )
    })
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus(Authentication authentication) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        String userLogin = isAuthenticated ? gitHubService.getUserLogin(authentication) : null;

        Map<String, Object> status = Map.of(
                "authenticated", isAuthenticated,
                "user", userLogin != null ? userLogin : "anonymous",
                "provider", "GitHub OAuth2"
        );

        return ResponseEntity.ok(status);
    }
}
