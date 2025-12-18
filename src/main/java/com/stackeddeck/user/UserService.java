package com.stackeddeck.user;

import com.stackeddeck.user.dto.UserProfileDto;
import com.stackeddeck.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileDto getUserProfile(UUID userId) {
        User user = getUserById(userId);
        return mapToDto(user);
    }

    @Transactional
    public UserProfileDto updateUserProfile(UUID userId, UserProfileDto dto) {
        User user = getUserById(userId);


        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhone(dto.phone());

        user.setAddressLine1(dto.addressLine1());
        user.setAddressLine2(dto.addressLine2());
        user.setCity(dto.city());
        user.setCountry(dto.country());
        user.setPostalCode(dto.postalCode());


        User savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }

    private User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private UserProfileDto mapToDto(User user) {
        return new UserProfileDto(
                user.getEmail(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getAddressLine1(),
                user.getAddressLine2(),
                user.getCity(),
                user.getCountry(),
                user.getPostalCode()
        );
    }
}
