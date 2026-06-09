package ru.itis.flavorful_book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.flavorful_book.form.SignupForm;
import ru.itis.flavorful_book.service.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("form", new SignupForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("form") SignupForm form,
                         BindingResult errors,
                         Model model) {
        if (errors.hasErrors()) {
            return "signup";
        }
        try {
            userService.register(form);
        } catch (ru.itis.flavorful_book.exception.IllegalUserArgumentException e) {
            if (e.getUsernameState() != null) {
                errors.rejectValue("username", "username.taken", e.getUsernameState());
            }
            if (e.getEmailState() != null) {
                errors.rejectValue("email", "email.taken", e.getEmailState());
            }
            return "signup";
        }
        return "redirect:/login";
    }
}
