package com.mycompany.myapp.domain;

import java.io.Serializable;

public class MembershipPK implements Serializable {


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String userID;

    public Long getTeamID() {
        return teamID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

    public Long teamID;
}

