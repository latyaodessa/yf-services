package yf.settings;

import yf.core.entities.AbstractDateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "system_settings")
@NamedQueries({@NamedQuery(name = SystemSettings.QUERY_GET_SS_BY_KEY, query = "SELECT t FROM SystemSettings t WHERE t.key = :key ") })
public class SystemSettings extends AbstractDateEntity {

    public static final String QUERY_GET_SS_BY_KEY = "SystemSettings.getSSByKey";

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(unique = true)
    private String key;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
