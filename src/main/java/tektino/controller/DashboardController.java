package tektino.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import tektino.model.UserModel;
import tektino.repository.UserRepository;
import tektino.service.UserService;

@Controller
@SessionAttributes("avatarPath")
public class DashboardController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String username = authentication.getName();
        UserModel user = userRepository.findByUsername(username);
        String name = user.getName();

        // Konversi ke List<String>
        List<String> roleList = authorities.stream()
                .map(GrantedAuthority::getAuthority) // Ambil nama role
                .collect(Collectors.toList());

        System.out.println("roles: " + roleList); // Debugging

        model.addAttribute("name", name);
        model.addAttribute("roles", roleList); // Pastikan roles berupa List<String>
        model.addAttribute("avatarPath", user.getAvatarPath());

        return "dashboard";
    }

}
