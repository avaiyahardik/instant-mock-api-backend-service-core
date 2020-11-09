package com.easyapi.core.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class MockObject {
    private String key1 = "value1";
    private String key2 = "value2";
    private String key3 = "value3";

    @JsonIgnore
    private static MockObject _instance;

    private MockObject() {

    }

    public static MockObject getMockObject() {
        if (_instance == null)
            _instance = new MockObject();
        return _instance;
    }

    private static String[] mockArray = {"One", "Two", "Three"};

    public static String[] getMockArray() {
        return mockArray;
    }

}
