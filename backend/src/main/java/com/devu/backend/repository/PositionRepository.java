package com.devu.backend.repository;

import com.devu.backend.entity.CompanyType;
import com.devu.backend.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Page<Position> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Position> findByTitleContainingIgnoreCaseAndCompany(String keyword, CompanyType company, Pageable pageable);
}
