package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.IntentService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.IntentDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Intent.
 */
@RestController
@RequestMapping("/api")
public class IntentResource {

    private final Logger log = LoggerFactory.getLogger(IntentResource.class);

    private static final String ENTITY_NAME = "intent";

    private final IntentService intentService;

    public IntentResource(IntentService intentService) {
        this.intentService = intentService;
    }

    /**
     * POST  /intents : Create a new intent.
     *
     * @param intentDTO the intentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new intentDTO, or with status 400 (Bad Request) if the intent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/intents")
    @Timed
    public ResponseEntity<IntentDTO> createIntent(@Valid @RequestBody IntentDTO intentDTO) throws URISyntaxException {
        log.debug("REST request to save Intent : {}", intentDTO);
        if (intentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new intent cannot already have an ID")).body(null);
        }
        IntentDTO result = intentService.save(intentDTO);
        return ResponseEntity.created(new URI("/api/intents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /intents : Updates an existing intent.
     *
     * @param intentDTO the intentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated intentDTO,
     * or with status 400 (Bad Request) if the intentDTO is not valid,
     * or with status 500 (Internal Server Error) if the intentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/intents")
    @Timed
    public ResponseEntity<IntentDTO> updateIntent(@Valid @RequestBody IntentDTO intentDTO) throws URISyntaxException {
        log.debug("REST request to update Intent : {}", intentDTO);
        if (intentDTO.getId() == null) {
            return createIntent(intentDTO);
        }
        IntentDTO result = intentService.save(intentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, intentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /intents : get all the intents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of intents in body
     */
    @GetMapping("/intents")
    @Timed
    public ResponseEntity<List<IntentDTO>> getAllIntents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Intents");
        Page<IntentDTO> page = intentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/intents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /intents/:id : get the "id" intent.
     *
     * @param id the id of the intentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the intentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/intents/{id}")
    @Timed
    public ResponseEntity<IntentDTO> getIntent(@PathVariable String id) {
        log.debug("REST request to get Intent : {}", id);
        IntentDTO intentDTO = intentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(intentDTO));
    }

    /**
     * DELETE  /intents/:id : delete the "id" intent.
     *
     * @param id the id of the intentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/intents/{id}")
    @Timed
    public ResponseEntity<Void> deleteIntent(@PathVariable String id) {
        log.debug("REST request to delete Intent : {}", id);
        intentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
