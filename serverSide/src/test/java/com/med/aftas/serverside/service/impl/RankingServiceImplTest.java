package com.med.aftas.serverside.service.impl;

import com.med.aftas.serverside.dto.CompetitionDto;
import com.med.aftas.serverside.dto.RankingDto;
import com.med.aftas.serverside.dto.respDto.RankingRespDto;
import com.med.aftas.serverside.enums.IdentityDocumentType;
import com.med.aftas.serverside.model.*;
import com.med.aftas.serverside.repository.CompetitionRepository;
import com.med.aftas.serverside.repository.HuntingRepository;
import com.med.aftas.serverside.repository.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RankingServiceImplTest {

    List<Ranking> currentRankings;
    @Mock
    private RankingRepository rankingRepository;
    @Mock
    private CompetitionRepository competitionRepository;
    @Mock
    private HuntingRepository huntingRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private RankingServiceImpl rankingService;
    private User user;
    private Competition competition;
    private Ranking ranking;
    private RankingDto rankingDto;
    private CompetitionDto competitionDto;
    private Hunting hunting;

    @BeforeEach
    void setUp() {

        competition = new Competition();
        competition.setCode("sao-20-12-23");
        competition.setDate(LocalDate.now());
        competition.setStartTime(LocalTime.now());
        competition.setEndTime(LocalTime.of(23, 59, 59));
        competition.setNumberOfParticipants(3);
        competition.setLocation("saouira");
        competition.setAmount(1000.0);


        user = User.builder()
                .num(1)
                .name("ahmed")
                .familyName("test")
                .identityDocument(IdentityDocumentType.CIN)
                .identityNumber("HH121093")
                .accessionDate(LocalDate.now())
                .nationality("MA")
                .build();

        competition = Competition.builder()
                .code("Saf-12-12-23")
                .date(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now())
                .amount(100.00)
                .location("Safi")
                .build();

        hunting = Hunting.builder()
                .numberOfFish(1)
                .fish(Fish.builder()
                        .level(Level.builder()
                                .points(10)
                                .build()
                        ).build()
                )
                .build();

        RankingId id = RankingId.builder()
                .competitionCode(competition.getCode())
                .userNum(user.getNum())
                .build();

        ranking = Ranking.builder()
                .id(id)
                .score(0)
                .user(user)
                .competition(competition).build();

        currentRankings = new ArrayList<>();
        currentRankings.add(new Ranking(id, 1, 0, user, competition));
        currentRankings.add(new Ranking(id, 1, 0, user, competition));
    }

    @Test
    void setUpCompetitionRankings() {
        when(competitionRepository.findById(competition.getCode())).thenReturn(Optional.of(competition));
        when(rankingRepository.findRankingsByCompetitionCode(competition.getCode())).thenReturn(currentRankings);
        when(rankingRepository.save(any())).thenReturn(new Ranking());

        List<RankingRespDto> podium = rankingService.SetUpCompetitionRankings(competition.getCode());

        verify(competitionRepository).findById(competition.getCode());
        verify(rankingRepository).findRankingsByCompetitionCode(competition.getCode());
        verify(rankingRepository, times(currentRankings.size())).save(any());

        assertNotNull(podium);
        assertEquals(2, podium.size());
    }
}