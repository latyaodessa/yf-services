package yf.meta.services;

import yf.meta.entities.City;
import yf.meta.entities.Country;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MetaDataDao {

    @PersistenceContext
    private EntityManager em;

    public Country getCountryById(final Long id) {
        return em.find(Country.class, id);
    }

    public City getCityById(final Long id) {
        return em.find(City.class, id);
    }


}
