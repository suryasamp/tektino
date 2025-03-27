package tektino.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import tektino.model.UserModel;
import tektino.repository.UserRepository;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("avatarPath")
    public void getAvatarPath(Model model, UserModel userModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = authentication.getName();
            userModel = userRepository.findByUsername(username);
            if (userModel != null) {
                String avatar = userModel.getAvatarPath();
                if (avatar != null && !avatar.isEmpty()) {
                    model.addAttribute("avatarPath", avatar);
                    return;
                }
            }
        }
        model.addAttribute("avatarPath", "/img/avatar.png");
        return;
    }
}
