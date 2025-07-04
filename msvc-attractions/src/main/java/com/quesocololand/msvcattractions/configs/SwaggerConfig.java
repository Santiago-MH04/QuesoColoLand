package com.quesocololand.msvcattractions.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "QuesoColoLand",
        description = "Amusement park management application",
        termsOfService = "Here you’ll find a fictional link",
        version = "1.0.0",
        contact = @Contact(
            name = "Santiago Marín Higuita",
            url = "Here you’ll find my portfolio link",
            email = "santimh04@gmail.com"
        )
    ),
    servers = {
        @Server(
            description = "DEV_SERVER",
            url = "http://127.0.0.1:8080"
        )
    }
)
public class SwaggerConfig {
    //Fields of SwaggerConfig
    //Constructors of SwaggerConfig
    //Field setters of SwaggerConfig (setters)
    //Field getters of SwaggerConfig (getters)
    //Methods of SwaggerConfig
}
