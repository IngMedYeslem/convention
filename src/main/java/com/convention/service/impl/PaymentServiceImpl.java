package com.convention.service.impl;

import com.convention.domain.PaymentEntity;
import com.convention.repository.FactureRepository;
import com.convention.repository.PaymentRepository;
import com.convention.service.PaymentService;
import com.convention.service.dto.PaymentDTO;
import com.convention.service.mapper.PaymentMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final FactureRepository factureRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, FactureRepository factureRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.factureRepository = factureRepository;
    }

    private void resolveAssociations(PaymentEntity entity, PaymentDTO dto) {
        if (dto.getFacture() != null && dto.getFacture().getId() != null) {
            entity.setFacture(factureRepository.getReferenceById(dto.getFacture().getId()));
        }
    }

    @Override
    public PaymentDTO save(PaymentDTO paymentDTO) {
        LOG.debug("Request to save Payment : {}", paymentDTO);
        PaymentEntity paymentEntity = paymentMapper.toEntity(paymentDTO);
        resolveAssociations(paymentEntity, paymentDTO);
        if (paymentEntity.getDateCreation() == null) {
            paymentEntity.setDateCreation(Instant.now());
        }
        paymentEntity = paymentRepository.save(paymentEntity);
        return paymentMapper.toDto(paymentEntity);
    }

    @Override
    public PaymentDTO update(PaymentDTO paymentDTO) {
        LOG.debug("Request to update Payment : {}", paymentDTO);
        PaymentEntity paymentEntity = paymentMapper.toEntity(paymentDTO);
        resolveAssociations(paymentEntity, paymentDTO);
        paymentEntity = paymentRepository.save(paymentEntity);
        return paymentMapper.toDto(paymentEntity);
    }

    @Override
    public Optional<PaymentDTO> partialUpdate(PaymentDTO paymentDTO) {
        LOG.debug("Request to partially update Payment : {}", paymentDTO);

        return paymentRepository
            .findById(paymentDTO.getId())
            .map(existingPayment -> {
                paymentMapper.partialUpdate(existingPayment, paymentDTO);
                return existingPayment;
            })
            .map(paymentRepository::save)
            .map(paymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable).map(paymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        LOG.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id).map(paymentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> findByFactureId(Long factureId) {
        return paymentRepository.findByFactureId(factureId).stream().map(paymentMapper::toDto).toList();
    }
}
