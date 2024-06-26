package com.med.aftas.serverside.controller;

import com.med.aftas.serverside.dto.LevelDto;
import com.med.aftas.serverside.service.LevelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/level")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @GetMapping
    public ResponseEntity<List<LevelDto>> findAllLevels() {
        return ResponseEntity.ok(levelService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<LevelDto>> getPaginatedLevels(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(levelService.findWithPagination(pageable).getContent());
    }

    @GetMapping("/{code}")
    public ResponseEntity<LevelDto> findOneLevel(@PathVariable Integer code) {
        return ResponseEntity.ok(levelService.findOne(code));
    }

    @PostMapping
    public ResponseEntity<LevelDto> saveLevel(@RequestBody @Valid LevelDto levelDto) {
        return ResponseEntity.ok(levelService.save(levelDto));
    }

    @PutMapping("/{code}")
    public ResponseEntity<LevelDto> updateLevel(@PathVariable Integer code, @RequestBody @Valid LevelDto levelDto) {
        return ResponseEntity.ok(levelService.update(code, levelDto));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteLevel(@PathVariable(name = "code") Integer id) {
        levelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
