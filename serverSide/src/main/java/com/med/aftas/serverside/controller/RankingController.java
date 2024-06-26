package com.med.aftas.serverside.controller;

import com.med.aftas.serverside.dto.RankingDto;
import com.med.aftas.serverside.dto.respDto.RankingRespDto;
import com.med.aftas.serverside.model.RankingId;
import com.med.aftas.serverside.service.RankingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<RankingRespDto>> findAllRankings() {
        return ResponseEntity.ok(rankingService.findAll());
    }

    @GetMapping("/{competitionCode}")
    public ResponseEntity<List<RankingRespDto>> findRankingsByCompetitionCod(@PathVariable String competitionCode) {
        return ResponseEntity.ok(rankingService.findWithCompetitionCode(competitionCode));
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<RankingRespDto>> getPaginatedRankings(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(rankingService.findWithPagination(pageable).getContent());
    }

    @GetMapping("/{competitionCode}/{userNum}")
    public ResponseEntity<RankingRespDto> findOneRanking(@PathVariable String competitionCode, @PathVariable Integer userNum) {
        RankingId id = new RankingId(userNum, competitionCode);
        return ResponseEntity.ok(rankingService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<RankingRespDto> saveRanking(@RequestBody @Valid RankingDto rankingDto) {
        return ResponseEntity.ok(rankingService.save(rankingDto));
    }

    @PutMapping("/{competitionCode}/{userNum}")
    public ResponseEntity<RankingRespDto> updateRanking(@PathVariable String competitionCode, @PathVariable Integer userNum, @RequestBody @Valid RankingDto rankingDto) {
        RankingId id = new RankingId(userNum, competitionCode);
        return ResponseEntity.ok(rankingService.update(id, rankingDto));
    }

    @DeleteMapping("/{competitionCode}/{userNum}")
    public ResponseEntity<Void> deleteRanking(@PathVariable String competitionCode, @PathVariable Integer userNum) {
        RankingId id = new RankingId(userNum, competitionCode);
        rankingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking/{competitionCode}")
    public ResponseEntity<List<RankingRespDto>> setCompetitionRankings(@PathVariable("competitionCode") final String competitionCode) {
        return ResponseEntity.ok(rankingService.SetUpCompetitionRankings(competitionCode));
    }
}
