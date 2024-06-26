package com.med.aftas.serverside.service.impl;

import com.med.aftas.serverside.dto.FishDto;
import com.med.aftas.serverside.dto.respDto.FishRespDto;
import com.med.aftas.serverside.exception.ResourceNotFoundException;
import com.med.aftas.serverside.model.Fish;
import com.med.aftas.serverside.model.Level;
import com.med.aftas.serverside.repository.FishRepository;
import com.med.aftas.serverside.repository.LevelRepository;
import com.med.aftas.serverside.service.FishService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FishServiceImpl implements FishService {

    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public FishRespDto save(FishDto fishDto) {
        Optional<Fish> fishOptional = fishRepository.findById(fishDto.getName());
        if (fishOptional.isPresent()) {
            throw new ResourceNotFoundException("Fish already exist");
        }
        Level level = levelRepository.findById(fishDto.getLevelCode()).orElseThrow(() -> new ResourceNotFoundException("Level Not found"));
        Fish fish = modelMapper.map(fishDto, Fish.class);
        fish.setLevel(level);
        return modelMapper.map(fishRepository.save(fish), FishRespDto.class);
    }

    @Override
    public void delete(String id) {
        fishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fish Not found with this: " + id));
        fishRepository.deleteById(id);
    }

    @Override
    public FishRespDto update(String id, FishDto fishDto) {
        Fish existingFish = fishRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fish Not found with this: " + id));
        Level level = levelRepository.findById(fishDto.getLevelCode()).orElseThrow(() -> new ResourceNotFoundException("Level Not found"));
        BeanUtils.copyProperties(fishDto, existingFish);
        existingFish.setName(id);
        existingFish.setLevel(level);
        return modelMapper.map(fishRepository.save(existingFish), FishRespDto.class);
    }

    @Override
    public FishRespDto findOne(String id) {
        return fishRepository.findById(id)
                .map(fish -> modelMapper.map(fish, FishRespDto.class)).orElseThrow(() -> new ResourceNotFoundException("Fish Not found with this: " + id));
    }

    @Override
    public List<FishRespDto> findAll() {
        return fishRepository.findAll().stream().map(fish -> modelMapper.map(fish, FishRespDto.class)).collect(Collectors.toList());
    }

    @Override
    public Page<FishRespDto> findWithPagination(Pageable pageable) {
        Page<Fish> fishsPage = fishRepository.findAll(pageable);
        return fishsPage.map(fish -> modelMapper.map(fish, FishRespDto.class));
    }
}
