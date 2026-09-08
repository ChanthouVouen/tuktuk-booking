package com.tuktuk.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VehicleResponse {

    private Long id;
    private String name;
    private String type;
    private Long driverId;
}
