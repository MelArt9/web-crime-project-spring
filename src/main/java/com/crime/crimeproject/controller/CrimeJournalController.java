package com.crime.crimeproject.controller;

import com.crime.crimeproject.dto.CrimeJournalDTO;
import com.crime.crimeproject.exception.NotFoundException;
import com.crime.crimeproject.request.CreateCrimeJournalRequest;
import com.crime.crimeproject.service.CrimeJournalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/journal")
public class CrimeJournalController {

    private final CrimeJournalService crimeJournalService;

    public CrimeJournalController(CrimeJournalService crimeJournalService) {
        this.crimeJournalService = crimeJournalService;
    } // либо вызывать @AllArgsConstructor

    // Ручка (end point) — /journal/{id}
    // Получение записи журнала по её ID
    @GetMapping("/{id}")
    public CrimeJournalDTO getById(@PathVariable UUID id) {
        return crimeJournalService.findById(id);
    }

    // Поиск записей журнала по описанию (ключевым словам)
    @GetMapping("/search/{query}")
    public List<CrimeJournalDTO> getById(@PathVariable String query) {
        return crimeJournalService.findByDescription(query);
    }

    // Создание новой записи в журнале
    @PostMapping
    public CrimeJournalDTO create(@RequestBody @Valid CreateCrimeJournalRequest request) {
        return crimeJournalService.create(request);
    }

    // Получение всех записей журнала
    @GetMapping
    public ResponseEntity<List<CrimeJournalDTO>> getAll() {
        List<CrimeJournalDTO> journals = crimeJournalService.getAll();
        return ResponseEntity.ok(journals);
    }

    // Получение всех записей журнала для конкретного пользователя по его ID
    @GetMapping("/user/{profileId}")
    public ResponseEntity<List<CrimeJournalDTO>> getByProfileId(@PathVariable UUID profileId) {
        List<CrimeJournalDTO> journals = crimeJournalService.getByProfileId(profileId);
        return ResponseEntity.ok(journals);
    }

    // Обновление записи в журнале по её ID
    @PutMapping("/{id}")
    public ResponseEntity<CrimeJournalDTO> update(@PathVariable UUID id, @RequestBody @Valid CreateCrimeJournalRequest request) {
        CrimeJournalDTO updatedJournal = crimeJournalService.update(id, request);
        return ResponseEntity.ok(updatedJournal);
    }

    // Удаление записи из журнала по её ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        crimeJournalService.delete(id);
    }

    // Удаление всех записей журнала, связанных с конкретным пользователем
    @DeleteMapping("/user/{profileId}")
    public void deleteAllByProfileId(@PathVariable UUID profileId) {
        crimeJournalService.deleteAllByProfileId(profileId);
    }

}