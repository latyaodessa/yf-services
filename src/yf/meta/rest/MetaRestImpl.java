package yf.meta.rest;

import yf.meta.dtos.CityDTO;
import yf.meta.dtos.CountryDTO;
import yf.meta.services.MetaService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("meta")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class MetaRestImpl {

    @Inject
    private MetaService metaService;

    @POST
    @Path("countries")
    public List<CountryDTO> searchCountry(final String countrySearchText) {
        return metaService.searchCountry(countrySearchText);
    }

    @POST
    @Path("cities/{countryId}")
    public List<CityDTO> searchCitiesOfCountry(@PathParam("countryId") final String countryId,
                                               final String countrySearchText) {
        return metaService.searchCitiesOfCountry(countryId,
                countrySearchText);
    }
}
