package com.extend.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class TestUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    public static String toJson(Object target) {
        if (target == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(target);
        } catch (Exception e) {
            log.error("TestUtils: exception while converting data to json");
        }
        return null;
    }

    public static <T> T getObjectFromJsonFile(String fileName, Class<T> clazz) {
        try {
            return objectMapper.readValue(Thread.currentThread().
                    getContextClassLoader().getResourceAsStream(fileName), clazz);
        } catch (Exception ex) {
            log.error("TestUtil: unable to convert json to object {}", ex);
        }
        return null;
    }

    public static String getJsonString(String fileName) {
        try {
            JsonNode jsonNode = objectMapper.readValue(Thread.currentThread().
                            getContextClassLoader().getResourceAsStream(fileName) ,
                    JsonNode.class);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            log.error("TestUtils: exception while converting file to json string");
        }
        return null;
    }
}
