package com.example.hotelbookingwebsite.Service;
import com.example.hotelbookingwebsite.Model.Promotion;
import com.example.hotelbookingwebsite.Repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;
    @Autowired
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }
    public List<Promotion> getPromotionByStatus(boolean status) {
        return promotionRepository.findByStatus(status);
    }

	public List<Promotion> getValidPromotions() {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findAll().stream()
                .filter(p -> p.isStatus()
                        && p.getStartDate() != null
                        && p.getEndDate() != null
                        && !now.isAfter(p.getEndDate().atStartOfDay()))
                .collect(Collectors.toList());
    }

    public List<Promotion> getExpiredPromotions() {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findAll().stream()
                .filter(p -> !p.isStatus()
                        || now.isAfter(p.getEndDate().atStartOfDay()))
                .collect(Collectors.toList());
    }
}