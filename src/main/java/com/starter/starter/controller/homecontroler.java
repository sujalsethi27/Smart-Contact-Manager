package com.starter.starter.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.starter.starter.dao.userRepo;
import com.starter.starter.entites.contact;
import com.starter.starter.entites.user;
import com.starter.starter.helper.helpermsg;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class homecontroler {

@Autowired
    private BCryptPasswordEncoder passwordEncoder;


@Autowired // this will create an instance of userRepo and inject it here
private userRepo userRepo;

    @GetMapping("/test")
    @ResponseBody // it will return the string as response body instead of looking for a view
    public String test() {

        user user = new user();
        user.setName("John Doe");
        user.setEmail("johndoe123@gmail.com");
        user.setPassword("abcd");
   contact contact = new contact();
    user.getContacts().add(contact); // adding contact to user's contact list


        userRepo.save(user);
        return "working";
    }
    // handler for home page
    @RequestMapping("/home")
    public String home(Model model) {
    model.addAttribute("title", "Home - Smart Contact Manager"); // heree title is the key and "Home - Smart Contact Manager" is the value
        return "home";
    }
    // handler for about page
     @RequestMapping("/about")
    public String about(Model model) {
    model.addAttribute("title", "About - Smart Contact Manager"); // heree title is the key and "Home - Smart Contact Manager" is the value
        return "about";
    }
    // handler for signup page
     @RequestMapping("/signup")
    public String signup(Model model) {
    model.addAttribute("title", "Register - Smart Contact Manager"); // heree title is the key and "Home - Smart Contact Manager" is the value
    model.addAttribute("user", new user());
 // we are sending an empty user object to the signup page to bind the form data
        return "signup";
    }
    // handler for login page
     @RequestMapping("/login")
    public String login(Model model) {
    model.addAttribute("title", "Login - Smart Contact Manager"); // heree title is the key and "Home - Smart Contact Manager" is the value
        return "login";
    }
    // handler to register user
    @PostMapping("/do_register")
public String registerUser(
        @Valid @ModelAttribute("user") user user,
        BindingResult bindingResult,
        @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
        Model model,
        HttpSession session) {

    // 1️⃣ Validation errors
    if (bindingResult.hasErrors()) {
        model.addAttribute("user", user);
        return "signup";
    }

    try {

        // 2️⃣ Terms not agreed
        if (!agreement) {
            throw new Exception("You must agree to the terms and conditions");
        }

        // 3️⃣ Set defaults
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setImageUrl("default.png");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 4️⃣ Save user
        user savedUser = this.userRepo.save(user);

        // 5️⃣ SUCCESS MESSAGE (session / flash)
        session.setAttribute(
                "message",
                new helpermsg(
                        "Successfully Registered! Welcome " + savedUser.getName(),
                        "alert-success"
                )
        );

        // 6️⃣ REDIRECT (IMPORTANT)
        return "redirect:/signup";

    } catch (Exception e) {

        // ❌ ERROR MESSAGE
        session.setAttribute(
                "message",
                new helpermsg(
                        "Something went wrong: " + e.getMessage(),
                        "alert-danger"
                )
        );

        model.addAttribute("user", user);
        return "signup";
    }
}


}
