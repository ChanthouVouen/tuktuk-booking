package com.tuktuk.driver.controller;

import com.tuktuk.common.dto.RatingResponse;
import com.tuktuk.core.service.RatingService;
import com.tuktuk.domain.entity.Driver;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Tag(name = "Ratings")
@SecurityRequirement(name = "bearerAuth")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Get ratings the current driver has received from passengers")
    public ResponseEntity<List<RatingResponse>> findMine(@AuthenticationPrincipal Driver driver) {
        return ResponseEntity.ok(ratingService.findForDriver(driver.getId()));
    }
}
