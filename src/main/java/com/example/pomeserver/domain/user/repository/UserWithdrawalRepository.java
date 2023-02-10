package com.example.pomeserver.domain.user.repository;

import com.example.pomeserver.domain.user.entity.UserWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWithdrawalRepository extends JpaRepository<UserWithdrawal, Long> {

    
}
