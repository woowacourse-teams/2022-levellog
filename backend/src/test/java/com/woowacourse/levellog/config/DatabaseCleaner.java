package com.woowacourse.levellog.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        this.tableNames = findTableNamesByEntities();
    }

    private List<String> findTableNamesByEntities() {
        final List<String> tableNames = new ArrayList<>();

        final Set<EntityType<?>> entities = entityManager.getMetamodel()
                .getEntities();

        for (final EntityType<?> entity : entities) {
            final Table t = entity.getJavaType()
                    .getAnnotation(Table.class);

            if (haveNoCustomTableName(t)) {
                tableNames.add(toSnakeCase(entity.getName()));
            } else {
                tableNames.add(t.name());
            }
        }
        return tableNames;
    }

    private boolean haveNoCustomTableName(final Table t) {
        return t == null || t.name().isBlank();
    }

    private String toSnakeCase(final String text) {
        final String regex = "([a-z])([A-Z]+)";
        final String replacement = "$1_$2";

        return text.replaceAll(regex, replacement)
                .toLowerCase();
    }

    @Transactional
    public void clean() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (final String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1")
                    .executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
