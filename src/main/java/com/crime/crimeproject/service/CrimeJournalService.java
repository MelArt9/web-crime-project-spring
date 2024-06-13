package com.crime.crimeproject.service;

import com.crime.crimeproject.dto.CrimeJournalDTO;
import com.crime.crimeproject.entity.CrimeJournal;
import com.crime.crimeproject.exception.NotFoundException;
import com.crime.crimeproject.exception.ValidationException;
import com.crime.crimeproject.repository.CrimeJournalRepository;
import com.crime.crimeproject.repository.ProfileRepository;
import com.crime.crimeproject.request.CreateCrimeJournalRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CrimeJournalService {

    private final CrimeJournalRepository crimeJournalRepository;
    private final ProfileRepository profileRepository;

    // Создает новую запись журнала преступлений
    public CrimeJournalDTO create(CreateCrimeJournalRequest request) {
        if (!profileRepository.existsById(request.getProfileId())) {
            throw new ValidationException("Не найден профиль с ID: " + request.getProfileId());
        }

        CrimeJournal newCrimeJournal = toCrimeJournal(request);
        if (crimeJournalRepository.findByAllAttributes(newCrimeJournal).isPresent()) {
            throw new ValidationException("Запись с такими атрибутами уже существует");
        }

        boolean saved = crimeJournalRepository.save(newCrimeJournal);
        if (!saved) {
            throw new ValidationException("Не удалось сохранить запись в журнале");
        }
        return toDTO(newCrimeJournal);
    }

    // Находит запись по ID и возвращает DTO
    public CrimeJournalDTO findById(UUID id) {
        // Получаем Optional<CrimeJournal> и преобразуем его в DTO, если объект существует.
        return crimeJournalRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Запись с ID " + id + " не найдена"));
    }

    // Находит записи по описанию и возвращает список DTO
    public List<CrimeJournalDTO> findByDescription(String query) {
        List<CrimeJournal> journals = crimeJournalRepository.findByDescription(query);
        if (journals.isEmpty()) {
            // Возврат пустого списка, чтобы API возвращал пустой JSON-массив
            // return Collections.emptyList();
            throw new NotFoundException("Записи с описанием «" + query + "» не найдены");
        }
        return journals.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Возвращает все записи в виде списка DTO
    public List<CrimeJournalDTO> getAll() {
        List<CrimeJournal> journals = crimeJournalRepository.findAll();
        if (journals.isEmpty()) {
            throw new NotFoundException("Записи в журнале не найдены");
        }
        return journals.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Возвращает все записи определенного пользователя по его ID
    public List<CrimeJournalDTO> getByProfileId(UUID profileId) {
        List<CrimeJournal> journals = crimeJournalRepository.findByProfileId(profileId);
        if (journals.isEmpty()) {
            throw new NotFoundException("Записи для данного пользователя не найдены");
        }
        return journals.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Обновляет существующую запись по ID
    public CrimeJournalDTO update(UUID id, CreateCrimeJournalRequest request) {
        // Проверка существования записи
        CrimeJournal existingJournal = crimeJournalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запись с ID " + id + " не найдена"));

        // Валидация profileId
        if (!profileRepository.existsById(request.getProfileId())) {
            throw new ValidationException("Профиль с ID " + request.getProfileId() + " не найден");
        }

        // Обновление данных
        existingJournal.setDescription(request.getDescription());
        existingJournal.setDateCrime(request.getDateCrime());
        existingJournal.setIsClosed(request.getIsClosed());
        existingJournal.setProfileId(request.getProfileId());

        crimeJournalRepository.update(existingJournal);
        return toDTO(existingJournal);
    }

    // Удаление записи по ID
    public void delete(UUID id) {
        if (!crimeJournalRepository.existsById(id)) {
            throw new NotFoundException("Запись с ID " + id + " не найдена для удаления");
        }
        crimeJournalRepository.deleteById(id);
    }

    // Удаляет все записи пользователя по его ID
    public void deleteAllByProfileId(UUID profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw new NotFoundException("Профиль с ID " + profileId + " не найден");
        }

        int deletedRows = crimeJournalRepository.deleteByProfileId(profileId);
        if (deletedRows == 0) {
            throw new NotFoundException("Записи для профиля с ID " + profileId + " не найдены или уже удалены");
        }
    }

    // Конвертирует данные запроса в сущность журнала преступлений
    private CrimeJournal toCrimeJournal(CreateCrimeJournalRequest request) {
        return CrimeJournal
                .builder()
                .id(UUID.randomUUID())
                .description(request.getDescription())
                .dateCrime(request.getDateCrime())
                .isClosed(request.getIsClosed())
                .profileId(request.getProfileId())
                .build();
    }

    // Конвертирует сущность журнала преступлений в DTO
    private CrimeJournalDTO toDTO(CrimeJournal newCrimeJournal) {
        return CrimeJournalDTO
                .builder()
                .id(newCrimeJournal.getId())
                .description(newCrimeJournal.getDescription())
                .dateCrime(newCrimeJournal.getDateCrime())
                .isClosed(newCrimeJournal.getIsClosed())
                .profile_id(newCrimeJournal.getProfileId())
                .build();
    }
}