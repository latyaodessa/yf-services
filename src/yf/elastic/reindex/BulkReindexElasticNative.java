//package yf.elastic.reindex;
//
//import java.lang.reflect.ParameterizedType;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.function.Function;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//
//import javax.inject.Inject;
//
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.elasticsearch.ElasticsearchException;
//import org.elasticsearch.ElasticsearchIllegalStateException;
//import org.elasticsearch.action.ListenableActionFuture;
//import org.elasticsearch.action.admin.indices.flush.FlushAction;
//import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
//import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
//import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
//import org.elasticsearch.action.bulk.BulkRequestBuilder;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.index.IndexRequestBuilder;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.update.UpdateRequestBuilder;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.common.base.Joiner;
//import org.elasticsearch.index.IndexService;
//
//import com.cargopartner.spot.domainmodel.addresses.core.AbstractEntityWithoutId;
//import com.cargopartner.spot.ejb.fulltextsearch.IndexService.AsyncIndexDto;
//import com.cargopartner.spot.ejb.util.credentials.BackendCredentials;
//import com.cargopartner.spot.restclient.JaxbObjectMapper;
//import com.cargopartner.spot.serviceinterface.core.exception.BusinessException;
//import com.cargopartner.spot.serviceinterface.core.exception.TechnicalException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//
//import vk.logic.elastic.ElasticSingleton;
//
//@SuppressWarnings({"PMD.GodClass",
//                   "PMD.AvoidDuplicateLiterals",
//                   "PMD.EmptyMethodInAbstractClassShouldBeAbstract",
//                   "PMD.CyclomaticComplexity",
//                   "PMD.ModifiedCyclomaticComplexity",
//                   "PMD.StdCyclomaticComplexity",
//                   "PMD.NPathComplexity" })
//public abstract class BulkReindexElasticNative {
//    private static final Logger LOG = Logger.getLogger(BulkReindexElasticNative.class.getName());
//
//    private static final int MIN_BULK_SIZE = 1;
//    private static final int VERSION_CONFLICT_RETRY = 5;
//
//    protected static final String ELASTIC_TYPE_STRING = "string";
//    protected static final String ELASTIC_TYPE_LONG = "long";
//    protected static final String ELASTIC_TYPE_BOOLEAN = "boolean";
//    protected static final String ELASTIC_TYPE_NESTED = "nested";
//    protected static final String ELASTIC_TYPE_DATE = "date";
//
//    @Inject
//    private ElasticSingleton elastic;
//    @Inject
//    private IndexService indexService;
//    @Inject
//    private BackendCredentials credentials;
//
//    private final Class<V> dtoClass;
//
//    public BulkReindexElasticNative() {
//        super();
//        dtoClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
//    }
//
//    protected Client getElasticClient() {
//        return elastic.getClient();
//    }
//
//    protected IndexRequestBuilder prepareIndex(final T entity,
//                                               final String entityId,
//                                               final Client client)
//            throws JsonProcessingException {
//        final V dto = toDto(entity);
//        if (dto == null) {
//            return null;
//        }
//        return client.prepareIndex(ElasticSingleton.SPOT_INDEX, getIndexName(), entityId).setSource(new JaxbObjectMapper().writeValueAsString(dto));
//    }
//
//    protected String getIndexName() {
//        return getEntityClass().getSimpleName();
//    }
//
//    protected String getParentId(final T entity) {
//        // Override, if needed
//        return null;
//    }
//
//    protected String getRoutingId(final T entity) {
//        // Override, if needed
//        return null;
//    }
//
//    protected void createChildParentMapping(final Client client) {
//        // Override, if needed
//    }
//
//    protected void fetchAllModelsRelations(final List<T> entities) {
//        // Override, if needed
//    }
//
//    protected void handleConsequences(final T entity) {
//        // Override, if needed
//    }
//
//    protected T fetchModel(final T entity) {
//        return entity;
//    }
//
//    protected String getDocumentId(final T entity) {
//        return String.valueOf(entity.getId());
//    }
//
//    public void executeLater(final T entity) {
//        indexService.indexLater(new AsyncIndexDto(entity.getId(),
//                                                  entity.getClass(),
//                                                  this.getClass(),
//                                                  credentials));
//    }
//
//    public void execute(final T entity) {
//
//        em.flush();
//
//        LOG.info(String.format("ES Indexing: Starting indexing %s (ID=%s)", getEntityClass().getSimpleName(), entity.getId()));
//        try (Client client = getElasticClient()) {
//
//            final T indexableEntity = fetchModel(entity);
//
//            if (indexableEntity == null) {
//                return;
//            }
//
//            final List<T> entities = new ArrayList<>();
//            entities.add(indexableEntity);
//            fetchAllModelsRelations(entities);
//
//            IndexRequestBuilder indexBuilder = prepareIndex(indexableEntity, getDocumentId(entity), client);
//            if (indexBuilder == null) {
//                return;
//            }
//            setParentRelation(indexableEntity, indexBuilder);
//            ListenableActionFuture<IndexResponse> action = indexBuilder.setRefresh(true).execute();
//            action.actionGet();
//
//            if (action.isCancelled()) {
//                LOG.info("ES Indexing: Update of index was not completed successfully !");
//            }
//        } catch (JsonProcessingException | ElasticsearchException e) {
//            LOG.severe(e.getMessage());
//            throw new BusinessException(e);
//        }
//
//        handleConsequences(entity);
//
//        LOG.info(String.format("ES Indexing: Finished indexing %s (ID=%s)", getEntityClass().getSimpleName(), entity.getId()));
//    }
//
//    @SuppressWarnings("PMD")
//    public Exception execute() {
//        try {
//            execute(offset -> fetchAllModels(offset), true);
//            return null;
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, e.getMessage(), e);
//            return e;
//        }
//    }
//
//    public void execute(final Function<Integer, List<T>> entitiesSupplier) {
//        execute(entitiesSupplier, false);
//    }
//
//    private void execute(final Function<Integer, List<T>> entitiesSupplier,
//                         final boolean reallyAll) {
//        LOG.info(String.format("ES Bulk Indexing: Starting indexing mapping of type %s", getEntityClass().getSimpleName()));
//        if (reallyAll) {
//            deleteMapping();
//            // createMapping();
//        }
//
//        setBulkSize(getDefaultBulkSize());
//
//        int offset = 0;
//        List<T> entities;
//
//        try (Client client = getElasticClient()) {
//
//            if (reallyAll) {
//                createChildParentMapping(client);
//                createTermsMapping(client);
//            }
//
//            int successfullyIndexed = 0;
//            do {
//                entities = entitiesSupplier.apply(offset);
//
//                if (entities == null || entities.isEmpty()) {
//                    break;
//                }
//
//                fetchAllModelsRelations(entities);
//
//                BulkRequestBuilder bulkRequest = client.prepareBulk();
//                for (T entity : entities) {
//                    addEntityToBulk(client, bulkRequest, entity);
//                }
//
//                if (entities.size() < getBulkSize()) {
//                    bulkRequest.setRefresh(true);
//                }
//
//                // skip, if we have nothing to index, but move the offset
//                if (bulkRequest.numberOfActions() == 0) {
//                    offset += entities.size();
//                    continue;
//                }
//
//                ListenableActionFuture<BulkResponse> future = bulkRequest.execute();
//
//                successfullyIndexed = handleBulkRequest(offset, entities, client, future);
//                offset += successfullyIndexed;
//                if (reallyAll) {
//                    em.clear();
//                }
//            } while (successfullyIndexed == 0 || entities.size() >= getBulkSize());
//
//        } catch (JsonProcessingException e) {
//            LOG.log(Level.SEVERE, e.getMessage(), e);
//        }
//
//        LOG.info(String.format("ES Bulk Indexing: Finished indexing mapping of type %s", getEntityClass().getSimpleName()));
//    }
//
//    private int handleBulkRequest(final int offset,
//                                  final List<T> entities,
//                                  final Client client,
//                                  final ListenableActionFuture<BulkResponse> future) {
//        int successfullyIndexed;
//        try {
//            successfullyIndexed = waitForBulkRequest(entities, client, future);
//
//            if (getBulkSize() < getDefaultBulkSize()) {
//                setBulkSize(getBulkSize() * 10);
//            }
//            LOG.info(String.format("ES Bulk Indexing: %d records of type %s has been indexed", entities.size(), getEntityClass().getSimpleName()));
//
//        } catch (TechnicalException ex) {
//            successfullyIndexed = 0;
//            handleElasticException(offset, entities, ex);
//        }
//        return successfullyIndexed;
//    }
//
//    private void handleElasticException(final int offset,
//                                        final List<T> entities,
//                                        final TechnicalException ex) {
//        if (Thread.interrupted()) {
//            LOG.severe("ES Bulk Indexing: Thread has been interrupted");
//        } else {
//            LOG.severe("ES Bulk Indexing: Thread has not been interrupted");
//        }
//        LOG.severe(String.format("ES %s Indexing: Failed @ offset = %d, bulk size = %d", getEntityClass().getSimpleName(), offset, getBulkSize()));
//        LOG.severe(ex.getMessage());
//
//        if (getBulkSize() > MIN_BULK_SIZE) {
//            setBulkSize(getBulkSize() / 10);
//            LOG.severe(String.format("ES %s Indexing: Continuing with bulk size = %d", getEntityClass().getSimpleName(), getBulkSize()));
//        } else {
//            final String givingUpMessage = String.format("ES %s Indexing: Giving up @ offset = %d, bulk size = %d, entityIDs = %s",
//                    getEntityClass().getSimpleName(), offset, getBulkSize(),
//                    Joiner.on(", ").join(entities.stream().map(T::getId).collect(Collectors.toList())));
//            LOG.severe(givingUpMessage);
//            throw (TechnicalException) new TechnicalException(givingUpMessage.concat("\n\n" + ex.getMessage())).initCause(ex);
//        }
//    }
//
//    private int waitForBulkRequest(final List<T> entities,
//                                   final Client client,
//                                   final ListenableActionFuture<BulkResponse> future)
//            throws TechnicalException {
//        int entitiesIndexed = 0;
//        try {
//
//            if (Thread.interrupted()) {
//                LOG.severe("Thread has been interrupted before results were collected");
//            }
//            BulkResponse bulkResponse = future.actionGet();
//
//            if (bulkResponse.hasFailures()) {
//                LOG.severe(bulkResponse.buildFailureMessage());
//                throw new TechnicalException(bulkResponse.buildFailureMessage());
//            }
//
//            entitiesIndexed += entities.size();
//
//            if (Thread.interrupted()) {
//                LOG.severe("Thread has been interrupted after results were collected");
//            }
//            FlushAction.INSTANCE.newRequestBuilder(client.admin().indices()).get();
//            return entitiesIndexed;
//        } catch (ElasticsearchIllegalStateException ex) {
//            throw (TechnicalException) new TechnicalException(ExceptionUtils.getStackTrace(ex)).initCause(ex);
//        }
//    }
//
//    private void addEntityToBulk(final Client client,
//                                 final BulkRequestBuilder bulkRequest,
//                                 final T entity)
//            throws JsonProcessingException {
//        IndexRequestBuilder indexBuilder = prepareIndex(entity, getDocumentId(entity), client);
//        // skip, if the IndexDTO is null
//        if (indexBuilder == null) {
//            return;
//        }
//        setParentRelation(entity, indexBuilder);
//        bulkRequest.add(indexBuilder);
//    }
//
//    private void setParentRelation(final T entity,
//                                   final IndexRequestBuilder indexBuilder) {
//        final String parentId = getParentId(entity);
//        if (parentId != null) {
//            indexBuilder.setParent(parentId);
//            indexBuilder.setRouting(parentId);
//        }
//        final String routingId = getRoutingId(entity);
//        if (routingId != null) {
//            indexBuilder.setRouting(routingId);
//        }
//    }
//
//    public void delete(final Long entityId) {
//        try (Client client = getElasticClient()) {
//            client.prepareDelete(ElasticSingleton.SPOT_INDEX, getIndexName(), entityId.toString()).setRefresh(true).execute().actionGet();
//        }
//    }
//
//    public void delete(final List<String> ids) {
//
//        if (ids.isEmpty()) {
//            return;
//        }
//
//        try (Client client = getElasticClient()) {
//
//            LOG.log(Level.INFO, "Deleting indexes from {0}: {1}", new Object[] {getIndexName(),
//                                                                                ids.stream().collect(Collectors.joining(", ")) });
//            BulkRequestBuilder bulkRequest = client.prepareBulk();
//            for (String id : ids) {
//                bulkRequest.add(client.prepareDelete(ElasticSingleton.SPOT_INDEX, getIndexName(), id));
//            }
//            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
//            if (bulkResponse.hasFailures()) {
//                LOG.severe(bulkResponse.buildFailureMessage());
//            } else {
//                LOG.log(Level.INFO, "Deleting operation failed for entity {0} and ids: {1}", new Object[] {getIndexName(),
//                                                                                                           ids.stream().collect(Collectors.joining(", ")) });
//            }
//        }
//    }
//
//    protected void deleteMapping() {
//
//        LOG.info(String.format("Starting deleting mapping of type %s", getIndexName()));
//        try (Client client = getElasticClient()) {
//
//            DeleteMappingRequest mappingRequest = new DeleteMappingRequest(ElasticSingleton.SPOT_INDEX);
//            mappingRequest.types(getIndexName());
//
//            client.admin().indices().deleteMapping(mappingRequest).actionGet();
//
//            LOG.info(String.format("Finished deleting mapping of type %s", getIndexName()));
//        } catch (Exception e) {
//            LOG.severe(e.getMessage());
//        }
//    }
//
//    protected void createMapping() {
//
//        LOG.info(String.format("Starting creating mapping of type %s", getIndexName()));
//        try (Client client = getElasticClient()) {
//
//            PutMappingRequestBuilder mappingRequestBuilder = new PutMappingRequestBuilder(client.admin().indices());
//
//            mappingRequestBuilder.setIndices(ElasticSingleton.SPOT_INDEX).setType(getIndexName()).setSource("");
//
//            client.admin().indices().putMapping(mappingRequestBuilder.request()).actionGet();
//
//            LOG.info(String.format("Finished creating mapping of type %s", getIndexName()));
//        } catch (Exception e) {
//            LOG.severe(e.getMessage());
//        }
//    }
//
//    /**
//     * declare fields that should not be analyzed in order to make them searchable via term queries/filters
//     * it applies to 'String' fields
//     */
//    protected void createTermsMapping(final Client client) {
//        Map<String, String> fieldMapping = getFieldAndTypeMapping();
//        client.admin().indices().putMapping(new PutMappingRequest(ElasticSingleton.SPOT_INDEX).type(getIndexName())
//                .source(getNotAnalyzedFields(getIndexName(), fieldMapping.keySet(), fieldMapping)).ignoreConflicts(true)).actionGet();
//    }
//
//    protected Set<String> getFieldsSearchableBySubstring() {
//        return new HashSet<>();
//    }
//
//    protected Map<String, String> getFieldAndTypeMapping() {
//        Set<String> strings = new HashSet<>();
//        ElasticListFragmentSearchWorkflow.getStringFieldsRecursively(dtoClass, strings);
//        Map<String, String> fieldMapping = new HashMap<>();
//        strings.forEach(s -> fieldMapping.put(s, ELASTIC_TYPE_STRING));
//        return fieldMapping;
//    }
//
//    protected abstract V toDto(T entity) throws JsonProcessingException;
//
//    protected String getNotAnalyzedFields(final String document,
//                                          final Set<String> fieldNames,
//                                          final Map<String, String> fieldTypes) {
//        try {
//            Map<String, Object> fields = new HashMap<>();
//            for (String fieldName : fieldNames) {
//                getMultifieldSetting(fields, fieldName, fieldName, fieldTypes.get(fieldName));
//            }
//            Map<String, Object> properties = new HashMap<>();
//            properties.put("properties", fields);
//            Map<String, Object> documentMap = new HashMap<>();
//            documentMap.put(document, properties);
//            return new JaxbObjectMapper().writeValueAsString(documentMap);
//        } catch (JsonProcessingException e) {
//            return null;
//        }
//    }
//
//    private Map<String, Object> getIndexMap(final String fieldType) {
//        Map<String, Object> index = new HashMap<>();
//        index.put("index", "not_analyzed");
//        index.put("type", fieldType == null ? ELASTIC_TYPE_STRING
//                                            : fieldType);
//        return index;
//    }
//
//    private Map<String, Object> getAnalyzerMap(final String fieldType,
//                                               final String indexAnalyzer,
//                                               final String searchAnalyzer) {
//        Map<String, Object> index = new HashMap<>();
//        index.put("type", fieldType == null ? ELASTIC_TYPE_STRING
//                                            : fieldType);
//        index.put("index_analyzer", indexAnalyzer);
//        index.put("search_analyzer", searchAnalyzer);
//        return index;
//    }
//
//    private Map<String, Object> getAnalyzerMap(final String fieldType,
//                                               final String analyzer) {
//        Map<String, Object> index = new HashMap<>();
//        index.put("type", fieldType == null ? ELASTIC_TYPE_STRING
//                                            : fieldType);
//        index.put("analyzer", analyzer);
//        return index;
//    }
//
//    private Map<String, Object> getMultifieldWrapper(final String fullFieldName,
//                                                     final String fieldType) {
//        Map<String, Object> raw = new HashMap<>();
//        raw.put("raw", getIndexMap(fieldType));
//        raw.put("sort", getAnalyzerMap(fieldType, "sort"));
//        if (getFieldsSearchableBySubstring().contains(fullFieldName)) {
//            raw.put("ngrams", getAnalyzerMap(fieldType, "ngrams", "search"));
//        }
//        return raw;
//    }
//
//    @SuppressWarnings("unchecked")
//    private void getMultifieldSetting(final Map<String, Object> hierarchy,
//                                      final String fullFieldPath,
//                                      final String fieldPath,
//                                      final String fieldType) {
//
//        if (fieldPath.contains(".")) {
//
//            Map<String, Object> raw = new HashMap<>();
//            Map<String, Object> properties = new HashMap<>();
//            properties.put("properties", raw); // { "properties" : <raw> }
//
//            String fieldName = fieldPath.substring(0, fieldPath.indexOf('.'));
//            if (hierarchy.containsKey(fieldName)) {
//                Map<String, Object> existingMapping = (Map<String, Object>) hierarchy.get(fieldName);
//                if (existingMapping.containsKey("properties")) {
//                    getMultifieldSetting((Map<String, Object>) existingMapping.get("properties"), fullFieldPath,
//                            fieldPath.substring(fieldPath.indexOf('.') + 1), fieldType);
//                    return;
//                }
//                properties.putAll(existingMapping);
//            }
//            hierarchy.put(fieldName, properties); // { "field_name" : { "properties" : <raw> } }
//
//            getMultifieldSetting(raw, fullFieldPath, fieldPath.substring(fieldPath.indexOf('.') + 1), fieldType);
//            return;
//        }
//
//        String type = fieldType == null ? ELASTIC_TYPE_STRING
//                                        : fieldType;
//
//        Map<String, Object> multiField = new HashMap<>();
//
//        if (!ELASTIC_TYPE_NESTED.equals(fieldType)) {
//            multiField.put("fields", getMultifieldWrapper(fullFieldPath, type));
//        }
//        multiField.put("type", type);
//
//        if (hierarchy.containsKey(fieldPath)) {
//            Map<String, Object> existingMapping = (Map<String, Object>) hierarchy.get(fieldPath);
//            multiField.putAll(existingMapping);
//        }
//        hierarchy.put(fieldPath, multiField);
//
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////
//
//    protected UpdateRequestBuilder prepareUpdate(final T entity,
//                                                 final String entityId,
//                                                 final Client client)
//            throws JsonProcessingException {
//        final V dto = toDto(entity);
//        if (dto == null) {
//            return null;
//        }
//        return client.prepareUpdate(ElasticSingleton.SPOT_INDEX, getIndexName(), entityId).setDoc(new JaxbObjectMapper().writeValueAsString(dto))
//                .setRetryOnConflict(VERSION_CONFLICT_RETRY);
//    }
//
//    private void addEntityPartialUpdateToBulk(final Client client,
//                                              final BulkRequestBuilder bulkRequest,
//                                              final T entity)
//            throws JsonProcessingException {
//        UpdateRequestBuilder indexBuilder = prepareUpdate(entity, getDocumentId(entity), client);
//        // skip, if the IndexDTO is null
//        if (indexBuilder == null) {
//            return;
//        }
//        bulkRequest.add(indexBuilder);
//    }
//
//    public void executePartialUpdate(final T entity) {
//        em.flush();
//        LOG.info(String.format("ES Updating: Starting updating %s (ID=%s)", getEntityClass().getSimpleName(), entity.getId()));
//        try (Client client = getElasticClient()) {
//
//            final T indexableEntity = fetchModel(entity);
//
//            if (indexableEntity == null) {
//                return;
//            }
//
//            final List<T> entities = new ArrayList<>();
//            entities.add(indexableEntity);
//            fetchAllModelsRelations(entities);
//
//            UpdateRequestBuilder updateBuilder = prepareUpdate(indexableEntity, getDocumentId(entity), client);
//            if (updateBuilder == null) {
//                return;
//            }
//            ListenableActionFuture<UpdateResponse> action = updateBuilder.setRefresh(true).execute();
//            action.actionGet();
//
//            if (action.isCancelled()) {
//                LOG.info("ES Updating: Update of index was not completed successfully !");
//            }
//        } catch (JsonProcessingException | ElasticsearchException e) {
//            LOG.severe(e.getMessage());
//            throw new BusinessException(e);
//        }
//
//        handleConsequences(entity);
//
//        LOG.info(String.format("ES Updating: Finished updating %s (ID=%s)", getEntityClass().getSimpleName(), entity.getId()));
//    }
//
//    protected void executePartialBulkUpdate(final Function<Integer, List<T>> entitiesSupplier) {
//        LOG.info(String.format("ES Bulk Updating: Starting updating mapping of type %s", getEntityClass().getSimpleName()));
//
//        setBulkSize(getDefaultBulkSize());
//
//        int offset = 0;
//        List<T> entities;
//
//        try (Client client = getElasticClient()) {
//
//            int successfullyIndexed = 0;
//            do {
//                entities = entitiesSupplier.apply(offset);
//
//                if (entities == null || entities.isEmpty()) {
//                    break;
//                }
//
//                fetchAllModelsRelations(entities);
//
//                BulkRequestBuilder bulkRequest = client.prepareBulk();
//                for (T entity : entities) {
//                    addEntityPartialUpdateToBulk(client, bulkRequest, entity);
//                }
//
//                if (entities.size() < getBulkSize()) {
//                    bulkRequest.setRefresh(true);
//                }
//
//                // skip, if we have nothing to index, but move the offset
//                if (bulkRequest.numberOfActions() == 0) {
//                    offset += entities.size();
//                    continue;
//                }
//
//                ListenableActionFuture<BulkResponse> future = bulkRequest.execute();
//
//                successfullyIndexed = handleBulkRequest(offset, entities, client, future);
//                offset += successfullyIndexed;
//            } while (successfullyIndexed == 0 || entities.size() >= getBulkSize());
//
//        } catch (JsonProcessingException e) {
//            LOG.log(Level.SEVERE, e.getMessage(), e);
//        }
//
//        LOG.info(String.format("ES Bulk Updating: Finished updating mapping of type %s", getEntityClass().getSimpleName()));
//    }
//
//}
