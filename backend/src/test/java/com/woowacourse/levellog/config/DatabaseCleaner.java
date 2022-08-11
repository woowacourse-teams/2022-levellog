package com.woowacourse.levellog.config;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {

    private static final String CONSTRAINT_OFF_SQL = "SET REFERENTIAL_INTEGRITY FALSE";
    private static final String CONSTRAINT_ON_SQL = "SET REFERENTIAL_INTEGRITY TRUE";
    private static final String TRUNCATE_TABLE_SQL = "TRUNCATE TABLE ";
    private static final String SHOW_TABLES_SQL = "SHOW TABLES";

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        this.tableNames = findTableNamesByEntities();
    }

    private List<String> findTableNamesByEntities() {
        final List<Object[]> tables = (List<Object[]>) entityManager.createNativeQuery(SHOW_TABLES_SQL)
                .getResultList();

        return tables.stream()
                .map(it -> it[0].toString())
                .collect(Collectors.toList());
    }

    @Transactional
    public void clean() {
        entityManager.flush();
        entityManager.createNativeQuery(CONSTRAINT_OFF_SQL).executeUpdate();

        for (final String tableName : tableNames) {
            entityManager.createNativeQuery(TRUNCATE_TABLE_SQL + tableName).executeUpdate();
        }
        entityManager.createNativeQuery(CONSTRAINT_ON_SQL).executeUpdate();
    }
}
