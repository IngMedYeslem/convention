package com.convention.web.rest;

import com.convention.service.BudgetEngagementService;
import com.convention.service.dto.BudgetEngagementDTO;
import com.convention.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class BudgetEngagementResource {

    private static final String ENTITY_NAME = "budgetEngagement";

    private final BudgetEngagementService budgetService;

    public BudgetEngagementResource(BudgetEngagementService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/budget-engagements")
    public ResponseEntity<BudgetEngagementDTO> create(@Valid @RequestBody BudgetEngagementDTO dto) throws Exception {
        if (dto.getId() != null) throw new BadRequestAlertException("ID ne doit pas exister", ENTITY_NAME, "idexists");
        BudgetEngagementDTO result = budgetService.save(dto);
        return ResponseEntity.created(new URI("/api/budget-engagements/" + result.getId())).body(result);
    }

    @PutMapping("/budget-engagements/{id}")
    public ResponseEntity<BudgetEngagementDTO> update(@PathVariable Long id, @Valid @RequestBody BudgetEngagementDTO dto) {
        if (dto.getId() == null) throw new BadRequestAlertException("ID invalide", ENTITY_NAME, "idnull");
        return ResponseEntity.ok(budgetService.save(dto));
    }

    @GetMapping("/conventions/{conventionId}/budget-engagements")
    public ResponseEntity<List<BudgetEngagementDTO>> getByConvention(@PathVariable Long conventionId) {
        return ResponseEntity.ok(budgetService.findByConvention(conventionId));
    }

    @GetMapping("/budget-engagements/{id}")
    public ResponseEntity<BudgetEngagementDTO> getOne(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(budgetService.findOne(id));
    }

    @PostMapping("/budget-engagements/{id}/consommation")
    public ResponseEntity<BudgetEngagementDTO> enregistrerConsommation(@PathVariable Long id, @RequestParam BigDecimal montant) {
        return ResponseEntity.ok(budgetService.enregistrerConsommation(id, montant));
    }

    @DeleteMapping("/budget-engagements/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
