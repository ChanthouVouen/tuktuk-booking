package com.tuktuk.passenger.config;

import com.tuktuk.domain.entity.VehicleType;
import com.tuktuk.domain.repository.VehicleTypeRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class VehicleTypeDataInitializer {

    private final VehicleTypeRepository vehicleTypeRepository;

    @Bean
    CommandLineRunner seedVehicleTypes() {
        return args -> {
            createIfMissing("STANDARD", new BigDecimal("2.50"));
            createIfMissing("PREMIUM", new BigDecimal("4.00"));
        };
    }

    private void createIfMissing(String typeName, BigDecimal price) {
        if (vehicleTypeRepository.findByTypeNameIgnoreCase(typeName).isEmpty()) {
            vehicleTypeRepository.save(VehicleType.builder()
                    .typeName(typeName)
                    .price(price)
                    .build());
        }
    }
}