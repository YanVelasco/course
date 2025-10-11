package com.ead.course.service.impls;

import com.ead.course.dtos.UserPageDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.UserModel;
import com.ead.course.repositories.UserRepository;
import com.ead.course.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
public class UserServiceImpl implements UserService {

    final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserPageDto findUserByCourse(UUID courseId, Pageable pageable, String name, String fullName,
                                        String userStatus, String userType) {
        List<Specification<UserModel>> specifications = new ArrayList<>();
        specifications.add((root, query, cb) -> {
            requireNonNull(query).distinct(true);
            return cb.equal(root.join("courses").get("courseId"), courseId);
        });
        if (name != null) {
            specifications.add((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() +
                    "%"));
        }
        if (fullName != null) {
            specifications.add((root, query, cb) -> cb.equal(root.get("fullName"), fullName));
        }
        if (userStatus != null) {
            specifications.add((root, query, cb) -> cb.equal(root.get("userStatus"), userStatus));
        }
        if (userType != null) {
            specifications.add((root, query, cb) -> cb.equal(root.get("userType"), userType));
        }
        Specification<UserModel> spec =
                specifications.stream().reduce(Specification::and).orElse((root, query, cb) -> cb.conjunction());
        var page = repository.findAll(spec, pageable);
        return UserPageDto.from(page);
    }

    @Transactional
    @Override
    public void save(UserModel userModel) {
        repository.save(userModel);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        repository.findById(userId).ifPresent(repository::delete);
    }

    @Override
    public UserModel findById(UUID userInstructor) {
        return repository.findById(userInstructor).orElseThrow(
                () -> new NotFoundException(
                        String.format("User with id %s not found", userInstructor)
                )
        );
    }
}
