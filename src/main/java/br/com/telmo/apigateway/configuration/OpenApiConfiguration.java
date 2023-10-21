package br.com.telmo.apigateway.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class OpenApiConfiguration {

    @Bean
    @Lazy(false)
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters,
            RouteDefinitionLocator routeDefinitionLocator) {

        var definitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();

        List<GroupedOpenApi> apis = definitions.stream()
                .filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
                .map(routeDefinition -> {
                    String name = routeDefinition.getId();
                    swaggerUiConfigParameters.addGroup(name);
                    return GroupedOpenApi.builder()
                            .pathsToMatch("/" + name + "/**")
                            .group(name)
                            .build();
                })
                .collect(Collectors.toList());

        return apis;

    }



}
