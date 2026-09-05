package com.tuktuk.core.driver;

import com.tuktuk.domain.driver.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Named explicitly because tuktuk-core also contains PassengerUserDetailsService; both
 * implement the same UserDetailsService interface, so each API app must @Qualifier
 * the one it wants when only one should be wired into its security filter chain.
 */
@Service("driverUserDetailsService")
@RequiredArgsConstructor
public class DriverUserDetailsService implements UserDetailsService {

    private final DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return driverRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found with email: " + email));
    }

}
