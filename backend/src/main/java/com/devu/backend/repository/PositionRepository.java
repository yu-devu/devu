package com.devu.backend.repository;

import com.devu.backend.entity.CompanyType;
import com.devu.backend.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Page<Position> findByCompany(CompanyType company, Pageable pageable);
}
