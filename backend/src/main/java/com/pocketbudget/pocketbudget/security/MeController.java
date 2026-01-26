package com.pocketbudget.pocketbudget.security;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MeController {


    @GetMapping("/me")
    public String me(Authentication auth){

        CurrentUser me=(CurrentUser) auth.getPrincipal();
        return "userId="+me.getUserId()
                + ", householdId="+ me.getHouseholdId()
                + ", role=" + me.getRole();
    }

}
