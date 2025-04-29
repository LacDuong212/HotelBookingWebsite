package com.example.hotelbookingwebsite.Repository;

import com.example.hotelbookingwebsite.Model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
    @Query("SELECT i FROM Images i WHERE i.id = :hid ORDER BY i.stt ASC")
    List<Images> findImagesByHid(Long hid);

    void deleteById(Long id);
}
