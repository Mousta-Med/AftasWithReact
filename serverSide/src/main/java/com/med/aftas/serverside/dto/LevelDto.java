package com.med.aftas.serverside.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelDto {

    @NotNull(message = "id should not be null")
    @Min(value = 1, message = "Code should be < 0")
    private Integer code;

    @NotBlank(message = "description should not be empty")
    private String description;

    @NotNull(message = "points should not be null")
    @Positive(message = "points should not be negative")
    private Integer points;
}
