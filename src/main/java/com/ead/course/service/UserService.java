package com.ead.course.service;

import com.ead.course.dtos.UserPageDto;
import com.ead.course.models.UserModel;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserPageDto findUserByCourse(UUID courseId, Pageable pageable, String name, String fullName, String userStatus, String userType);

    UserModel save(UserModel userModel);

}
