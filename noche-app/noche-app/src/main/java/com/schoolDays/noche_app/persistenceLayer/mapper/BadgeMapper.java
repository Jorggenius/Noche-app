package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.BadgeCreateDTO;
import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;
import com.schoolDays.noche_app.businessLayer.dto.BadgeUpdateDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.BadgeEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BadgeMapper {

    // Entity -> DTO
    BadgeDTO toDTO(BadgeEntity entity);

    List<BadgeDTO> toDTOList(List<BadgeEntity> entities);

    // CreateDTO -> Entity
    @Mapping(target = "idBadge", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    BadgeEntity toEntity(BadgeCreateDTO dto);

    // UpdateDTO -> Entity (ignora nulos)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(BadgeUpdateDTO dto, @MappingTarget BadgeEntity entity);
}