package tektino.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tektino.model.UserModel;
import tektino.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        System.out.println("USER:" + user);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    public void uploadAvatar(MultipartFile avatar, UserModel user) {
        if (avatar == null || avatar.isEmpty()) {
            String defaultAvatar = "man-avatar.png";
            if ("perempuan".equalsIgnoreCase(user.getJenis_kelamin())) {
                defaultAvatar = "woman-avatar.png";
            }
            String defaultAvatarPath = "src/main/resources/static/img/" + defaultAvatar;
            String uploadDir = new File("src/main/resources/static/uploads/").getAbsolutePath();
            String fileName = defaultAvatar;
            Path filePath = Paths.get(uploadDir + "/" + fileName);
        
            try {
                Files.copy(Paths.get(defaultAvatarPath), filePath, StandardCopyOption.REPLACE_EXISTING);
                user.setAvatarPath("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error jika gagal menyalin gambar default
            }
        } else {
            String originalFileName = avatar.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        
            String fileName = user.getUsername() + "_" + System.currentTimeMillis() + fileExtension;
            String uploadDir = new File("src/main/resources/static/uploads/").getAbsolutePath();
            Path filePath = Paths.get(uploadDir + "/" + fileName);
        
            try {
                Files.createDirectories(filePath.getParent());
                Files.copy(avatar.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                user.setAvatarPath("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error jika gagal mengunggah gambar
            }
        }

    }

}