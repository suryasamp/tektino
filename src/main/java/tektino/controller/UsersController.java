package tektino.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tektino.model.UserModel;
import tektino.repository.UserRepository;
import tektino.service.UserService;

@Controller
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping
    public String userPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "userContent/user";
    }

    @GetMapping("/tambah")
    public String tampilFormTambah(Model model) {
        // model.addAttribute("buku", new Buku());
        return "userContent/create-user";
    }

    @PostMapping("/simpan")
    public String simpanUser(@ModelAttribute UserModel user, @RequestParam("avatar") MultipartFile avatar,RedirectAttributes redirectAttributes) {

        System.out.println("AVATAR: "+avatar);
        System.out.println("USER: "+user);
        try {
            userService.uploadAvatar(avatar, user);

            // Enkripsi Password
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            // Simpan user ke database
            userRepository.save(user);
            
        } catch (Exception e) {
            System.out.println("ERROR: "+e);
        }
        
        redirectAttributes.addFlashAttribute("success", "User berhasil disimpan!");
        return "redirect:/user";
    }

}
