package com.tuktuk.core.passenger;

import com.tuktuk.domain.passenger.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Named explicitly because tuktuk-core also contains DriverUserDetailsService; both
 * implement the same UserDetailsService interface, so each API app must @Qualifier
 * the one it wants when only one should be wired into its security filter chain.
 */
@Service("passengerUserDetailsService")
@RequiredArgsConstructor
public class PassengerUserDetailsService implements UserDetailsService {

    private final PassengerRepository passengerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return passengerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Passenger not found with email: " + email));
    }

}
