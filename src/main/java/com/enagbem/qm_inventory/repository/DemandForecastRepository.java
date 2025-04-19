package com.enagbem.qm_inventory.repository;


import com.enagbem.qm_inventory.model.DemandForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface DemandForecastRepository extends JpaRepository<DemandForecast, Long> {
    List<DemandForecast> findByProduct_ProductId(Long productId);
    List<DemandForecast> findByProduct_ProductIdAndForecastDateAfter(Long productId, LocalDate date);
    List<DemandForecast> findByForecastDateBetween(LocalDate start, LocalDate end);

    Collection<DemandForecast> findByForecastDateAfter(LocalDate cutoffDate);
}
