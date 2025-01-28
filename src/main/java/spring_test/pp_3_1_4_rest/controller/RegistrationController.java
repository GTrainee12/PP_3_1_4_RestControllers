package spring_test.pp_3_1_4_rest.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring_test.pp_3_1_4_rest.controller.error.EmailAlreadyExistsException;
import spring_test.pp_3_1_4_rest.entity.User;
import spring_test.pp_3_1_4_rest.repository.RoleRepository;
import spring_test.pp_3_1_4_rest.service.UserService;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "bootstrap_js/security/login";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") User user, Model model,
                               @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user, password);
            redirectAttributes.addFlashAttribute("message", "User registered successfully, Please log in.");
            return "redirect:/login";
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("errorMessage", "Email is already registered.");
            model.addAttribute("roles", roleRepository.findAll());
            return "bootstrap_js/security/login";
        }
    }
}

