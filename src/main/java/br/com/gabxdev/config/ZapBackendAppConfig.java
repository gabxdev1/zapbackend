package br.com.gabxdev.config;

import br.com.gabxdev.properties.ZapBackendProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ZapBackendProperties.class})
public class ZapBackendAppConfig {
}
