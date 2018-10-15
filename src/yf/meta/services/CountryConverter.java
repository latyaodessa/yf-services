package yf.meta.services;

import yf.meta.dtos.CountryDTO;
import yf.meta.entities.Country;

public class CountryConverter {

    public CountryDTO toDto(final Country entity) {
        CountryDTO dto = new CountryDTO();
        dto.setId(entity.getId());
        dto.setTitleRu(entity.getTitleRu());
        dto.setTitleEn(entity.getTitleEn());
        return dto;
    }
}
