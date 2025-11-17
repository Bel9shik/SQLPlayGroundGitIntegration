package org.nsu.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "SQL query response object")
public class QueryResponse {

    @Schema(description = "Indicates if the query was executed successfully", example = "true")
    private boolean success;

    @Schema(description = "Response message or error description", example = "Query executed successfully")
    private String message;

    @Schema(description = "Query execution time in milliseconds", example = "125")
    private Long executionTime;

    @Schema(description = "Number of rows affected by the query", example = "10")
    private Integer rowsAffected;

    @Schema(description = "Query result data as list of maps")
    private List<Map<String, Object>> data;

    @Schema(description = "Column metadata information")
    private List<ColumnInfo> columns;

    @Schema(description = "Timestamp when the query was executed")
    private LocalDateTime timestamp;

    @Schema(description = "Error code if query failed", example = "SQL001")
    private String errorCode;

    // Constructors
    public QueryResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public QueryResponse(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    // Builder pattern
    public static QueryResponseBuilder builder() {
        return new QueryResponseBuilder();
    }

    public static class QueryResponseBuilder {
        private boolean success;
        private String message;
        private Long executionTime;
        private Integer rowsAffected;
        private List<Map<String, Object>> data;
        private List<ColumnInfo> columns;
        private LocalDateTime timestamp;
        private String errorCode;

        public QueryResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public QueryResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public QueryResponseBuilder executionTime(Long executionTime) {
            this.executionTime = executionTime;
            return this;
        }

        public QueryResponseBuilder rowsAffected(Integer rowsAffected) {
            this.rowsAffected = rowsAffected;
            return this;
        }

        public QueryResponseBuilder data(List<Map<String, Object>> data) {
            this.data = data;
            return this;
        }

        public QueryResponseBuilder columns(List<ColumnInfo> columns) {
            this.columns = columns;
            return this;
        }

        public QueryResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public QueryResponseBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public QueryResponse build() {
            QueryResponse response = new QueryResponse();
            response.success = this.success;
            response.message = this.message;
            response.executionTime = this.executionTime;
            response.rowsAffected = this.rowsAffected;
            response.data = this.data;
            response.columns = this.columns;
            response.timestamp = this.timestamp != null ? this.timestamp : LocalDateTime.now();
            response.errorCode = this.errorCode;
            return response;
        }
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Integer getRowsAffected() {
        return rowsAffected;
    }

    public void setRowsAffected(Integer rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "QueryResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", executionTime=" + executionTime +
                ", rowsAffected=" + rowsAffected +
                ", timestamp=" + timestamp +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}