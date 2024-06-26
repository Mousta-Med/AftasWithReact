package com.med.aftas.serverside.service.impl;

import com.med.aftas.serverside.dto.CompetitionDto;
import com.med.aftas.serverside.dto.respDto.CompetitionRespDto;
import com.med.aftas.serverside.exception.ResourceNotFoundException;
import com.med.aftas.serverside.model.Competition;
import com.med.aftas.serverside.repository.CompetitionRepository;
import com.med.aftas.serverside.service.CompetitionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CompetitionRespDto save(CompetitionDto competitionDto) {
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionDto.getCode());
        List<Competition> competitionWithDate = competitionRepository.findCompetitionByDate(competitionDto.getDate());
        if (competitionOptional.isPresent() || !competitionWithDate.isEmpty()) {
            throw new ResourceNotFoundException("Competition already exist with same name or date");
        }
        Competition competition = modelMapper.map(competitionDto, Competition.class);
        return modelMapper.map(competitionRepository.save(competition), CompetitionRespDto.class);
    }

    @Override
    public void delete(String id) {
        competitionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Competition Not found with this: " + id));
        competitionRepository.deleteById(id);
    }

    @Override
    public CompetitionRespDto update(String id, CompetitionDto competitionDto) {
        Competition existingCompetition = competitionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Competition Not found with this: " + id));
        BeanUtils.copyProperties(competitionDto, existingCompetition);
        existingCompetition.setCode(id);
        return modelMapper.map(competitionRepository.save(existingCompetition), CompetitionRespDto.class);
    }

    @Override
    public CompetitionRespDto findOne(String id) {
        return competitionRepository.findById(id)
                .map(competition -> modelMapper.map(competition, CompetitionRespDto.class)).orElseThrow(() -> new ResourceNotFoundException("Competition Not found with this: " + id));
    }

    @Override
    public List<CompetitionRespDto> findAll() {
        return competitionRepository.findAll().stream().map(competition -> modelMapper.map(competition, CompetitionRespDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<CompetitionRespDto> findWithPagination(Pageable pageable) {
        Page<Competition> competitionsPage = competitionRepository.findAll(pageable);
        return competitionsPage.map(competition -> modelMapper.map(competition, CompetitionRespDto.class));
    }


    @Override
    public List<CompetitionRespDto> findCompetitionsWithDate(LocalDate date) {
        return competitionRepository.findCompetitionByDate(date).stream().map(competition -> modelMapper.map(competition, CompetitionRespDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<CompetitionRespDto> getFilteredCompetitions(String filter, Pageable pageable) {
        LocalDate currentDate = LocalDate.now();
        Page<Competition> competitionsPage;
        switch (filter) {
            case "passed":
                competitionsPage = competitionRepository.findCompetitionsByDateBefore(currentDate, pageable);
                return competitionsPage.map(competition -> modelMapper.map(competition, CompetitionRespDto.class));
            case "ongoing":
                competitionsPage = competitionRepository.findCompetitionsByDate(currentDate, pageable);
                return competitionsPage.map(competition -> modelMapper.map(competition, CompetitionRespDto.class));
            case "upcoming":
                competitionsPage = competitionRepository.findCompetitionsByDateAfter(currentDate, pageable);
                return competitionsPage.map(competition -> modelMapper.map(competition, CompetitionRespDto.class));
            default:
                competitionsPage = competitionRepository.findAll(pageable);
                return competitionsPage.map(competition -> modelMapper.map(competition, CompetitionRespDto.class));
        }
    }

}
