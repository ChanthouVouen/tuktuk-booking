package com.tuktuk.core.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Named explicitly because tuktuk-core also contains DriverUserDetailsService; both
 * extend the same UserDetailsService interface, so each API app must @Qualifier
 * the one it wants when only one should be wired into its security filter chain.
 */
public interface PassengerUserDetailsService extends UserDetailsService {
}
