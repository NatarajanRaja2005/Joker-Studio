package com.projoker.joker_studio.service.user;

import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.request.AddUserRequest;
import com.projoker.joker_studio.request.UpdateUserRequest;

public interface IUserService {
    //For function like when user is created we should have to
    // create cart at that simultaneous time
    void createUser(AddUserRequest user);

    User verifyUserEmail(String email, String password);

    User updateUserDetails(Long userId, UpdateUserRequest user);
    User getUser(Long userId);
    void deleteUser(Long userId);
    User getUserByEmail(String email);

    User getUserByPhone(Long phone);

    User verifyUserPhone(Long phone, String password);
}
