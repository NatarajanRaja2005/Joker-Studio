package com.projoker.joker_studio.security.user_details;

import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.repository.UserRepository;
import com.projoker.joker_studio.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudioUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found!");
        }
        return StudioUserDetails.createUser(user);
    }
}
