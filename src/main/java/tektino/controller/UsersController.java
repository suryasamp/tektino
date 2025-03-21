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

@Controller
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public String simpanUser(@ModelAttribute UserModel user, @RequestParam("avatar") MultipartFile avatar,
            RedirectAttributes redirectAttributes) {
        try {
            // Simpan file jika ada upload
            if (!avatar.isEmpty()) {
                String originalFileName = avatar.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                // Buat nama file unik: username + timestamp + ekstensi
                String fileName = user.getUsername() + "_" + System.currentTimeMillis() + fileExtension;
                String uploadDir = new File("src/main/resources/static/uploads/").getAbsolutePath();
                Path filePath = Paths.get(uploadDir + fileName);

                Files.createDirectories(filePath.getParent());
                Files.copy(avatar.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Set path gambar ke user
                user.setAvatarPath("/" + uploadDir + fileName);
            }

            // Enkripsi Password
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            // Simpan user ke database
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "User berhasil disimpan!");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengupload avatar!");
            e.printStackTrace();
        }

        return "redirect:/user";
    }

}
