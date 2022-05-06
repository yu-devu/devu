package com.devu.backend.repository;

import com.devu.backend.entity.CompanyType;
import com.devu.backend.entity.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    Page<Recruit> findByCompany(CompanyType company, Pageable pageable);
}
