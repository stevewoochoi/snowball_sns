package com.snowball.snowball.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotDto {
    private Long id;
    private String name;
    private String iconUrl;
    private String scope;
    private Long ownerId;
    private String ownerNickname;
    private Double lat;
    private Double lng;
}