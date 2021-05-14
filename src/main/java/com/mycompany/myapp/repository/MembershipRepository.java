package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Membership;
import com.mycompany.myapp.domain.MembershipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MembershipRepository extends JpaRepository<Membership, MembershipPK> {
}
