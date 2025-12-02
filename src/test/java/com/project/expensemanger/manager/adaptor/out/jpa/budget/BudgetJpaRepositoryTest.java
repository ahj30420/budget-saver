package com.project.expensemanger.manager.adaptor.out.jpa.budget;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.expensemanger.core.config.QueryDSLConfig;
import com.project.expensemanger.manager.adaptor.out.jpa.budget.projection.CategoryBudgetUsageProjection;
import com.project.expensemanger.manager.adaptor.out.jpa.expenditure.ExpenditureCustomRepositoryImpl;
import java.time.LocalDate;
import java.util.List;
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
    @DisplayName("특정 기간 동안 사용자의 예산 및 지출 현황 조회 테스트")
    void find_total_expenditure_by_category_and_date_and_id_test() throws Exception {
        // given
        Long userId = 1L;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 3);

        // when
        List<CategoryBudgetUsageProjection> result
                = sut.findTotalExpenditureByCategoryAndDateAndId(startDate, endDate, userId);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(5);

        CategoryBudgetUsageProjection category1 = result.stream()
                .filter(r -> r.getCategoryId().equals(1L))
                .findFirst()
                .orElseThrow();

        assertThat(category1.getCategoryName()).isEqualTo("식비");
        assertThat(category1.getBudgetAmount()).isEqualTo(200000);
        assertThat(category1.getExpenditureAmount()).isEqualTo(10000);


        CategoryBudgetUsageProjection category2 = result.stream()
                .filter(r -> r.getCategoryId().equals(2L))
                .findFirst()
                .orElseThrow();

        assertThat(category2.getCategoryName()).isEqualTo("교통비");
        assertThat(category2.getBudgetAmount()).isEqualTo(150000);
        assertThat(category2.getExpenditureAmount()).isEqualTo(4000);


        CategoryBudgetUsageProjection category3 = result.stream()
                .filter(r -> r.getCategoryId().equals(3L))
                .findFirst()
                .orElseThrow();

        assertThat(category3.getCategoryName()).isEqualTo("경조사비");
        assertThat(category3.getBudgetAmount()).isEqualTo(100000);
        assertThat(category3.getExpenditureAmount()).isEqualTo(8000);


        CategoryBudgetUsageProjection category4 = result.stream()
                .filter(r -> r.getCategoryId().equals(4L))
                .findFirst()
                .orElseThrow();

        assertThat(category4.getCategoryName()).isEqualTo("문화비");
        assertThat(category4.getBudgetAmount()).isEqualTo(120000);
        assertThat(category4.getExpenditureAmount()).isEqualTo(18000);


        CategoryBudgetUsageProjection category5 = result.stream()
                .filter(r -> r.getCategoryId().equals(5L))
                .findFirst()
                .orElseThrow();

        assertThat(category5.getCategoryName()).isEqualTo("기타");
        assertThat(category5.getBudgetAmount()).isEqualTo(80000);
        assertThat(category5.getExpenditureAmount()).isEqualTo(14000);
    }

}