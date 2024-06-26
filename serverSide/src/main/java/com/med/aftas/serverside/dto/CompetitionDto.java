package com.med.aftas.serverside.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionDto {

    @NotNull(message = "Code Should Not Be Null")
    @Pattern(regexp = "[A-Za-z]{3}-(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{2}", message = "Code should follow the pattern 'XXX-DD-MM-YY'")
    private String code;

    @FutureOrPresent(message = "date Should Be Present or future")
    @NotNull(message = "Date Should Not Be Null")
    private LocalDate date;

    @NotNull(message = "Start Time Should Not Be Null")
    private LocalTime startTime;

    @NotNull(message = "End Time Should Not Be Null")
    private LocalTime endTime;

    @Positive(message = "number Of Participants should be a positive value")
    private Integer numberOfParticipants;

    @NotBlank(message = "Location Should Not Be Empty")
    private String location;

    @NotNull(message = "Amount Should Not Be Null")
    private Double amount;
}
