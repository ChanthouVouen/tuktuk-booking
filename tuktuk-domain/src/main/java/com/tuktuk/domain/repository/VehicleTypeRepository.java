package com.tuktuk.domain.repository;

import com.tuktuk.domain.entity.VehicleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    Optional<VehicleType> findByTypeNameIgnoreCase(String typeName);

}
