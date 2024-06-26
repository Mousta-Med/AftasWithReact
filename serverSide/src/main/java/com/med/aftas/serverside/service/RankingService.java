package com.med.aftas.serverside.service;

import com.med.aftas.serverside.dto.RankingDto;
import com.med.aftas.serverside.dto.respDto.RankingRespDto;
import com.med.aftas.serverside.model.RankingId;

import java.util.List;

public interface RankingService extends BaseService<RankingDto, RankingId, RankingRespDto> {
    List<RankingRespDto> SetUpCompetitionRankings(String competitionCode);

    List<RankingRespDto> findWithCompetitionCode(String competitionCode);
}
