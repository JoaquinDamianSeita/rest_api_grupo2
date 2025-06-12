package org.api.rest_api_grupo2.dto.response;

import lombok.Data;

@Data
public class UserResponseDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String biography;
    private String roleName;
    private Long userId;
}
