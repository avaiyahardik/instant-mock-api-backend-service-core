package com.easyapi.core.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document
@NoArgsConstructor
public class DynaModel {
    @Id
    @Getter
    @Setter
    @JsonIgnore
    private String _id; // unique identifier

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    @JsonIgnore
    private String resource;

    // and then "other" stuff:
    protected Map<String, Object> other = new HashMap<>();

    public DynaModel(String resource) {
        this.resource = resource;
    }

    public Object get(String name) {
        return other.get(name);
    }

    // "any getter" needed for serialization
    @JsonAnyGetter
    public Map<String, Object> any() {
        return other;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        other.put(name, value);
    }
}
