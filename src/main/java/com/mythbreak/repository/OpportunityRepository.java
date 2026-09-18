package com.mythbreak.repository;

import com.mythbreak.entity.Opportunity;
import com.mythbreak.enums.OpportunityStatus;
import com.mythbreak.enums.OpportunityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    List<Opportunity> findByStatus(OpportunityStatus status);

    List<Opportunity> findByCompanyId(Long companyProfileId);

    List<Opportunity> findByCompanyIdAndStatus(Long companyProfileId, OpportunityStatus status);

    List<Opportunity> findByType(OpportunityType type);

    @Query("SELECT o FROM Opportunity o WHERE o.status = 'OPEN' AND " +
           "(LOWER(o.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Opportunity> searchOpenOpportunities(@Param("keyword") String keyword);
}
