package com.extend.infrastructure.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendAuthConfig {
    @Value("${security.extend.api.url}")
    private String baseUrl;

    @Value("${security.extend.api.version}")
    private String apiVersion;
}
