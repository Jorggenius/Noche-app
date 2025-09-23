package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.BadgeEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BadgeMapper {

    BadgeDTO toDTO(BadgeEntity entity);

    List<BadgeDTO> toDTOList(List<BadgeEntity> entities);

    @Mapping(target = "idBadge", ignore = true)
    @Mapping(target = "usuariosBadges", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BadgeEntity toEntity(BadgeDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idBadge", ignore = true)
    @Mapping(target = "usuariosBadges", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(BadgeDTO dto, @MappingTarget BadgeEntity entity);
}