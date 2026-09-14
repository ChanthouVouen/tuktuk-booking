package com.tuktuk.core.service.serviceimpl;

import com.tuktuk.core.service.PassengerUserDetailsService;
import com.tuktuk.domain.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("passengerUserDetailsService")
@RequiredArgsConstructor
public class PassengerUserDetailsServiceImpl implements PassengerUserDetailsService {

    private final PassengerRepository passengerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return passengerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Passenger not found with email: " + email));
    }

}
