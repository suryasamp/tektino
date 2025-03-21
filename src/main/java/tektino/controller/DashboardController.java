package tektino.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import tektino.model.UserModel;
import tektino.repository.UserRepository;
import tektino.service.UserService;

@Controller
public class DashboardController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, UserModel userMOdel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String username = authentication.getName();
        String name = userRepository.findByUsername(username).getName();

        model.addAttribute("name", name);
        model.addAttribute("roles", authorities);

        return "dashboard";
    }
}
