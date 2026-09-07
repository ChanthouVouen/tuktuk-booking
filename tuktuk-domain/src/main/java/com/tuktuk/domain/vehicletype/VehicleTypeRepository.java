package com.tuktuk.domain.vehicletype;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    Optional<VehicleType> findByTypeNameIgnoreCase(String typeName);

}
