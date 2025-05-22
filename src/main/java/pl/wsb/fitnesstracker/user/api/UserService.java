package pl.wsb.fitnesstracker.user.api;

import java.time.LocalDate;

import pl.wsb.fitnesstracker.user.internal.UserDto;
import java.util.List;
/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    User createUser(User user);

    void deleteUser(Long userId);

    void updateUser(Long userId, pl.wsb.fitnesstracker.user.api.UserDto userDto);

    void updateUser(Long userId, UserDto userDto);

    List<User> findUsersOlderThan(LocalDate date);
}
