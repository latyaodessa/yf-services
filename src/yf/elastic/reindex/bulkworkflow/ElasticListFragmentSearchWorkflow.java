package yf.elastic.reindex.bulkworkflow;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;


public abstract class ElasticListFragmentSearchWorkflow<T> {


    public static void getStringFieldsRecursively(final Class<?> clazz,
                                                  final Set<String> stringFields) {
        getStringFieldsRecursively(clazz, stringFields, "");
    }

    public static void getStringFieldsRecursively(final Class<?> clazz,
                                                   final Set<String> stringFields,
                                                   final String prefix) {
        if (clazz == null || clazz == Class.class || clazz == Object.class) {
            return;
        }
        getStringFieldsRecursively(clazz.getSuperclass(), stringFields, prefix);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(String.class) || field.getType().isEnum()) {
                stringFields.add(prefix.concat(field.getName()));
            } else if (Collection.class.isAssignableFrom(field.getType())) {
                ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                Class<?> collectionActualType = (Class<?>) stringListType.getActualTypeArguments()[0];
                if (collectionActualType.equals(String.class) || collectionActualType.isEnum()) {
                    stringFields.add(prefix.concat(field.getName()));
                } else if (collectionActualType.getSimpleName().toUpperCase(Locale.ROOT).endsWith("DTO") && !collectionActualType.equals(clazz)) {
                    getStringFieldsRecursively(collectionActualType, stringFields,
                            Optional.ofNullable(prefix).orElse(StringUtils.EMPTY).concat(field.getName().concat(".")));
                }
            } else if (field.getType().getSimpleName().toUpperCase(Locale.ROOT).endsWith("DTO") && !field.getType().equals(clazz)) {
                getStringFieldsRecursively(field.getType(), stringFields,
                        Optional.ofNullable(prefix).orElse(StringUtils.EMPTY).concat(field.getName().concat(".")));
            }
        }
    }

//    public Map<String, String> createSortMapping(final Map<String, String> mapping) {
//        HashSet<String> strings = new HashSet<>();
//        ElasticListFragmentSearchWorkflow.getStringFieldsRecursively(getReturnDtoClass(), strings);
//        Map<String, String> sortMapping = new HashMap<>();
//        sortMapping.putAll(mapping);
//        strings.forEach(s -> {
//            if (sortMapping.containsKey(s)) {
//                sortMapping.put(s + ".sort", sortMapping.get(s) + ".sort");
//            }
//        });
//        return sortMapping;
//    }
//
//    protected String prepareQueryString(final String searchText) {
//        return prepareQueryString(searchText, null);
//    }
//
//    protected String prepareQueryString(final String searchText,
//                                        final String suffix) {
//        String replacedString = searchText.replaceAll(NGRAM_SEARCH_REGEX, " ");
//        StringJoiner stringJoiner = new StringJoiner(" AND ");
//        for (String str : replacedString.split(" ")) {
//            if (StringUtils.isEmpty(str)) {
//                continue;
//            }
//            stringJoiner.add(String.format("%s%s", str, Optional.ofNullable(suffix).orElse(StringUtils.EMPTY)));
//        }
//        return stringJoiner.toString();
//    }
}