package com.med.aftas.serverside.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingDto {

    @NotNull(message = "Rank should not be null")
    @Min(value = 1, message = "Rank should be at least 1")
    private Integer rank;

    @NotNull(message = "Score should not be null")
    @PositiveOrZero(message = "Score should not be negative")
    private Integer score;

    @NotNull(message = "user num should not be null")
    @Positive(message = "user num should not be 0 or negative")
    private Integer userNum;

    @NotBlank(message = "competition code should not be blank")
    private String competitionCode;
}
