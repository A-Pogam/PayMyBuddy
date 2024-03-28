package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import util.HTMLGenerator;

@Controller
public class LoginController {

    private final HTMLGenerator htmlGenerator;

    @Autowired
    public LoginController(HTMLGenerator htmlGenerator) {
        this.htmlGenerator = htmlGenerator;
    }

    @GetMapping("/login")
    @ResponseBody
    public String loginPage() {
        return "login";
    }
}
