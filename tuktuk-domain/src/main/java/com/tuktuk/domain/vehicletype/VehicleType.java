package com.tuktuk.domain.vehicletype;

import com.tuktuk.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicle_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, of = "id")
public class VehicleType extends BaseEntity {

    @Column(name = "type_name", nullable = false, unique = true, length = 50)
    private String typeName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

}
