package br.com.gabxdev.commons;

import java.util.List;

public class Constants {
    public static final List<String> WHITE_LIST = List.of("/v1/auth/**", "auth", "/zapbackend-ws/**", "/zapbackend-ws");

    public static final Long NEW_USER = 0L;

    public static final String BEARER_PREFIX = "Bearer ";
}
