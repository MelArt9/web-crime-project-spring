package com.crime.crimeproject.service;

import com.crime.crimeproject.dto.ProfileDTO;
import com.crime.crimeproject.entity.Profile;
import com.crime.crimeproject.exception.NotFoundException;
import com.crime.crimeproject.exception.ValidationException;
import com.crime.crimeproject.repository.ProfileRepository;
import com.crime.crimeproject.request.CreateProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    // Создание нового профиля
    public ProfileDTO create(CreateProfileRequest request) {
        // Проверка, существует ли уже профиль с таким логином
        if (profileRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new ValidationException("Профиль с таким логином уже существует");
        }

        // Проверка, существует ли уже профиль с такой электронной почтой
        if (profileRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Профиль с такой электронной почтой уже существует");
        }

        Profile newProfile = Profile.builder()
                .id(UUID.randomUUID())
                .login(request.getLogin())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();

        profileRepository.save(newProfile);
        return toDTO(newProfile);
    }

    // Получение профиля по ID
    public ProfileDTO getById(UUID id) {
        return profileRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Профиль с ID " + id + " не найден"));
    }

    // Получение всех профилей
    public List<ProfileDTO> getAll() {
        return profileRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Обновление профиля по ID
    public ProfileDTO update(UUID id, CreateProfileRequest request) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Профиль с ID " + id + " не найден"));

        // Проверка на уникальность нового логина и email, если они изменены
        if (!existingProfile.getLogin().equals(request.getLogin()) && profileRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new ValidationException("Профиль с таким логином уже существует");
        }
        if (!existingProfile.getEmail().equals(request.getEmail()) && profileRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Профиль с такой электронной почтой уже существует");
        }

        existingProfile.setLogin(request.getLogin());
        existingProfile.setEmail(request.getEmail());
        existingProfile.setPassword(request.getPassword());

        boolean updated = profileRepository.update(existingProfile);
        if (!updated) {
            throw new RuntimeException("Обновление профиля не удалось");
        }
        return toDTO(existingProfile);
    }

    // Удаление профиля по ID
    public boolean delete(UUID id) {
        if (!profileRepository.existsById(id)) {
            throw new NotFoundException("Профиль с ID " + id + " не найден для удаления");
        }
        profileRepository.deleteById(id);
        return true;
    }

    // Вспомогательный метод для конвертации сущности в DTO
    private ProfileDTO toDTO(Profile profile) {
        return ProfileDTO.builder()
                .id(profile.getId())
                .login(profile.getLogin())
                .password(profile.getPassword())
                .email(profile.getEmail())
                .build();
    }

}