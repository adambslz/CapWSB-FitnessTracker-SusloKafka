package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.internal.UserMapper;

/**
 * Komponent odpowiedzialny za mapowanie pomiędzy encjami "Training" a obiektami "TrainingDto".
 */
@Component
@RequiredArgsConstructor
public class TrainingMapper {

    private final UserMapper userMapper; // Do mapowania User <-> UserDto

    public TrainingDto toDto(Training training) {
        if (training == null) {
            return null;
        }
        TrainingDto dto = new TrainingDto();
        dto.setId(training.getId());
        if (training.getUser() != null) {
            dto.setUser(userMapper.toDto(training.getUser()));
            dto.setUserId(training.getUser().getId());
        }
        dto.setStartTime(training.getStartTime());
        dto.setEndTime(training.getEndTime());
        dto.setActivityType(training.getActivityType());
        dto.setDistance(training.getDistance());
        dto.setAverageSpeed(training.getAverageSpeed());
        return dto;
    }

    public Training toEntity(TrainingDto dto, User user) { // Przekazujemy zmapowanego Usera
        if (dto == null) {
            return null;
        }
        Training training = new Training(
                user,
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getActivityType(),
                dto.getDistance(),
                dto.getAverageSpeed()
        );

        return training;
    }
}