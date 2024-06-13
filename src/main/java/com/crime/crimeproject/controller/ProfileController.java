package com.crime.crimeproject.controller;

import com.crime.crimeproject.dto.ProfileDTO;
import com.crime.crimeproject.exception.NotFoundException;
import com.crime.crimeproject.exception.ValidationException;
import com.crime.crimeproject.request.CreateProfileRequest;
import com.crime.crimeproject.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // Создание нового пользователя
    @PostMapping
    public ResponseEntity<ProfileDTO> create(@RequestBody @Valid CreateProfileRequest request) {
        ProfileDTO profile = profileService.create(request);
        return ResponseEntity.ok(profile);
    }

    // Получение пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getById(@PathVariable UUID id) {
        ProfileDTO profile = profileService.getById(id);
        return profile != null ? ResponseEntity.ok(profile) : ResponseEntity.notFound().build();
    }

    // Получение списка всех пользователей
    @GetMapping
    public ResponseEntity<List<ProfileDTO>> getAll() {
        List<ProfileDTO> profiles = profileService.getAll();
        return ResponseEntity.ok(profiles);
    }

    // Обновление данных пользователя по ID
    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> update(@PathVariable UUID id, @RequestBody @Valid CreateProfileRequest request) {
        try {
            ProfileDTO updatedProfile = profileService.update(id, request);
            return ResponseEntity.ok(updatedProfile);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Удаление пользователя по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean isDeleted = profileService.delete(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}