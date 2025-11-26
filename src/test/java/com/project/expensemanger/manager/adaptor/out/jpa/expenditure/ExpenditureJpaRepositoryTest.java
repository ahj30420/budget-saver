package com.project.expensemanger.manager.adaptor.out.jpa.expenditure;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.expensemanger.core.config.QueryDSLConfig;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.dto.GetExpenditureListCondition;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.entity.ExpenditureJpaEntity;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.projection.ExpenditureByCategoryProjection;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({ExpenditureCustomRepositoryImpl.class, QueryDSLConfig.class})
@Transactional
class ExpenditureJpaRepositoryTest {

    @Container
    static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("init_db.sql");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }

    @Autowired
    ExpenditureJpaRepository repo;

    GetExpenditureListCondition condition;

    @BeforeEach
    void init() {
        condition = GetExpenditureListCondition.builder()
                .userId(1L)
                .categoryId(1L)
                .startDate(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2025, 1, 11, 0, 0, 0))
                .minAmount(1000L)
                .maxAmount(50000L)
                .build();
    }

    @Test
    @DisplayName("조건에 맞는 지출 목록 조회")
    void get_expenditure_list_by_condition_test() throws Exception {
        // when
        List<ExpenditureJpaEntity> result = repo.findAllExpenditureByCondition(condition);

        // then
        assertThat(result).isNotEmpty();
        for (ExpenditureJpaEntity expenditureJpaEntity : result) {
            assertThat(expenditureJpaEntity.getUser().getId()).isEqualTo(1L);
            assertThat(expenditureJpaEntity.getCategory().getId()).isEqualTo(1L);
            assertThat(expenditureJpaEntity.getSpendAt()).isBetween(condition.startDate(), condition.endDate());
            assertThat(expenditureJpaEntity.getAmount()).isBetween(condition.minAmount(), condition.maxAmount());
        }
    }

    @Test
    @DisplayName("카테고리 별 지출 합계 조회")
    void get_total_expenditure_by_category_test() throws Exception {
        // when
        List<ExpenditureByCategoryProjection> result = repo.findTotalExpenditureByCategory(condition);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).isInstanceOf(List.class);
    }
}