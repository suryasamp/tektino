package tektino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    // @GetMapping("/login")
    // public String getLoginPage() {
    // return "login";
    // }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Username atau password salah!");
        }
        if (logout != null) {
            model.addAttribute("message", "Anda telah berhasil logout.");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout=true"; // Menampilkan pesan logout sukses
    }

    // @GetMapping("/logout")
    // public String logout(HttpServletRequest request, HttpServletResponse
    // response) {
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    // if (authentication != null) {
    // new SecurityContextLogoutHandler().logout(request, response, authentication);
    // }
    // return "redirect:/login"; // Arahkan ke halaman login dengan pesan logout
    // }

}
