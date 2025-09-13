package com.ead.course.clients;

import com.ead.course.dtos.UserPageDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

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
}
