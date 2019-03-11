package com.mycompany.myapp.service.dto;


import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Intent entity.
 */
public class IntentDTO implements Serializable {

    private String id;

    @NotNull
    private String tag;
    private List<String> patterns;
    private List<String> responses;
    private String context_set;
    private String context_filter;

    public List<String> getPatterns() {
        return patterns;
    }

    public String getContext_set() {
        return context_set;
    }

    public void setContext_set(String context_set) {
        this.context_set = context_set;
    }

    public String getContext_filter() {
        return context_filter;
    }

    public void setContext_filter(String context_filter) {
        this.context_filter = context_filter;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    public List<String> getResponses() {
        return responses;
    }

    public void setResponses(List<String> responses) {
        this.responses = responses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IntentDTO intentDTO = (IntentDTO) o;
        if(intentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), intentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IntentDTO{" +
            "id=" + getId() +
            ", tag='" + getTag() + "'" +
            "}";
    }
}
