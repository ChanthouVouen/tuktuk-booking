package com.tuktuk.domain.repository;

import com.tuktuk.domain.entity.Vehicle;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	Optional<Vehicle> findByDriverId(Long driverId);
}
