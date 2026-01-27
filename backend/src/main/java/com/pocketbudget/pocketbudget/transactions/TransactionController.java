package com.pocketbudget.pocketbudget.transactions;

import org.springframework.web.bind.annotation.RequestMapping;
import com.pocketbudget.pocketbudget.auth.dto.TransactionCreateRequest;
import com.pocketbudget.pocketbudget.auth.dto.TransactionResponse;
import com.pocketbudget.pocketbudget.jars.Jar;
import com.pocketbudget.pocketbudget.jars.JarRepository;
import com.pocketbudget.pocketbudget.security.CurrentUser;
import com.pocketbudget.pocketbudget.user.User;
import com.pocketbudget.pocketbudget.user.UserRepository;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {



    private final TransactionRepository txns;
    private final JarRepository jars;
    private final UserRepository users;


    public TransactionController(TransactionRepository txns, JarRepository jars, UserRepository users) {
        this.txns = txns;
        this.jars = jars;
        this.users = users;
    }

    @GetMapping
    public List<TransactionResponse> list(@RequestParam String month, Authentication auth){
        CurrentUser me =(CurrentUser) auth.getPrincipal();

        YearMonth ym = YearMonth.parse(month);
        LocalDate start= ym.atDay(1);
        LocalDate end =ym.atEndOfMonth();

        return txns.findByHouseholdIdAndDateBetweenOrderByDateDesc(me.getHouseholdId(), start, end)
                .stream()
                .map(t-> new TransactionResponse(
                    t.getId(),
                    t.getType(),
                    t.getAmount(),
                    t.getDate(),
                    t.getJar()==null?null:t.getJar().getId(),
                    t.getNote(),
                    t.getCreatedBy().getId()
                ))

                .toList();

    }

    @PostMapping
    public TransactionResponse create(@Valid @RequestBody TransactionCreateRequest req, Authentication auth){
        CurrentUser me=(CurrentUser) auth.getPrincipal();

        User creator= users.findById(me.getUserId())
                .orElseThrow(()-> new RuntimeException("User not found"));

        Jar jar=null;

        if(req.type==TransactionType.EXPENSE){
            if(req.jarId==null){
                throw new RuntimeException("jarId is required for EXPENSE");
            }
            jar=jars.findByIdAndHouseholdId(req.jarId, me.getHouseholdId())
                    .orElseThrow(()-> new RuntimeException("jar not found"));
        }

        Transaction t =new Transaction();
        t.setHousehold(creator.getHousehold());
        t.setCreatedBy(creator);
        t.setType(req.type);
        t.setAmount(req.amount);
        t.setDate(req.date);
        t.setNote(req.note);
        t.setJar(jar);

        Transaction saved=txns.save(t);

        return new TransactionResponse(
            saved.getId(),
            saved.getType(),
            saved.getAmount(),
            saved.getDate(),
            saved.getJar()==null?null:saved.getJar().getId(),
            saved.getNote(),
            saved.getCreatedBy().getId());
        }

        @DeleteMapping("/{id}")
        public void delete(@PathVariable Long id , Authentication auth){
            CurrentUser me =(CurrentUser) auth.getPrincipal();

            Transaction t= txns.findByIdAndHouseholdId(id, me.getHouseholdId())
                    .orElseThrow(()-> new RuntimeException("Transaction not found"));

            //Owner can delete any transactions whereas spouse can delete only their own

            if(!me.isOwner() && !t.getCreatedBy().getId().equals(me.getUserId())){
                throw new RuntimeException("you can delete only your own transactions");
            }

            txns.delete(t);




}
}
