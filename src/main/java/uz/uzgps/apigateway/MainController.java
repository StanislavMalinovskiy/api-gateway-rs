package uz.uzgps.apigateway;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MainController {
    @GetMapping("/")
    public String index(Principal principal) {
        return "root";
    }

    @GetMapping("/test")
    public String test(Principal principal) {
        return "test";
    }

}