package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.IntentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Intent.
 */
public interface IntentService {

    /**
     * Save a intent.
     *
     * @param intentDTO the entity to save
     * @return the persisted entity
     */
    IntentDTO save(IntentDTO intentDTO);

    /**
     *  Get all the intents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IntentDTO> findAll(Pageable pageable);

    /**
     *  Get the "tag" intent.
     *
     *  @param tag the id of the entity
     *  @return the entity
     */
    IntentDTO findOne(String tag);

    /**
     *  Delete the "tag" intent.
     *
     *  @param tag the tag of the entity
     */
    void delete(String tag);
}
