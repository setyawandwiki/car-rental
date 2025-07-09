package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.page.PageRequestDTO;
import com.rental_car_project_backend.car.rental.dto.request.page.SearchUserDTO;
import com.rental_car_project_backend.car.rental.dto.request.user.UpdateUserRequest;
import com.rental_car_project_backend.car.rental.dto.response.user.UpdateUserResponse;
import com.rental_car_project_backend.car.rental.dto.response.user.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserResponse> getAllUsers(PageRequestDTO pageRequestDTO, SearchUserDTO searchUserDTO);
    UpdateUserResponse updateUser(UpdateUserRequest request);
}
