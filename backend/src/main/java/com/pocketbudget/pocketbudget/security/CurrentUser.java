package com.pocketbudget.pocketbudget.security;

public class CurrentUser {

    private final Long userId;
    private final Long householdId;
    private final String role;

    public CurrentUser(Long userId, Long householdId, String role) {
        this.userId = userId;
        this.householdId = householdId;
        this.role = role;
    }

    public Long getUserId(){return userId;}
    public Long getHouseholdId(){return householdId;}
    public String getRole(){return role;}

    public boolean isOwner(){return "OWNER".equals(role);}

    @Override
    public String toString(){
        return "CurrentUser{userId=" + userId +
                ", householdId=" + householdId +
                ", role=" + role + "'}";

    }






}
