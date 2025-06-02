package pl.wsb.fitnesstracker.training.internal;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.training.api.TrainingService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler REST obsługujący żądania HTTP związane z zarządzaniem treningami.
 */
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    @PostMapping
    public ResponseEntity<TrainingDto> createTraining(@RequestBody TrainingDto trainingDto) {
        Training createdTraining = trainingService.createTraining(trainingDto);
        return new ResponseEntity<>(trainingMapper.toDto(createdTraining), HttpStatus.CREATED);
    }

    @PutMapping("/{trainingId}")
    public ResponseEntity<TrainingDto> updateTraining(@PathVariable Long trainingId, @RequestBody TrainingDto trainingDto) {
        Training updatedTraining = trainingService.updateTraining(trainingId, trainingDto);
        return ResponseEntity.ok(trainingMapper.toDto(updatedTraining));
    }

    @GetMapping
    public ResponseEntity<List<TrainingDto>> getAllTrainings() {
        List<TrainingDto> trainings = trainingService.getAllTrainings().stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/user/{userId}") // Zmieniono ścieżkę, aby uniknąć konfliktu z /{trainingId}
    public ResponseEntity<List<TrainingDto>> getTrainingsByUserId(@PathVariable Long userId) {
        List<TrainingDto> trainings = trainingService.getTrainingsByUserId(userId).stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/{trainingId}")
    public ResponseEntity<TrainingDto> getTrainingById(@PathVariable Long trainingId) {
        Training training = trainingService.getTraining(trainingId) // Zakładając, że TrainingService ma dostęp do getTraining
                .orElseThrow(() -> new EntityNotFoundException("Training not found with id " + trainingId));
        return ResponseEntity.ok(trainingMapper.toDto(training));
    }


    @GetMapping("/finished/{afterTime}")
    public ResponseEntity<List<TrainingDto>> getFinishedTrainingsAfter(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date afterTime) {
        List<TrainingDto> trainings = trainingService.getFinishedTrainingsAfter(afterTime).stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/activityType")
    public ResponseEntity<List<TrainingDto>> getTrainingsByActivityType(@RequestParam ActivityType activityType) {
        List<TrainingDto> trainings = trainingService.getTrainingsByActivityType(activityType).stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainings);
    }
}