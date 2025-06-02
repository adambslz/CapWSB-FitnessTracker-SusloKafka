package pl.wsb.fitnesstracker.user.internal;

import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.SimpleUserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    /**
     * @return
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * @return
     */
    @GetMapping("/simple")
    public List<SimpleUserDto> getSimpleUsers(){
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toSimpleDto)
                .toList();
    }

    /**
     * @param userDto
     * @return
     * @throws InterruptedException
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //zwraca kod 201 jeżeli zasób został poprawnie utworzony
    public UserDto addUser(@RequestBody UserDto userDto) throws InterruptedException {

        // TODO: Implement the method to add a new user.
        //  You can use the @RequestBody annotation to map the request body to the UserDto object.
        User user = userMapper.toEntity(userDto);
        User savedUser = userService.createUser(user);
        return userMapper.toDto(savedUser);

        //return null;
    }

    /**
     * @param userId
     * @throws InterruptedException
     */
    @DeleteMapping("/{userId}") //Usuwanie użytkownika po id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) throws InterruptedException {
        userService.deleteUser(userId);
    }

    /**
     * @param email
     * @return
     */
    @GetMapping("/email")
    public List<UserDto> getUsersByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(user -> List.of(userMapper.toDto(user)))
                .orElseGet(() -> new ArrayList<>());
    }

    /**
     * @param id
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) throws InterruptedException { //@PathVariable wiążę id z adresu URL do parametru metody
        return userService.getUser(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    /**
     * @param userId
     * @param userDto
     * @throws InterruptedException
     */
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) throws InterruptedException {
        userService.updateUser(userId, userDto);
    }

    /**
     * @param date
     * @return
     */
    @GetMapping("older/{date}")
    public List<UserDto> getOlderUsers(@PathVariable("date") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){
        return userService.findUsersOlderThan(date)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}
