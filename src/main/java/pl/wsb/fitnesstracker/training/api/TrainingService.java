package pl.wsb.fitnesstracker.training.api;

import pl.wsb.fitnesstracker.training.internal.ActivityType; // Importuj ActivityType

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

    Training createTraining(TrainingDto trainingDto);

    Training updateTraining(Long trainingId, TrainingDto trainingDto);

    List<Training> getAllTrainings();

    List<Training> getTrainingsByUserId(Long userId);

    List<Training> getFinishedTrainingsAfter(Date afterTime);

    List<Training> getTrainingsByActivityType(ActivityType activityType);

    Optional<Training> getTraining(Long trainingId);
}