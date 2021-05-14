package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@IdClass(MembershipPK.class)
@Entity
@Table(name="membership")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Membership {

    @Id
    @Column(name="teamid")
    public Long teamID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Id
    @Column(name="userid")
    public String userID;





    public Long getTeamID() {
        return teamID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

}
