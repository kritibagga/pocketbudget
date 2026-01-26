package com.pocketbudget.pocketbudget.jars;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocketbudget.pocketbudget.household.HouseholdRepository;
import com.pocketbudget.pocketbudget.jars.dto.JarCreateRequest;
import com.pocketbudget.pocketbudget.jars.dto.JarResponse;
import com.pocketbudget.pocketbudget.security.CurrentUser;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/jars")
public class JarController {
    private final JarRepository jars;
    private final HouseholdRepository households;

    public JarController(JarRepository jars, HouseholdRepository households) {
        this.jars = jars;
        this.households = households;
    }

    @GetMapping
    public List<JarResponse> list(Authentication auth){
        CurrentUser me=(CurrentUser) auth.getPrincipal();
        return jars.findByHouseholdIdAndArchivedFalse(me.getHouseholdId())
                .stream()
                .map(j->new JarResponse(j.getId(), j.getName(), j.getMonthlyLimit(), j.getIcon(), j.getColor(), j.isArchived()))
                .toList();

    }

    @PostMapping
    public JarResponse create(@Valid @RequestBody JarCreateRequest req, Authentication auth){
        CurrentUser me=(CurrentUser) auth.getPrincipal();
        if(!me.isOwner()){
            throw new RuntimeException("Only Owner can create jars");
        }

        var household =households.findById(me.getHouseholdId())
                .orElseThrow(()-> new RuntimeException("Household not found"));

        Jar jar =new Jar();
        jar.setHousehold(household);
        jar.setName(req.name);
        jar.setMonthlyLimit(req.monthlyLimit);
        jar.setIcon(req.icon);
        jar.setColor(req.color);
        jar.setArchived(false);

        Jar saved =jars.save(jar);

        return new JarResponse(saved.getId(), saved.getName(), saved.getMonthlyLimit(), saved.getIcon(), saved.getColor(), saved.isArchived());

    }


    @PostMapping("/{id}/archive")
    public void archive(@PathVariable Long id, Authentication auth){
        CurrentUser me= (CurrentUser) auth.getPrincipal();

        if(!me.isOwner()){
            throw new RuntimeException("Only owner can archive jars");
        }

        Jar jar= jars.findByIdAndHouseholdId(id, me.getHouseholdId())
                .orElseThrow(()->new RuntimeException("Jar not found"));

        jar.setArchived(true);
        jars.save(jar);

    }

}
