package com.med.aftas.serverside.service.impl;

import com.med.aftas.serverside.dto.RankingDto;
import com.med.aftas.serverside.dto.respDto.RankingRespDto;
import com.med.aftas.serverside.exception.CompetitionDateValidationException;
import com.med.aftas.serverside.exception.ResourceNotFoundException;
import com.med.aftas.serverside.model.Competition;
import com.med.aftas.serverside.model.User;
import com.med.aftas.serverside.model.Ranking;
import com.med.aftas.serverside.model.RankingId;
import com.med.aftas.serverside.repository.CompetitionRepository;
import com.med.aftas.serverside.repository.HuntingRepository;
import com.med.aftas.serverside.repository.UserRepository;
import com.med.aftas.serverside.repository.RankingRepository;
import com.med.aftas.serverside.service.RankingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RankingServiceImpl implements RankingService {

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HuntingRepository huntingRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public RankingRespDto save(RankingDto rankingDto) {
        RankingId rankingId = new RankingId(rankingDto.getUserNum(), rankingDto.getCompetitionCode());
        Optional<Ranking> rankingOptional = rankingRepository.findById(rankingId);
        if (rankingOptional.isPresent()) {
            throw new ResourceNotFoundException("Ranking already exist");
        }
        Competition competition = competitionRepository.findById(rankingDto.getCompetitionCode()).orElseThrow(() -> new ResourceNotFoundException("Competition Not found"));
        User user = userRepository.findById(rankingDto.getUserNum()).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = competition.getDate();
        if (ChronoUnit.DAYS.between(currentDate, startDate) < 1) {
            throw new CompetitionDateValidationException("Registration for competitions is allowed from the announcement until 24 hours before the start.");
        } else if (competition.getRankings().size() == competition.getNumberOfParticipants()) {
            throw new CompetitionDateValidationException("You Reach The Max Of Users In Competition");
        }
        Ranking ranking = modelMapper.map(rankingDto, Ranking.class);
        ranking.setId(rankingId);
        ranking.setCompetition(competition);
        ranking.setUser(user);
        return modelMapper.map(rankingRepository.save(ranking), RankingRespDto.class);
    }

    @Override
    public void delete(RankingId id) {
        rankingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ranking Not found with this: " + id));
        rankingRepository.deleteById(id);
    }

    @Override
    public RankingRespDto update(RankingId id, RankingDto rankingDto) {
        Competition competition = competitionRepository.findById(rankingDto.getCompetitionCode()).orElseThrow(() -> new ResourceNotFoundException("Competition Not found"));
        User user = userRepository.findById(rankingDto.getUserNum()).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
        Ranking existingRanking = rankingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ranking Not found with this: " + id));
        BeanUtils.copyProperties(rankingDto, existingRanking);
        existingRanking.setId(id);
        existingRanking.setCompetition(competition);
        existingRanking.setUser(user);
        return modelMapper.map(rankingRepository.save(existingRanking), RankingRespDto.class);
    }

    @Override
    public RankingRespDto findOne(RankingId id) {
        return rankingRepository.findById(id)
                .map(ranking -> modelMapper.map(ranking, RankingRespDto.class)).orElseThrow(() -> new ResourceNotFoundException("Ranking Not found with this: " + id));
    }

    @Override
    public List<RankingRespDto> findAll() {
        return rankingRepository.findAll().stream().map(ranking -> modelMapper.map(ranking, RankingRespDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<RankingRespDto> findWithPagination(Pageable pageable) {
        Page<Ranking> rankingsPage = rankingRepository.findAll(pageable);
        return rankingsPage.map(ranking -> modelMapper.map(ranking, RankingRespDto.class));
    }

    @Override
    public List<RankingRespDto> findWithCompetitionCode(String code) {
        return rankingRepository.findRankingsByCompetitionCodeOrderByScoreDesc(code).stream().map(ranking -> modelMapper.map(ranking, RankingRespDto.class)).collect(Collectors.toList());
    }


    @Override
    public List<RankingRespDto> SetUpCompetitionRankings(String competitionCode) {
        List<Ranking> rankings = rankingRepository.findRankingsByCompetitionCode(competitionCode);
        if (rankings.isEmpty()) {
            throw new ResourceNotFoundException("There is no Ranking in this competition");
        }
        rankings.forEach(ranking -> ranking.setScore(huntingRepository.getHuntingsByCompetitionCodeAndUserNum(competitionCode, ranking.getUser().getNum())
                .stream()
                .mapToInt(hunt -> hunt.getNumberOfFish() * hunt.getFish().getLevel().getPoints())
                .sum()));
        rankings.sort(Comparator.comparingInt(Ranking::getScore).reversed());
        int rank = 1;
        for (Ranking ranking : rankings) {
            ranking.setRank(rank++);
        }
        return Arrays.asList(modelMapper.map(rankingRepository.saveAll(rankings), RankingRespDto[].class)
        );
    }
}
