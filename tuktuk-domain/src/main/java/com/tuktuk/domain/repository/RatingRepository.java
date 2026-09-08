package com.tuktuk.domain.repository;

import com.tuktuk.domain.entity.Rating;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    boolean existsByBookingId(Long bookingId);

    Optional<Rating> findByPassengerIdOrderByCreatedAtDesc(Long passengerId);
}
