package com.ead.course.dtos;

import com.ead.course.models.UserModel;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public record UserEventDto(
        UUID userId,
        String username,
        String email,
        String fullName,
        String userStatus,
        String userType,
        String phoneNumber,
        String imageUrl,
        String actionType
) {
    public UserModel convertToUserModel() {
        var newUserModel = new UserModel();
        BeanUtils.copyProperties(this, newUserModel);
        return newUserModel;
    }
}