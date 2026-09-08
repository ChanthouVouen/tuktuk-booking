package com.tuktuk.domain.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByPassengerId(Long passengerId);

    Optional<Booking> findByIdAndPassengerId(Long id, Long passengerId);
}
