package com.martin.url_shortener.repository;

import com.martin.url_shortener.model.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {

    long countByShortUrlId(Long shortUrlId);

    List<Click> findByShortUrlIdOrderByClickedAtDesc(Long shortUrlId);

    @Query("SELECT COUNT(c) FROM Click c WHERE c.shortUrl.id = :id AND c.clickedAt >= CURRENT_TIMESTAMP - 7 DAY")
    long contarClicksUltimos7Dias(@Param("id") Long shortUrlId);
}