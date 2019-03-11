package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.IntentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Intent and its DTO IntentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IntentMapper extends EntityMapper <IntentDTO, Intent> {
    
    

}
