package com.Wallet.Application.module.Transactions.transectionlimite.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Wallet.Application.module.Transactions.transectionlimite.entity.MontlyLimite;

@Repository
public interface MontlyLimiteRepository extends JpaRepository<MontlyLimite, Long> {
	@Query("SELECT m FROM MontlyLimite m WHERE m.user_id = ?1 AND m.updatedon = CURRENT_DATE")
    Optional<MontlyLimite> findByUserId(Long userId);
}
