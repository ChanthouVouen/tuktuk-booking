package com.tuktuk.passenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * scanBasePackages/EntityScan/EnableJpaRepositories point at com.tuktuk so that
 * beans, entities, and repositories defined in tuktuk-common/-domain/-core are
 * picked up even though they live outside this application's own package.
 */
@SpringBootApplication(scanBasePackages = "com.tuktuk")
@EntityScan(basePackages = "com.tuktuk.domain")
@EnableJpaRepositories(basePackages = "com.tuktuk.domain")
public class TuktukApiPassengerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TuktukApiPassengerApplication.class, args);
    }

}
