package com.med.aftas.serverside.dto.respDto;

import com.med.aftas.serverside.dto.HuntingDto;
import com.med.aftas.serverside.dto.LevelDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FishRespDto {

    private String name;

    private Double averageWeight;

    private LevelDto level;

    private List<HuntingDto> huntings;
}
