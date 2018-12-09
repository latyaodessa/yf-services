package yf.meta.services;

import yf.meta.dtos.CountryDTO;
import yf.meta.entities.Country;
import yf.submission.dtos.CountryFrontendDto;

public class CountryConverter {

    public CountryDTO toDto(final Country entity) {
        CountryDTO dto = new CountryDTO();
        dto.setId(entity.getId());
        dto.setTitleRu(entity.getTitleRu());
        dto.setTitleEn(entity.getTitleEn());
        return dto;
    }

    public CountryFrontendDto toFrontendDto(final Country entity){
        CountryFrontendDto dto = new CountryFrontendDto();
        dto.setKey(entity.getId());
        dto.setValue(entity.getTitleRu());
        dto.setText(entity.getTitleRu());
        return dto;
    }
}
