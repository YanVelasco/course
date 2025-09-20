package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.dtos.UserPageDto;
import com.ead.course.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AuthUserClient {

    Logger logger = LogManager.getLogger(AuthUserClient.class);

    final RestClient restClient;

    @Value("${ead.api.url.authuser}")
    String BASE_URL_AUTHUSER;

    public AuthUserClient(RestClient.Builder restClient) {
        this.restClient = restClient.build();
    }

    public UserPageDto getAllUsersByCourse(UUID courseId, Pageable pageable) {
        String url = BASE_URL_AUTHUSER + "/users?courseId=" + courseId +
                "&page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replace(": ", ",");
        logger.debug("Request URL: {}", url);
        try {
            return restClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .body(UserPageDto.class);
        } catch (Exception e) {
            logger.error("Error fetching users by course {}: {}", courseId, e.getMessage());
            return null;
        }
    }

    public ResponseEntity<UserDto> getOneUserByUserId(UUID userId) {
        String url = BASE_URL_AUTHUSER + "/users/" + userId;
        logger.debug("Request URL: {}", url);
        return restClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.value() == 404, ((request, response) -> {
                    logger.warn("404 Not Found: {}", userId);
                    throw new NotFoundException("User not found.");
                }))
                .toEntity(UserDto.class);

    }

    public void saveSubscriptionUserInCourse(UUID userId, UUID courseId) {
        String url = BASE_URL_AUTHUSER + "/users/" + userId + "/courses/subscription";
        logger.debug("Request URL: {}", url);

        try{
            var courseUserDto = new CourseUserDto(userId, courseId);
            restClient
                    .post()
                    .uri(url)
                    .contentType(APPLICATION_JSON)
                    .body(courseUserDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            logger.error("Error saving subscription for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Error saving subscription for user.", e);
        }

    }

}
