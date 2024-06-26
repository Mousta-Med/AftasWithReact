package com.med.aftas.serverside.controller;

import com.med.aftas.serverside.dto.UserDto;
import com.med.aftas.serverside.dto.respDto.UserRespDto;
import com.med.aftas.serverside.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
//    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<UserRespDto>> findAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<UserRespDto>> getPaginatedUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.findWithPagination(pageable).getContent());
    }

    @GetMapping("/{num}")
    public ResponseEntity<UserRespDto> findOneUser(@PathVariable Integer num) {
        return ResponseEntity.ok(userService.findOne(num));
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<UserRespDto>> findUserByNameOrFamilyName(@PathVariable String query) {
        return ResponseEntity.ok(userService.findByNameOrFamilyName(query));
    }

    @PostMapping
    public ResponseEntity<UserRespDto> saveUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.save(userDto));
    }

    @PutMapping("/{num}")
    public ResponseEntity<UserRespDto> updateUser(@PathVariable Integer num, @RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.update(num, userDto));
    }

    @PutMapping("/status")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<String> updateStatus(@RequestParam(name = "num") Integer num, @RequestParam(name = "role") String role) {
        return ResponseEntity.ok(userService.updateStatus(num, role));
    }

    @DeleteMapping("/{num}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "num") Integer num) {
        userService.delete(num);
        return ResponseEntity.noContent().build();
    }
}
