package com.inheaven.PetService.dto;

import lombok.Data;

@Data
public class PetRequest {
    private String nickName;
    private String name;
    private String type;
    private String breed;
    private String gender;
    private double weight;
    private String birthday;
    private String description;
    private String healthStatus;
    private String healthHistory;
}
