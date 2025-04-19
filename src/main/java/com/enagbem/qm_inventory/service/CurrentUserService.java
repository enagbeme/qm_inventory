package com.enagbem.qm_inventory.service;

import com.enagbem.qm_inventory.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            // Assuming your UserDetails implementation provides a method to get the full User object
            return (User) userDetails;
        } else {
            return null;
        }
    }
}
