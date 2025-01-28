package spring_test.pp_3_1_4_rest.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_test.pp_3_1_4_rest.dto.UserDto;
import spring_test.pp_3_1_4_rest.entity.Role;
import spring_test.pp_3_1_4_rest.entity.User;
import spring_test.pp_3_1_4_rest.repository.RoleRepository;
import spring_test.pp_3_1_4_rest.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminRestController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        return userOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        System.out.println("Incoming user data: " + userDto);
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAge(userDto.getAge());
        Set<Role> roles = userDto.getRole().stream()
                .map(roleDto -> new Role(roleDto.getId(), roleDto.getRole()))
                .collect(Collectors.toSet());
        user.setRole(roles);
        userService.saveUser(user, userDto.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        System.out.println("Incoming user ID: " + id);
        System.out.println("Incoming user data: " + userDto);

        Optional<User> optionalUser = userService.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        User existingUser = optionalUser.get();
        existingUser.setEmail(userDto.getEmail());
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setAge(userDto.getAge());
        Set<Role> roles = userDto.getRole().stream()
                .map(roleDto -> new Role(roleDto.getId(), roleDto.getRole()))
                .collect(Collectors.toSet());
        existingUser.setRole(roles);
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            userService.updateUser(existingUser, userDto.getPassword());
            System.out.println("Updating password...");
        } else {
            userService.updateUserWithoutPassword(existingUser);
            System.out.println("Updating without changing password...");
        }
        return ResponseEntity.ok("User updated successfully!");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    @GetMapping("/profile")
    public ResponseEntity<User> viewProfile(Principal principal) {
        String username = principal.getName();
        User user = userService.findByEmail(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }
}