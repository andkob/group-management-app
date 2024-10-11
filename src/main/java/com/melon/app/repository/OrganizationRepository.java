package com.melon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.melon.app.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    // none
}
