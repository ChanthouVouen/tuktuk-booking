package com.tuktuk.domain.booking;

import com.tuktuk.domain.common.BaseEntity;
import com.tuktuk.domain.driver.Driver;
import com.tuktuk.domain.passenger.Passenger;
import com.tuktuk.domain.vehicletype.VehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, of = "id")
public class Booking extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "pickup_lat", nullable = false, precision = 10, scale = 7)
    private BigDecimal pickupLat;

    @Column(name = "pickup_long", nullable = false, precision = 10, scale = 7)
    private BigDecimal pickupLong;

    @Column(name = "drop_lat", nullable = false, precision = 10, scale = 7)
    private BigDecimal dropLat;

    @Column(name = "drop_long", nullable = false, precision = 10, scale = 7)
    private BigDecimal dropLong;

    @Column(name = "distance_km", nullable = false, precision = 10, scale = 3)
    private BigDecimal distanceKm;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_type_id", nullable = false)
    private VehicleType vehicleType;

}
