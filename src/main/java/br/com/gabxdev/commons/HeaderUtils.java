package br.com.gabxdev.commons;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class HeaderUtils {

    public URI createHeaderLocation(String sourceId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sourceId)
                .toUri();
    }

}
