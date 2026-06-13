package com.convention.web.rest;

import com.convention.service.UniteOrganisationnelleService;
import com.convention.service.dto.UniteOrganisationnelleDTO;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/unite-organisationnelles")
public class UniteOrganisationnelleResource {

    private final UniteOrganisationnelleService service;

    public UniteOrganisationnelleResource(UniteOrganisationnelleService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<UniteOrganisationnelleDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniteOrganisationnelleDTO> getOne(@PathVariable Long id) {
        return service.findOne(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UniteOrganisationnelleDTO> create(@Valid @RequestBody UniteOrganisationnelleDTO dto) throws URISyntaxException {
        dto.setId(null);
        UniteOrganisationnelleDTO result = service.save(dto);
        return ResponseEntity.created(new URI("/api/unite-organisationnelles/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UniteOrganisationnelleDTO> update(@PathVariable Long id, @Valid @RequestBody UniteOrganisationnelleDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
