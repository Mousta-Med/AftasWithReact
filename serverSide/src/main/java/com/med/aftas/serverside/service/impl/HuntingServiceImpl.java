package com.med.aftas.serverside.service.impl;

import com.med.aftas.serverside.dto.HuntingDto;
import com.med.aftas.serverside.dto.respDto.HuntingRespDto;
import com.med.aftas.serverside.exception.ResourceNotFoundException;
import com.med.aftas.serverside.model.Competition;
import com.med.aftas.serverside.model.Fish;
import com.med.aftas.serverside.model.Hunting;
import com.med.aftas.serverside.model.User;
import com.med.aftas.serverside.repository.CompetitionRepository;
import com.med.aftas.serverside.repository.FishRepository;
import com.med.aftas.serverside.repository.HuntingRepository;
import com.med.aftas.serverside.repository.UserRepository;
import com.med.aftas.serverside.service.HuntingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HuntingServiceImpl implements HuntingService {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private HuntingRepository huntingRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public HuntingRespDto save(HuntingDto huntingDto) {
        Competition competition = competitionRepository.findById(huntingDto.getCompetitionCode()).orElseThrow(() -> new ResourceNotFoundException("Competition Not found"));
        User user = userRepository.findById(huntingDto.getUserNum()).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
        Fish fish = fishRepository.findById(huntingDto.getFishName()).orElseThrow(() -> new ResourceNotFoundException("Fish Not found"));
        Optional<Hunting> huntingOptional = huntingRepository.getHuntingByCompetitionCodeAndFishNameAndUserNum(huntingDto.getCompetitionCode(), huntingDto.getFishName(), huntingDto.getUserNum());
        Hunting hunting;
        if (Objects.equals(competition.getDate(), LocalDate.now())) {
            if (huntingOptional.isPresent()) {
                hunting = huntingOptional.get();
                hunting.setCompetition(competition);
                hunting.setUser(user);
                hunting.setFish(fish);
                hunting.setNumberOfFish(hunting.getNumberOfFish() + huntingDto.getNumberOfFish());
            } else {
                hunting = modelMapper.map(huntingDto, Hunting.class);
                hunting.setCompetition(competition);
                hunting.setUser(user);
                hunting.setFish(fish);
            }
        } else {
            throw new IllegalStateException("You Cannot Add hunting's if Competition end or not started yet");
        }
        return modelMapper.map(huntingRepository.save(hunting), HuntingRespDto.class);
    }

    @Override
    public void delete(Integer id) {
        huntingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hunting Not found with this: " + id));
        huntingRepository.deleteById(id);
    }

    @Override
    public HuntingRespDto update(Integer id, HuntingDto huntingDto) {
        Competition competition = competitionRepository.findById(huntingDto.getCompetitionCode()).orElseThrow(() -> new ResourceNotFoundException("Competition Not found"));
        User user = userRepository.findById(huntingDto.getUserNum()).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
        Fish fish = fishRepository.findById(huntingDto.getFishName()).orElseThrow(() -> new ResourceNotFoundException("Fish Not found"));
        Hunting existingHunting = huntingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hunting Not found with this: " + id));
        BeanUtils.copyProperties(huntingDto, existingHunting);
        existingHunting.setId(id);
        existingHunting.setCompetition(competition);
        existingHunting.setUser(user);
        existingHunting.setFish(fish);
        return modelMapper.map(huntingRepository.save(existingHunting), HuntingRespDto.class);
    }

    @Override
    public HuntingRespDto findOne(Integer id) {
        return huntingRepository.findById(id)
                .map(hunting -> modelMapper.map(hunting, HuntingRespDto.class)).orElseThrow(() -> new ResourceNotFoundException("Hunting Not found with this: " + id));
    }

    @Override
    public List<HuntingRespDto> findAll() {
        return huntingRepository.findAll().stream().map(hunting -> modelMapper.map(hunting, HuntingRespDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<HuntingRespDto> findWithPagination(Pageable pageable) {
        Page<Hunting> huntingsPage = huntingRepository.findAll(pageable);
        return huntingsPage.map(hunting -> modelMapper.map(hunting, HuntingRespDto.class));
    }


    @Override
    public List<HuntingRespDto> getHuntingsByCompetitionCodeAndUserNum(String code, Integer num) {
        return huntingRepository.getHuntingsByCompetitionCodeAndUserNum(code, num).stream().map(hunting -> modelMapper.map(hunting, HuntingRespDto.class)).collect(Collectors.toList());
    }
}
