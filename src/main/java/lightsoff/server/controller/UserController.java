package lightsoff.server.controller;

import lightsoff.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private User loggedUser;

    @PostMapping("/login/submit")
    public String login(String name, String passwd){
        if(!name.isEmpty() && !passwd.isEmpty()){
            loggedUser = new User(name,passwd);
        }
        return "redirect:/lightsoff/new";
    }

    @RequestMapping("/lightsoff/logout")
    public String logout(){
        loggedUser = null;
        return "redirect:/lightsoff/new";
    }

    public User getLoggedUser(){
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

}
