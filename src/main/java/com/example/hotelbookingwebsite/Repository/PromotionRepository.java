package com.example.hotelbookingwebsite.Repository;

import com.example.hotelbookingwebsite.Model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByStatus(boolean status);
 	List<Promotion> findAllByStatusTrue();
    Optional<Promotion> findByCode(String code);
}
