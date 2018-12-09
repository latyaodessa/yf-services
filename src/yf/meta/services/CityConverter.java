package yf.meta.services;

import yf.meta.dtos.CityDTO;
import yf.meta.entities.City;

public class CityConverter {

    public CityDTO toDto(final City entity) {
        CityDTO dto = new CityDTO();
        dto.setId(entity.getId());
        dto.setTitleRu(entity.getTitleRu());
        dto.setTitleEn(entity.getTitleEn());
        dto.setCountryId(entity.getCountry()
                .getId());
        return dto;
    }
}
