package com.example.hotelbookingwebsite.Controller;

import com.example.hotelbookingwebsite.Model.User;
import com.example.hotelbookingwebsite.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/signin")
public class SigninController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showSigninForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng");
        }
        return "web/signin";
    }

    @PostMapping
    public String processSignin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User user = userService.authenticate(email, password);

        if (user == null) {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng");
            model.addAttribute("email", email);
            return "web/signin";
        }

        session.setAttribute("loggedInUser", user);
        return "redirect:/";
    }
}
