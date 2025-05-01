package com.example.hotelbookingwebsite.Repository;

import com.example.hotelbookingwebsite.Model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findAllByStatusTrue();
}
