package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchUserDTO;
import com.rental_car_project_backend.car.rental.dto.request.user.UpdateUserRequest;
import com.rental_car_project_backend.car.rental.dto.response.user.UpdateUserResponse;
import com.rental_car_project_backend.car.rental.dto.response.user.UserResponse;
import com.rental_car_project_backend.car.rental.entity.Address;
import com.rental_car_project_backend.car.rental.entity.Cities;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.UserService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(PageRequestDTO pageRequestDTO, SearchUserDTO searchUserDTO) {
        List<Predicate> predicates = new ArrayList<>();
        Specification<Users>
                userSpecification = (root, query, criteriaBuilder) -> {
            if(searchUserDTO.getFullName() != null && !searchUserDTO.getFullName().isBlank()){
                Predicate fullName = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("fullName")), "%" +
                                searchUserDTO.getFullName().toLowerCase() + "%");
                predicates.add(fullName);
            }
            if(searchUserDTO.getEmail() != null && !searchUserDTO.getEmail().isBlank()){
                Predicate email = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                        "%" + searchUserDTO.getEmail().toLowerCase() + "%");
                predicates.add(email);
            }
            if(searchUserDTO.getAccountNumber() != null && !searchUserDTO.getAccountNumber().isBlank()){
                Predicate accountNumber = criteriaBuilder.equal(root.get("accountNumber"),
                        searchUserDTO.getAccountNumber());
                predicates.add(accountNumber);
            }
            if(searchUserDTO.getCityName() != null && !searchUserDTO.getCityName().isBlank()){
                Join<Users, Address> address = root.join("addresses", JoinType.RIGHT);
                Join<Address, Cities> city1 = address.join("city", JoinType.RIGHT);
                Predicate city = criteriaBuilder.like(criteriaBuilder
                                .lower(city1.get("name")),
                        "%" + searchUserDTO.getCityName() + "%");
                predicates.add(city);
            }
            assert query != null;
            return query.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{}))).getRestriction();
        };
        Sort sort = Sort.by(pageRequestDTO.getSort(), pageRequestDTO.getSortColumn());
        Pageable pageable = PageRequest.of(Integer.parseInt(pageRequestDTO
                        .getPageNo()), Integer.parseInt(pageRequestDTO.getPageSize()),
                sort);
        Page<Users> all = userRepository.findAll(userSpecification, pageable);
        return all.map(val -> UserResponse.builder()
                .id(val.getId())
                .accountNumber(val.getAccountNumber())
                .bankCode(val.getBankCode())
                .birthDate(val.getBirthDate())
                .createdAt(val.getCreatedAt())
                .updatedAt(val.getUpdatedAt())
                .email(val.getEmail())
                .fullName(val.getFullName())
                .idRole(val.getIdRole())
                .phoneNumber(val.getPhoneNumber())
                .build());
    }

    @Override
    @Transactional
    public UpdateUserResponse updateUser(UpdateUserRequest request) {
        boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if(!authenticated){
            throw new SecurityException("You must logged in first!");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found with email " + email));
        if(Objects.nonNull(request.getAccountNumber())){
            users.setAccountNumber(request.getAccountNumber());
        }
        if(Objects.nonNull(request.getBankCode())){
            users.setBankCode(request.getBankCode());
        }
        if(Objects.nonNull(request.getBirthDate())){
            users.setBirthDate(request.getBirthDate());
        }
        if(Objects.nonNull(request.getFullName())){
            users.setFullName(request.getFullName());
        }
        if(Objects.nonNull(request.getPhoneNumber())){
            users.setPhoneNumber(request.getAccountNumber());
        }
        users.setUpdatedAt(LocalDateTime.now());
        Users save = userRepository.save(users);
        return UpdateUserResponse.builder()
                .accountNumber(save.getAccountNumber())
                .birthDate(save.getBirthDate())
                .phoneNumber(save.getPhoneNumber())
                .bankCode(save.getBankCode())
                .id(save.getId())
                .createdAt(save.getCreatedAt())
                .email(save.getEmail())
                .fullName(save.getFullName())
                .build();
    }
}
