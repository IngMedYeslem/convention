package com.convention.web.rest;

import com.convention.service.StatistiquesService;
import com.convention.service.dto.StatistiquesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing statistics.
 */
@RestController
@RequestMapping("/api/statistiques")
public class StatistiquesResource {

    private static final Logger LOG = LoggerFactory.getLogger(StatistiquesResource.class);

    private final StatistiquesService statistiquesService;

    public StatistiquesResource(StatistiquesService statistiquesService) {
        this.statistiquesService = statistiquesService;
    }

    /**
     * {@code GET  /statistiques/dashboard} : get dashboard statistics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statistics.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<StatistiquesDTO> getDashboardStatistics() {
        LOG.debug("REST request to get dashboard statistics");
        StatistiquesDTO statistics = statistiquesService.getDashboardStatistics();
        return ResponseEntity.ok().body(statistics);
    }
}
