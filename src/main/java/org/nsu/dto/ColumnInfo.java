package org.nsu.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Column metadata information")
public class ColumnInfo {

    @Schema(description = "Column name", example = "user_id")
    private String name;

    @Schema(description = "Column data type", example = "INTEGER")
    private String type;

    @Schema(description = "Whether the column allows null values", example = "false")
    private boolean nullable;

    @Schema(description = "Column size/length", example = "255")
    private Integer size;

    @Schema(description = "Column precision for numeric types", example = "10")
    private Integer precision;

    @Schema(description = "Column scale for numeric types", example = "2")
    private Integer scale;

    // Constructors
    public ColumnInfo() {}

    public ColumnInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ColumnInfo(String name, String type, boolean nullable) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", nullable=" + nullable +
                ", size=" + size +
                ", precision=" + precision +
                ", scale=" + scale +
                '}';
    }
}