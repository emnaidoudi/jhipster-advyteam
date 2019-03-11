package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.IntentService;
import com.mycompany.myapp.domain.Intent;
import com.mycompany.myapp.repository.IntentRepository;
import com.mycompany.myapp.service.dto.IntentDTO;
import com.mycompany.myapp.service.mapper.IntentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing Intent.
 */
@Service
public class IntentServiceImpl implements IntentService{

    private final Logger log = LoggerFactory.getLogger(IntentServiceImpl.class);

    private final IntentRepository intentRepository;

    private final IntentMapper intentMapper;

    public IntentServiceImpl(IntentRepository intentRepository, IntentMapper intentMapper) {
        this.intentRepository = intentRepository;
        this.intentMapper = intentMapper;
    }

    /**
     * Save a intent.
     *
     * @param intentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public IntentDTO save(IntentDTO intentDTO) {
        log.debug("Request to save Intent : {}", intentDTO);
        Intent intent = intentMapper.toEntity(intentDTO);
        intent = intentRepository.save(intent);
        return intentMapper.toDto(intent);
    }

    /**
     *  Get all the intents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<IntentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Intents");
        return intentRepository.findAll(pageable)
            .map(intentMapper::toDto);
    }

    /**
     *  Get one intent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public IntentDTO findOne(String id) {
        log.debug("Request to get Intent : {}", id);
        Intent intent = intentRepository.findOne(id);
        return intentMapper.toDto(intent);
    }

    /**
     *  Delete the  intent by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Intent : {}", id);
        intentRepository.delete(id);
    }
}
