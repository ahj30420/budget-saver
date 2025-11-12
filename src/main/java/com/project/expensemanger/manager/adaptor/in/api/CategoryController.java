package com.project.expensemanger.manager.adaptor.in.api;

import com.project.expensemanger.core.common.util.UrlCreator;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterCategoryRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.CategoryIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.mapper.CategoryMapper;
import com.project.expensemanger.manager.adaptor.in.api.spec.CategoryControllerSpec;
import com.project.expensemanger.manager.application.port.in.CategoryUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerSpec {

    private static final String DEFAULT = "/api/category";
    private final CategoryUseCase categoryUseCase;
    private final CategoryMapper mapper;

    @PostMapping("/api/category")
    public ResponseEntity<CategoryIdResponse> registerCategory(
            @RequestBody @Valid RegisterCategoryRequest requestDto
    ) {
        Long categoryId = categoryUseCase.register(requestDto);
        URI location = UrlCreator.createUri(DEFAULT, categoryId);
        return ResponseEntity.created(location).body(mapper.toIdDto(categoryId));
    }
}
