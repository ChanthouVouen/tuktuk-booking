package com.tuktuk.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tuktuk.common.exception.InvalidStateException;
import com.tuktuk.domain.entity.Booking;
import com.tuktuk.domain.entity.Driver;
import com.tuktuk.domain.entity.Passenger;
import com.tuktuk.domain.entity.VehicleType;
import com.tuktuk.domain.enums.BookingStatus;
import com.tuktuk.domain.repository.BookingRepository;
import com.tuktuk.domain.repository.DriverRepository;
import com.tuktuk.domain.repository.NotificationRepository;
import com.tuktuk.domain.repository.PassengerRepository;
import com.tuktuk.domain.repository.VehicleTypeRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private Passenger passenger;

    @Mock
    private VehicleType vehicleType;

    private BookingService bookingService;
    private Driver driver;
    private Booking booking;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(
            bookingRepository, driverRepository, passengerRepository, vehicleTypeRepository, notificationRepository);
        driver = Driver.builder().build();
        driver.setId(7L);
        booking = Booking.builder()
                .passenger(passenger)
                .pickupLat(BigDecimal.valueOf(11.5))
                .pickupLong(BigDecimal.valueOf(104.9))
                .dropLat(BigDecimal.valueOf(11.6))
                .dropLong(BigDecimal.valueOf(105.0))
                .distanceKm(BigDecimal.ONE)
                .price(BigDecimal.TEN)
                .status(BookingStatus.PENDING)
                .vehicleType(vehicleType)
                .build();
        booking.setId(42L);
        when(driverRepository.findById(7L)).thenReturn(Optional.of(driver));
        when(bookingRepository.findByIdForUpdate(42L)).thenReturn(Optional.of(booking));
    }

    @Test
    void acceptAssignsDriverAndChangesStatus() {
        when(passenger.getId()).thenReturn(3L);
        when(vehicleType.getId()).thenReturn(1L);
        when(vehicleType.getTypeName()).thenReturn("STANDARD");
        when(bookingRepository.save(booking)).thenReturn(booking);

        var response = bookingService.accept(7L, 42L);

        assertThat(booking.getDriver()).isSameAs(driver);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.ACCEPTED);
        assertThat(response.getDriver().getId()).isEqualTo(7L);
        assertThat(response.getStatus()).isEqualTo(BookingStatus.ACCEPTED.name());
        verify(bookingRepository).save(booking);
    }

    @Test
    void acceptRejectsBookingThatIsNotPending() {
        booking.setStatus(BookingStatus.ACCEPTED);

        assertThatThrownBy(() -> bookingService.accept(7L, 42L))
                .isInstanceOf(InvalidStateException.class)
                .hasMessage("Booking can only be accepted while it is pending");
    }

}
