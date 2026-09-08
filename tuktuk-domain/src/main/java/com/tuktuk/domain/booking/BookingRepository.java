package com.tuktuk.domain.booking;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select booking from Booking booking where booking.id = :id")
	Optional<Booking> findByIdForUpdate(@Param("id") Long id);

}
