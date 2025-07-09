package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchUserDTO;
import com.rental_car_project_backend.car.rental.dto.response.user.UserResponse;
import com.rental_car_project_backend.car.rental.entity.Users;
import com.rental_car_project_backend.car.rental.repository.UserRepository;
import com.rental_car_project_backend.car.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public Page<UserResponse> getAllUsers(PageRequestDTO pageRequestDTO, SearchUserDTO searchUserDTO) {
        List<Predicate> predicates = new ArrayList<>();
        Specification<Users>
                userSpecification = (root, query, criteriaBuilder) -> {
            if(Objects.nonNull(searchUserDTO.getFullName())){
                Predicate fullName = criteriaBuilder
                        .like(root.get("fullName"), "%" + searchUserDTO.getFullName() + "%");
                predicates.add(fullName);
            }
            if(Objects.nonNull(searchUserDTO.getEmail())){
                Predicate email = criteriaBuilder.like(root.get("email"), searchUserDTO.getEmail());
                predicates.add(email);
            }
            if(Objects.nonNull(searchUserDTO.getAccountNumber())){
                Predicate accountNumber = criteriaBuilder.equal(root.get("accountNumber"), searchUserDTO.getAccountNumber());
                predicates.add(accountNumber);
            }
            if(Objects.nonNull(searchUserDTO.getCity())){
                Predicate city = criteriaBuilder.like(root.join("idCity").get("name"), searchUserDTO.getCity());
                predicates.add(city);
            }
            assert query != null;
            return query.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{}))).getRestriction();
        };

        Sort sort = Sort.by(pageRequestDTO.getSort(), pageRequestDTO.getSortColumn());
        Pageable pageable =
                PageRequest.of(Integer.parseInt(pageRequestDTO.getPageNo()),
                        Integer.parseInt(pageRequestDTO.getPageSize()), sort);
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
                .idCity(val.getIdCity())
                .idRole(val.getIdRole())
                .phoneNumber(val.getPhoneNumber())
                .build());
    }
}
