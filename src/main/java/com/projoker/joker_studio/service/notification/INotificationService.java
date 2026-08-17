package com.projoker.joker_studio.service.notification;

import com.projoker.joker_studio.model.NotifyMessage;
import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.response.ApiResponse;

public interface INotificationService {
    void notify(User user, NotifyMessage message);
    void optVerification(String email,String message);
}
