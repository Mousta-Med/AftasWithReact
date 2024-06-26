package com.med.aftas.serverside.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FishDto {

    @NotBlank(message = "Fish Name Should not be Empty")
    private String name;

    @NotNull(message = "Fish Average Weight Should not be Null")
    @Positive(message = "Fish average weight should be a positive value")
    private Double averageWeight;

    @NotNull(message = "level id should not be null")
    private Integer levelCode;
}
