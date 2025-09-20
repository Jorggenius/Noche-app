package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.BadgeEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
@NoArgsConstructor
@AllArgsConstructor
public interface BadgeMapper {

    // Entity -> DTO
    BadgeDTO toDTO(BadgeEntity entity);

    List<BadgeDTO> toDTOList(List<BadgeEntity> entities);

    // DTO -> Entity
    @Mapping(target = "idBadge", ignore = true)
    @Mapping(target = "usuariosBadges", ignore = true)
    BadgeEntity toEntity(BadgeDTO dto);

    // Update (usa el mismo DTO, ignorando nulos)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idBadge", ignore = true)
    @Mapping(target = "usuariosBadges", ignore = true)
    void updateEntityFromDTO(BadgeDTO dto, @MappingTarget BadgeEntity entity);
}