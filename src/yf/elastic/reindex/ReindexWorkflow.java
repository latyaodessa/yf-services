package yf.elastic.reindex;

import yf.dashboard.postphoto.entities.UserSavedPhotos;
import yf.dashboard.postphoto.entities.UserSavedPosts;
import yf.elastic.reindex.bulkworkflow.PhotoDashboardBulkWorkflow;
import yf.elastic.reindex.bulkworkflow.PostBulkWorkflow;
import yf.elastic.reindex.bulkworkflow.PostDashboardBulkWorkflow;
import yf.meta.bulkworkflow.CityBulkWorkflow;
import yf.meta.bulkworkflow.CountryBulkWorkflow;
import yf.meta.entities.City;
import yf.meta.entities.Country;
import yf.post.entities.Post;
import yf.publication.PublicationDao;
import yf.publication.bulkworkflow.PublicationBulkWorkflow;
import yf.publication.entities.Publication;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReindexWorkflow {

    public static final Logger LOG = Logger.getLogger(ReindexWorkflow.class.getName());
    @Inject
    private PostBulkWorkflow postReindexWorkflow;
    @Inject
    private PostDashboardBulkWorkflow postDashboardBulkWorkflow;
    @Inject
    private PhotoDashboardBulkWorkflow photoDashboardBulkWorkflow;
    @Inject
    private ElasticBulkFetcher elasticBulkFetcher;
    @Inject
    private PublicationBulkWorkflow publicationBulkWorkflow;
    @Inject
    private CityBulkWorkflow cityBulkWorkflow;
    @Inject
    private CountryBulkWorkflow countryBulkWorkflow;
    @Inject
    private PublicationDao publicationDao;

    public boolean reindexPosts() {

        List<Post> entities;
        int offset = 0;

        postReindexWorkflow.recreateIndex();

        do {
            entities = elasticBulkFetcher.fetchAllModels(offset,
                    Post.class);

            if (entities == null || entities.isEmpty()) {
                break;
            }
            postReindexWorkflow.execute(entities);
            offset += entities.size();

            LOG.info(String.format("Bulk Updating: Already updated %s posts",
                    offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
        return true;
    }

    public boolean reindexPublications() {

        List<Publication> entities;
        int offset = 0;

        publicationBulkWorkflow.recreateIndex();

        do {
            entities = elasticBulkFetcher.fetchAllModels(offset,
                    Publication.class);

            if (entities == null || entities.isEmpty()) {
                break;
            }
            publicationBulkWorkflow.execute(entities);
            offset += entities.size();

            LOG.info(String.format("Bulk Updating: Already updated %s publications",
                    offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
        return true;
    }

    public boolean reindexPublicationsIds(final List<Long> ids) {

        List<Publication> entities = ids.stream().map(publicationId -> publicationDao.getPublicationById(publicationId))
                .filter(Objects::nonNull).collect(Collectors.toList());

        publicationBulkWorkflow.execute(entities);

        LOG.info(String.format("Bulk Updating: Already updated %s publications",
                entities.size()));

        return true;
    }

    public boolean reindexDashboardPosts() {
        List<UserSavedPosts> entities;
        int offset = 0;

        postDashboardBulkWorkflow.recreateIndex();

        do {
            entities = elasticBulkFetcher.fetchAllModels(offset,
                    UserSavedPosts.class);

            if (entities == null || entities.isEmpty()) {
                break;
            }
            postDashboardBulkWorkflow.execute(entities);
            offset += entities.size();

            LOG.info(String.format("Bulk Updating: Already updated %s dashboard posts",
                    offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
        return true;
    }

    public boolean reindexDashboardPhotos() {
        List<UserSavedPhotos> entities;
        int offset = 0;

        photoDashboardBulkWorkflow.recreateIndex();

        do {
            entities = elasticBulkFetcher.fetchAllModels(offset,
                    UserSavedPhotos.class);

            if (entities == null || entities.isEmpty()) {
                break;
            }
            photoDashboardBulkWorkflow.execute(entities);
            offset += entities.size();

            LOG.info(String.format("Bulk Updating: Already updated %s dashboard photos",
                    offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
        return true;
    }

    public boolean reindexCountries() {
        List<Country> entities;
        int offset = 0;

        countryBulkWorkflow.recreateIndex();

        do {
            entities = elasticBulkFetcher.fetchAllModels(offset,
                    Country.class);

            if (entities == null || entities.isEmpty()) {
                break;
            }
            countryBulkWorkflow.execute(entities);
            offset += entities.size();

            LOG.info(String.format("Bulk Updating: Already updated %s countries",
                    offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
        return true;
    }

    public boolean reindexCities() {
        List<City> entities;
        int offset = 0;

        cityBulkWorkflow.recreateIndex();

        do {
            entities = elasticBulkFetcher.fetchAllModels(offset,
                    City.class);

            if (entities == null || entities.isEmpty()) {
                break;
            }
            cityBulkWorkflow.execute(entities);
            offset += entities.size();

            LOG.info(String.format("Bulk Updating: Already updated %s cities",
                    offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
        return true;
    }
}
