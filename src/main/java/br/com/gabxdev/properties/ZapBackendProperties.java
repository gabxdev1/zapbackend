package br.com.gabxdev.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "zap-backend")
@Component
public class ZapBackendProperties {

    @Valid
    private Jwt jwt;

    @Getter
    @Setter
    public static class Jwt {
        @NotBlank
        private String secretKey;

        @NotNull
        private long expirationSeconds;
    }
}
