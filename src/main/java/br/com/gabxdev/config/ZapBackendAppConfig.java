package br.com.gabxdev.config;

import br.com.gabxdev.properties.ZapBackendProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableConfigurationProperties({ZapBackendProperties.class})
public class ZapBackendAppConfig {
}
