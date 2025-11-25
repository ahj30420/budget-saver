package com.project.expensemanger.manager.adaptor.in.api;

import com.project.expensemanger.core.common.annotation.CurrentUser;
import com.project.expensemanger.core.common.util.UrlCreator;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.mapper.ExpenditureMapper;
import com.project.expensemanger.manager.adaptor.in.api.spec.ExpenditureControllerSpec;
import com.project.expensemanger.manager.application.port.in.ExpenditureUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpenditureController implements ExpenditureControllerSpec {

    private final static String DEFUALT = "/api/expenditure";
    private final ExpenditureUseCase useCase;
    private final ExpenditureMapper mapper;

    @Override
    @PostMapping("/api/expenditure")
    public ResponseEntity<ExpenditureIdResponse> registerExpenditure(
            @CurrentUser Long userId,
            @Valid @RequestBody RegisterExpenditure requestDto
    ) {
        Long expenditureId = useCase.registerExpenditure(userId, requestDto);
        URI location = UrlCreator.createUri(DEFUALT, expenditureId);
        return ResponseEntity.created(location).body(mapper.toIdDto(expenditureId));
    }

}
