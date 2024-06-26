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
public class HuntingDto {

    private Integer id;

    @Positive(message = "number Of Fish should not be 0 or negative")
    private Integer numberOfFish;

    @NotBlank(message = "fish name should not be blank")
    private String fishName;

    @NotNull(message = "user num should not be null")
    @Positive(message = "user num should not be 0 or negative")
    private Integer userNum;

    @NotBlank(message = "competition code should not be blank")
    private String competitionCode;
}
