package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchUserDTO;
import com.rental_car_project_backend.car.rental.dto.response.user.UserResponse;
import com.rental_car_project_backend.car.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(name = "fullName", defaultValue = "", required = false) String name,
            @RequestParam(name = "email", defaultValue = "", required = false) String email,
            @RequestParam(name = "city", defaultValue = "", required = false) String city,
            @RequestParam(name = "accountNumber", defaultValue = "", required = false) String accountNumber,
            @RequestParam(name = "sort", defaultValue = "ASC", required = false) Sort.Direction sort,
            @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(name = "bankCode", defaultValue = "", required = false) String bankCode,
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) String pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) String pageSize
    ){
        PageRequestDTO page = PageRequestDTO.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sort(sort)
                .sortColumn(sortBy)
                .build();
        SearchUserDTO searchUserDTO = SearchUserDTO.builder()
                .fullName(name)
                .accountNumber(accountNumber)
                .bankCode(bankCode)
                .cityName(city)
                .email(email)
                .cityName(city)
                .build();
        Page<UserResponse> allUsers = userService.getAllUsers(page, searchUserDTO);
        return ResponseEntity.ok(allUsers);
    }
}
