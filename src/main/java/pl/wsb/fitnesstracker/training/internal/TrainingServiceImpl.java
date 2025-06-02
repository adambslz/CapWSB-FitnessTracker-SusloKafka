package pl.wsb.fitnesstracker.training.internal;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.training.api.TrainingProvider;
import pl.wsb.fitnesstracker.training.api.TrainingService;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.internal.UserRepository; // Potrzebne do pobrania użytkownika


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Implementacja interfejsów "TrainingService" oraz "TrainingProvider".
 * Odpowiada za logikę biznesową operacji na treningach oraz ich dostarczanie.
 */
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService, TrainingProvider { // Implementuje oba interfejsy

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository; // Do pobierania User
    private final TrainingMapper trainingMapper; // Zakładamy, że TrainingMapper jest już @Component i wstrzykiwalny

    @Override
    @Transactional
    public Training createTraining(TrainingDto trainingDto) {
        User user = userRepository.findById(trainingDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + trainingDto.getUserId()));
        Training training = trainingMapper.toEntity(trainingDto, user);
        return trainingRepository.save(training);
    }

    @Override
    @Transactional
    public Training updateTraining(Long trainingId, TrainingDto trainingDto) {
        Training existingTraining = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new EntityNotFoundException("Training not found with id " + trainingId));

        User user = userRepository.findById(trainingDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + trainingDto.getUserId()));

        existingTraining.setUser(user);
        existingTraining.setStartTime(trainingDto.getStartTime());
        existingTraining.setEndTime(trainingDto.getEndTime());
        existingTraining.setActivityType(trainingDto.getActivityType());
        existingTraining.setDistance(trainingDto.getDistance());
        existingTraining.setAverageSpeed(trainingDto.getAverageSpeed());
        // Encja Training powinna mieć settery dla tych pól

        return trainingRepository.save(existingTraining);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainingsByUserId(Long userId) {
        return trainingRepository.findAll().stream()
                .filter(training -> training.getUser() != null && training.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getFinishedTrainingsAfter(Date afterTime) {
        return trainingRepository.findAll().stream()
                .filter(training -> training.getEndTime() != null && training.getEndTime().after(afterTime))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findAll().stream()
                .filter(training -> training.getActivityType() == activityType)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Training> getTraining(final Long trainingId) {
        return trainingRepository.findById(trainingId);
    }
}