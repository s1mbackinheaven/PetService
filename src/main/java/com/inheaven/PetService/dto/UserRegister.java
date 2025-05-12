package com.inheaven.PetService.dto;

import lombok.Data;

@Data
public class UserRegister {
    private String username;
    private String fullname;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private String password;

}
