package com.tuktuk.core.service.serviceimpl;

import com.tuktuk.common.dto.PassengerResponse;
import com.tuktuk.common.dto.PassengerUpdateRequest;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.core.mapper.PassengerMapper;
import com.tuktuk.core.service.PassengerService;
import com.tuktuk.domain.entity.Passenger;
import com.tuktuk.domain.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse findById(Long id) {
        return PassengerMapper.toResponse(getPassengerOrThrow(id));
    }

    @Override
    @Transactional
    public PassengerResponse update(Long id, PassengerUpdateRequest request) {
        Passenger passenger = getPassengerOrThrow(id);
        passenger.setFullName(request.getFullName());
        passenger.setPhoneNumber(request.getPhoneNumber());
        return PassengerMapper.toResponse(passengerRepository.save(passenger));
    }

    private Passenger getPassengerOrThrow(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
    }

}
