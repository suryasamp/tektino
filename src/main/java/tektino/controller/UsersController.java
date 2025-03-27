package tektino.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tektino.model.RoleModel;
import tektino.model.UserModel;
import tektino.repository.RoleRepository;
import tektino.repository.UserRepository;
import tektino.service.UserService;

@Controller
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping
    public String userPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String userLogin = userDetails.getUsername();

        model.addAttribute("userLogin", userLogin);
        model.addAttribute("users", userRepository.findAllByOrderByIdDesc());
        return "userContent/user";
    }

    @GetMapping("/detail/{id}")
    public String deatilScreenUser(Model model, @PathVariable Long id) {

        UserModel user = userRepository.findById(id).orElse(null);

        model.addAttribute("user", user);
        return "userContent/detail-user";
    }

    @GetMapping("/tambah")
    public String tampilFormTambah(Model model) {
        // model.addAttribute("buku", new Buku());
        return "userContent/create-user";
    }

    @PostMapping("/simpan")
    public String simpanUser(@ModelAttribute UserModel user, @RequestParam("avatar") MultipartFile avatar,
            RedirectAttributes redirectAttributes) {

        try {
            userService.uploadAvatar(avatar, user);

            // Enkripsi Password
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            // Simpan user ke database
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", "User berhasil disimpan!");
            return "redirect:/user"; // Pastikan ada '/' di awal

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            redirectAttributes.addFlashAttribute("error", "User gagal disimpan!");
            return "redirect:/userContent/create-user"; // Tambahkan '/' di awal
        }
    }

    @GetMapping("/edit/{id}")
    public String updatePage(@PathVariable Long id, Model model) {
        UserModel user = userRepository.findById(id).orElse(null);
        if (user != null) {
            System.out.println("getAvatarPath: " + user.getAvatarPath());
            model.addAttribute("user", user);
            model.addAttribute("roles", roleRepository.findAll());
            return "userContent/update-user";
        } else {
            return "error";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserModel updatedUser,
            @RequestParam("avatar") MultipartFile avatar, Model model,
            RedirectAttributes redirectAttributes) {

        UserModel existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            redirectAttributes.addFlashAttribute("error", "User tidak ditemukan!");
            return "redirect:/user";
        }

        if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
                userRepository.existsByUsername(updatedUser.getUsername())) {
            model.addAttribute("error", "Username sudah terdaftar.");
            model.addAttribute("user", existingUser);
            return "userContent/update-user";
        }

        existingUser.setUsername(updatedUser.getUsername());
        if (!updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setJenis_kelamin(updatedUser.getJenis_kelamin());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAlamat(updatedUser.getAlamat());
        existingUser.setNo_ktp(updatedUser.getNo_ktp());
        existingUser.setNo_npwp(updatedUser.getNo_npwp());
        existingUser.setNo_handphone(updatedUser.getNo_handphone());
        existingUser.setRole(updatedUser.getRole());

        if (!avatar.isEmpty()) {
            userService.uploadAvatar(avatar, existingUser);
        }

        userRepository.save(existingUser);
        redirectAttributes.addFlashAttribute("success", "User berhasil diupdate!");
        return "redirect:/user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        UserModel user = userRepository.findById(id).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User tidak ditemukan!");
        } else if ("superadmin".equals(user.getUsername()) && "Super Admin".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("error", "Super Admin tidak dapat dihapus!");
        } else {
            userRepository.delete(user);
            redirectAttributes.addFlashAttribute("success", "User berhasil dihapus!");
        }

        return "redirect:/user";
    }

}
