package com.tuktuk.core.passenger;

import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.core.passenger.dto.PassengerResponse;
import com.tuktuk.core.passenger.dto.PassengerUpdateRequest;
import com.tuktuk.domain.passenger.Passenger;
import com.tuktuk.domain.passenger.PassengerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    @Transactional(readOnly = true)
    public List<PassengerResponse> findAll() {
        return passengerRepository.findAll().stream()
                .map(PassengerMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PassengerResponse findById(Long id) {
        return PassengerMapper.toResponse(getPassengerOrThrow(id));
    }

    @Transactional
    public PassengerResponse update(Long id, PassengerUpdateRequest request) {
        Passenger passenger = getPassengerOrThrow(id);
        passenger.setFullName(request.getFullName());
        passenger.setPhoneNumber(request.getPhoneNumber());
        return PassengerMapper.toResponse(passengerRepository.save(passenger));
    }

    @Transactional
    public void delete(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Passenger not found with id: " + id);
        }
        passengerRepository.deleteById(id);
    }

    private Passenger getPassengerOrThrow(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
    }

}
