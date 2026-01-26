package com.pocketbudget.pocketbudget.household;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocketbudget.pocketbudget.security.CurrentUser;

@RestController
@RequestMapping("/api/household")
public class HouseholdController {

    private final HouseholdRepository households;

    public HouseholdController(HouseholdRepository households) {
        this.households = households;
    };

    @GetMapping("/invite")
    public InviteCodeResponse getInviteCode(Authentication auth){

        CurrentUser me =(CurrentUser) auth.getPrincipal();
        if(!me.isOwner()){
            throw new RuntimeException("Only Owner can view the invite code ");
        }

        Household household =households.findById(me.getHouseholdId())
                .orElseThrow(()-> new RuntimeException("Household not found"));

        return new InviteCodeResponse(household.getInviteCode());

    }

    public static class InviteCodeResponse{

        public String inviteCode;

        public InviteCodeResponse(String inviteCode){
            this.inviteCode=inviteCode;
        }

    }


}
