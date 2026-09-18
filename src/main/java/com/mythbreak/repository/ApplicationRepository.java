package com.mythbreak.repository;

import com.mythbreak.entity.Application;
import com.mythbreak.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByEarnerIdAndOpportunityId(Long earnerProfileId, Long opportunityId);

    boolean existsByEarnerIdAndOpportunityId(Long earnerProfileId, Long opportunityId);

    List<Application> findByEarnerId(Long earnerProfileId);

    List<Application> findByOpportunityId(Long opportunityId);

    List<Application> findByOpportunityIdAndStatus(Long opportunityId, ApplicationStatus status);

    List<Application> findByOpportunityCompanyId(Long companyProfileId);
}
