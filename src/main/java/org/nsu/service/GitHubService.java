package org.nsu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.List;

@Service
public class GitHubService {

    @Value("${github.api.base-url:https://api.github.com}")
    private String githubApiBaseUrl;

    private final RestTemplate restTemplate;

    public GitHubService() {
        this.restTemplate = new RestTemplate();
    }

    public String getUserLogin(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            return oauth2User.getAttribute("login");
        }
        return null;
    }

    public String getUserEmail(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            return oauth2User.getAttribute("email");
        }
        return null;
    }

    public String getUserAvatarUrl(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            return oauth2User.getAttribute("avatar_url");
        }
        return null;
    }

    public List<Map<String, Object>> getUserRepositories(Authentication authentication) {
        String accessToken = getAccessToken(authentication);
        if (accessToken == null) {
            return List.of();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List> response = restTemplate.exchange(
                    githubApiBaseUrl + "/user/repos",
                    HttpMethod.GET,
                    entity,
                    List.class
            );
            return response.getBody();
        } catch (Exception e) {
            return List.of();
        }
    }

    public Map<String, Object> createRepository(Authentication authentication, String repositoryName, String description) {
        String accessToken = getAccessToken(authentication);
        if (accessToken == null) {
            return Map.of("error", "No access token available");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = Map.of(
                "name", repositoryName,
                "description", description != null ? description : "SQL Playground Repository",
                "private", false,
                "auto_init", true
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    githubApiBaseUrl + "/user/repos",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
            return response.getBody();
        } catch (Exception e) {
            return Map.of("error", "Failed to create repository: " + e.getMessage());
        }
    }

    public Map<String, Object> commitFile(Authentication authentication, String owner, String repo, 
                                         String path, String content, String commitMessage) {
        String accessToken = getAccessToken(authentication);
        if (accessToken == null) {
            return Map.of("error", "No access token available");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = Map.of(
                "message", commitMessage,
                "content", java.util.Base64.getEncoder().encodeToString(content.getBytes()),
                "branch", "main"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    String.format("%s/repos/%s/%s/contents/%s", githubApiBaseUrl, owner, repo, path),
                    HttpMethod.PUT,
                    entity,
                    Map.class
            );
            return response.getBody();
        } catch (Exception e) {
            return Map.of("error", "Failed to commit file: " + e.getMessage());
        }
    }

    private String getAccessToken(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            return oauth2User.getAttribute("access_token");
        }
        return null;
    }
}