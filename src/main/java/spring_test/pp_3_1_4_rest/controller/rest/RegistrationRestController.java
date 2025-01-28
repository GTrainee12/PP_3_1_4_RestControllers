package spring_test.pp_3_1_4_rest.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_test.pp_3_1_4_rest.controller.error.EmailAlreadyExistsException;
import spring_test.pp_3_1_4_rest.dto.UserDto;
import spring_test.pp_3_1_4_rest.entity.Role;
import spring_test.pp_3_1_4_rest.entity.User;
import spring_test.pp_3_1_4_rest.repository.RoleRepository;
import spring_test.pp_3_1_4_rest.repository.UserRepository;
import spring_test.pp_3_1_4_rest.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationRestController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    @PostMapping()
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

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        System.out.println("Fetching roles...");
        List<Role> roles = roleRepository.findAll();
        System.out.println("Roles fetched: " + roles);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}
