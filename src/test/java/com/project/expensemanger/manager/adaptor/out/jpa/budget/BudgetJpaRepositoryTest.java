package com.project.expensemanger.manager.adaptor.out.jpa.budget;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.expensemanger.core.config.QueryDSLConfig;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetUsageProjection;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.ExpenditureCustomRepositoryImpl;
import com.project.expensemanger.manager.application.service.dto.GetBudgetUsageCondition;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@Import({BudgetCustomRepositoryImpl.class, ExpenditureCustomRepositoryImpl.class, QueryDSLConfig.class})
@Transactional
class BudgetJpaRepositoryTest {

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
    BudgetJpaRepository sut;

    @Test
    @DisplayName("특정 기간 동안 사용자의 예산 및 지출 현황 조회 테스트 : 카테고리 조건 X")
    void find_total_expenditure_by_category_and_date_and_id_test() throws Exception {
        // given
        Long userId = 1L;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 3);

        GetBudgetUsageCondition condition = GetBudgetUsageCondition.builder()
                .startDate(startDate)
                .endDate(endDate)
                .userId(userId)
                .build();

        // when
        List<CategoryBudgetUsageProjection> result
                = sut.findTotalExpenditureByCategoryAndDateAndId(condition);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(5);

        CategoryBudgetUsageProjection category1 = result.stream()
                .filter(r -> r.categoryId().equals(1L))
                .findFirst()
                .orElseThrow();

        assertThat(category1.categoryName()).isEqualTo("식비");
        assertThat(category1.budgetAmount()).isEqualTo(200000);
        assertThat(category1.expenditureAmount()).isEqualTo(10000);

        CategoryBudgetUsageProjection category2 = result.stream()
                .filter(r -> r.categoryId().equals(2L))
                .findFirst()
                .orElseThrow();

        assertThat(category2.categoryName()).isEqualTo("교통비");
        assertThat(category2.budgetAmount()).isEqualTo(150000);
        assertThat(category2.expenditureAmount()).isEqualTo(4000);

        CategoryBudgetUsageProjection category3 = result.stream()
                .filter(r -> r.categoryId().equals(3L))
                .findFirst()
                .orElseThrow();

        assertThat(category3.categoryName()).isEqualTo("경조사비");
        assertThat(category3.budgetAmount()).isEqualTo(100000);
        assertThat(category3.expenditureAmount()).isEqualTo(8000);

        CategoryBudgetUsageProjection category4 = result.stream()
                .filter(r -> r.categoryId().equals(4L))
                .findFirst()
                .orElseThrow();

        assertThat(category4.categoryName()).isEqualTo("문화비");
        assertThat(category4.budgetAmount()).isEqualTo(120000);
        assertThat(category4.expenditureAmount()).isEqualTo(18000);

        CategoryBudgetUsageProjection category5 = result.stream()
                .filter(r -> r.categoryId().equals(5L))
                .findFirst()
                .orElseThrow();

        assertThat(category5.categoryName()).isEqualTo("기타");
        assertThat(category5.budgetAmount()).isEqualTo(80000);
        assertThat(category5.expenditureAmount()).isEqualTo(14000);
    }

    @Test
    @DisplayName("특정 기간 동안 사용자의 예산 및 지출 현황 조회 테스트 : 카테고리 조건 o")
    void find_total_expenditure_by_category_and_date_and_id_with_category_condition_test() throws Exception {
        // given
        Long userId = 1L;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 3);

        GetBudgetUsageCondition condition = GetBudgetUsageCondition.builder()
                .startDate(startDate)
                .endDate(endDate)
                .userId(userId)
                .categoryIdList(List.of(1L, 2L))
                .build();

        // when
        List<CategoryBudgetUsageProjection> result
                = sut.findTotalExpenditureByCategoryAndDateAndId(condition);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);

        CategoryBudgetUsageProjection category1 = result.stream()
                .filter(r -> r.categoryId().equals(1L))
                .findFirst()
                .orElseThrow();

        assertThat(category1.categoryName()).isEqualTo("식비");
        assertThat(category1.budgetAmount()).isEqualTo(200000);
        assertThat(category1.expenditureAmount()).isEqualTo(10000);

        CategoryBudgetUsageProjection category2 = result.stream()
                .filter(r -> r.categoryId().equals(2L))
                .findFirst()
                .orElseThrow();

        assertThat(category2.categoryName()).isEqualTo("교통비");
        assertThat(category2.budgetAmount()).isEqualTo(150000);
        assertThat(category2.expenditureAmount()).isEqualTo(4000);
    }

}