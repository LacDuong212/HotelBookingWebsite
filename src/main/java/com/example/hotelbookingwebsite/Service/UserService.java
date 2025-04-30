package com.example.hotelbookingwebsite.Service;

import com.example.hotelbookingwebsite.DTO.UserDTO;
import com.example.hotelbookingwebsite.Model.User;
import com.example.hotelbookingwebsite.Repository.CustomerRepository;
import com.example.hotelbookingwebsite.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<UserDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> updateUsers(List<UserDTO> userDTOList) {
        return userDTOList.stream()
                .map(userDTO -> {
                    Optional<User> userOptional = userRepository.findById(userDTO.getId());

                    if (userOptional.isPresent()) {
                        User user = userOptional.get();

                        if (userDTO.getEmail() != null) {
                            user.setEmail(userDTO.getEmail());
                        }
                        if (userDTO.getFullname() != null) {
                            user.setFullname(userDTO.getFullname());
                        }
                        if (userDTO.getPhoneNumber() != null) {
                            user.setPhoneNumber(userDTO.getPhoneNumber());
                        }
                        if (userDTO.getRole() != null) {
                            user.setRole(userDTO.getRole().toUpperCase());
                        }

                        userRepository.save(user);

                        return convertUserToUserDTO(user);
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            customerRepository.deleteByUid(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private UserDTO convertUserToUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userListDTO = new UserDTO();
        userListDTO.setId(user.getUid());
        userListDTO.setEmail(user.getEmail());
        userListDTO.setFullname(user.getFullname());
        userListDTO.setPhoneNumber(user.getPhoneNumber());
        userListDTO.setRole(user.getRole());

        return userListDTO;
    }
}
