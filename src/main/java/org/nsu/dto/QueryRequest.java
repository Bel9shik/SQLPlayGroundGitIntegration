package org.nsu.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;

@Schema(description = "SQL query request object")
public class QueryRequest {

    @Schema(description = "SQL query string to execute", example = "SELECT * FROM users WHERE age > ?")
    @NotBlank(message = "Query cannot be empty")
    @Size(max = 10000, message = "Query cannot exceed 10000 characters")
    private String query;

    @Schema(description = "Query parameters for prepared statements", example = "{\"1\": \"25\"}")
    private Map<String, Object> parameters;

    @Schema(description = "Maximum number of rows to return", example = "100")
    private Integer limit;

    @Schema(description = "Query timeout in seconds", example = "30")
    private Integer timeout;

    // Constructors
    public QueryRequest() {}

    public QueryRequest(String query) {
        this.query = query;
    }

    public QueryRequest(String query, Map<String, Object> parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    // Getters and Setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "QueryRequest{" +
                "query='" + query + '\'' +
                ", parameters=" + parameters +
                ", limit=" + limit +
                ", timeout=" + timeout +
                '}';
    }
}