package com.med.aftas.serverside.service;

import com.med.aftas.serverside.dto.HuntingDto;
import com.med.aftas.serverside.dto.respDto.HuntingRespDto;

import java.util.List;

public interface HuntingService extends BaseService<HuntingDto, Integer, HuntingRespDto> {
    List<HuntingRespDto> getHuntingsByCompetitionCodeAndUserNum(String code, Integer num);
}
