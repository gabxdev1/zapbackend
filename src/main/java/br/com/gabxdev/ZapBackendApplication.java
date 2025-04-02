package br.com.gabxdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ZapBackendApplication {

    static {
        initTimeZoneConfig();
    }

    public static void main(String[] args) {
        SpringApplication.run(ZapBackendApplication.class, args);
    }

    private static void initTimeZoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.setProperty("user.timezone", "UTC");
    }
}
