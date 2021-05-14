package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Team;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select team from Team team where team.leader.login = ?#{principal.preferredUsername}")
    List<Team> findByLeaderIsCurrentUser();
}
