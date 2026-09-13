package com.tuktuk.core.service.serviceimpl;

import com.tuktuk.core.service.DriverUserDetailsService;
import com.tuktuk.domain.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("driverUserDetailsService")
@RequiredArgsConstructor
public class DriverUserDetailsServiceImpl implements DriverUserDetailsService {

    private final DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return driverRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found with email: " + email));
    }

}
