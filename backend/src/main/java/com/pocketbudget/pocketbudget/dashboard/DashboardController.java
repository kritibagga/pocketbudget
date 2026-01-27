package com.pocketbudget.pocketbudget.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pocketbudget.pocketbudget.dashboard.dto.DashboardResponse;
import com.pocketbudget.pocketbudget.dashboard.dto.JarCardResponse;
import com.pocketbudget.pocketbudget.dashboard.dto.JarStatus;
import com.pocketbudget.pocketbudget.jars.JarRepository;

import com.pocketbudget.pocketbudget.security.CurrentUser;
import com.pocketbudget.pocketbudget.transactions.Transaction;
import com.pocketbudget.pocketbudget.transactions.TransactionRepository;
import com.pocketbudget.pocketbudget.transactions.TransactionType;
import com.pocketbudget.pocketbudget.jars.Jar;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final JarRepository jars;
    private final TransactionRepository txns;


    public DashboardController(JarRepository jars, TransactionRepository txns) {
        this.jars = jars;
        this.txns = txns;
    }


    @GetMapping
    public DashboardResponse get(@RequestParam String month, Authentication auth){
        CurrentUser me=(CurrentUser) auth.getPrincipal();

        YearMonth ym = YearMonth.parse(month);
        LocalDate start=ym.atDay(1);
        LocalDate end =ym.atEndOfMonth();

        List<Jar> jarList=jars.findByHouseholdIdAndArchivedFalse(me.getHouseholdId());
        List<Transaction> monthTxns= txns.findByHouseholdIdAndDateBetweenOrderByDateDesc(me.getHouseholdId(), start, end);

        BigDecimal totalIncome=BigDecimal.ZERO;
        BigDecimal totalExpense=BigDecimal.ZERO;

        //spentByJarId holds total EXPENSE amount per jar

        Map<Long, BigDecimal> spentByJarId=new HashMap<>();

        for(Transaction t: monthTxns){
            if(t.getType()==TransactionType.INCOME){
                totalIncome=totalIncome.add(t.getAmount());
            }else {
                totalExpense=totalExpense.add(t.getAmount());

                if(t.getJar() !=null){
                    Long jarId=t.getJar().getId();
                    BigDecimal current=spentByJarId.getOrDefault(jarId, BigDecimal.ZERO);
                    spentByJarId.put(jarId, current.add(t.getAmount()));
                }
            }
        }

        BigDecimal netBalance= totalIncome.subtract(totalExpense);
        List<JarCardResponse> jarCards=new ArrayList<>();

        for(Jar j: jarList){
            BigDecimal limit=j.getMonthlyLimit();
            BigDecimal spent= spentByJarId.getOrDefault(j.getId(), BigDecimal.ZERO);
            BigDecimal remaining= limit.subtract(spent);

            JarStatus status= calculateStatus(limit, spent);

            jarCards.add(new JarCardResponse(j.getId(), j.getName(), j.getIcon(), j.getColor(), limit, spent, remaining, status));
        }

    return new DashboardResponse(month, totalIncome, totalExpense, netBalance, jarCards);


    }
    private JarStatus calculateStatus(BigDecimal limit, BigDecimal spent){

        if(limit==null || limit.compareTo(BigDecimal.ZERO)==0){
            return spent.compareTo(BigDecimal.ZERO) >0 ? JarStatus.OVER: JarStatus.ON_TRACK;
        }

        if(spent.compareTo(limit)>0){
            return JarStatus.OVER;
        }

        //close to limit if spent >= 80% of limit
        BigDecimal ratio=spent.divide(limit, 4, RoundingMode.HALF_UP);

        if(ratio.compareTo(new BigDecimal("0.8"))>=0){
            return JarStatus.CLOSE;
        }

        return JarStatus.ON_TRACK;


    }


}

