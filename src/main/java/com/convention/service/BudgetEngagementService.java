package com.convention.service;

import com.convention.repository.BudgetEngagementRepository;
import com.convention.service.dto.BudgetEngagementDTO;
import com.convention.service.mapper.BudgetEngagementMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BudgetEngagementService {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetEngagementService.class);

    private final BudgetEngagementRepository budgetRepository;
    private final BudgetEngagementMapper budgetMapper;

    public BudgetEngagementService(BudgetEngagementRepository budgetRepository, BudgetEngagementMapper budgetMapper) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
    }

    public BudgetEngagementDTO save(BudgetEngagementDTO dto) {
        LOG.debug("Saving budget engagement: {}", dto);
        var entity = budgetMapper.toEntity(dto);
        if (entity.getDateCreation() == null) entity.setDateCreation(Instant.now());
        if (entity.getMontantConsomme() == null) entity.setMontantConsomme(BigDecimal.ZERO);
        entity = budgetRepository.save(entity);
        return budgetMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<BudgetEngagementDTO> findByConvention(Long conventionId) {
        return budgetRepository.findByConventionIdOrderByAnneeBudgetaireDesc(conventionId).stream().map(budgetMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Optional<BudgetEngagementDTO> findOne(Long id) {
        return budgetRepository.findById(id).map(budgetMapper::toDto);
    }

    public BudgetEngagementDTO enregistrerConsommation(Long id, BigDecimal montant) {
        var entity = budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget engagement introuvable: " + id));
        BigDecimal total = entity.getMontantConsomme() != null ? entity.getMontantConsomme().add(montant) : montant;
        if (total.compareTo(entity.getMontantAutorise()) > 0) {
            throw new RuntimeException("Montant consommé dépasse le montant autorisé");
        }
        entity.setMontantConsomme(total);
        return budgetMapper.toDto(budgetRepository.save(entity));
    }

    public void delete(Long id) {
        budgetRepository.deleteById(id);
    }
}
