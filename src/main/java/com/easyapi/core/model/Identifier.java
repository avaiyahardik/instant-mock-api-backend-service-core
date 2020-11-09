package com.easyapi.core.model;

import com.easyapi.core.enums.AttributeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document
@Data
public class Identifier {
    @Id
    private String _id;

    private long id;

    private String resource;

    private Date createdAt = new Date();

    private Map<String, AttributeType> attributes;
    // Types: String, Number, Boolean, Object, Array, Date

    private String identifierUrl;
    private String generateDataUrl;

}
