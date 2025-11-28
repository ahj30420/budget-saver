package com.project.expensemanger.manager.adaptor.in.api;

import com.project.expensemanger.core.common.annotation.CurrentUser;
import com.project.expensemanger.core.common.util.UrlCreator;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.GetExpenditureListRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.RegisterExpenditure;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.UpdateExpenditureRequest;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureDetailsResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureIdResponse;
import com.project.expensemanger.manager.adaptor.in.api.dto.response.ExpenditureListResponse;
import com.project.expensemanger.manager.adaptor.in.api.mapper.ExpenditureMapper;
import com.project.expensemanger.manager.adaptor.in.api.spec.ExpenditureControllerSpec;
import com.project.expensemanger.manager.application.service.model.ExpenditureDetailModel;
import com.project.expensemanger.manager.application.service.model.ExpenditureListModel;
import com.project.expensemanger.manager.application.port.in.ExpenditureUseCase;
import com.project.expensemanger.manager.domain.expenditure.Expenditure;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Override
    @DeleteMapping("/api/expenditure/{expenditureId}")
    public ResponseEntity<Void> deleteExpenditure(
            @CurrentUser Long userId,
            @PathVariable("expenditureId") Long expenditureId
    ) {
        useCase.deleteExpenditure(userId, expenditureId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/api/expenditure/{expenditureId}")
    public ResponseEntity<ExpenditureIdResponse> updateExpenditure(
            @CurrentUser Long userId,
            @PathVariable("expenditureId") Long expenditureId,
            @Valid @RequestBody UpdateExpenditureRequest requestDto
    ) {
        Expenditure updateExpenditure = useCase.updateExpenditure(userId, expenditureId, requestDto);
        URI location = UrlCreator.createUri(DEFUALT, expenditureId);
        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, location.toString())
                .body(mapper.toIdDto(updateExpenditure.getId()));
    }

    @Override
    @GetMapping("/api/expenditure/{expenditureId}")
    public ResponseEntity<ExpenditureDetailsResponse> getExpenditure(
            @CurrentUser Long userId,
            @PathVariable("expenditureId") Long expenditureId
    ) {
        ExpenditureDetailModel expenditureDetails = useCase.getExpenditureDetails(userId, expenditureId);
        return ResponseEntity.ok(mapper.toExpenditureDetailsDto(expenditureDetails));
    }

    @Override
    @GetMapping("/api/expenditure/list")
    public ResponseEntity<ExpenditureListResponse> getExpenditureList(
            @CurrentUser Long userId,
            @Valid @ModelAttribute GetExpenditureListRequest requestDto
    ) {
        ExpenditureListModel expenditureListByCondition = useCase.getExpenditureListByCondition(
                mapper.toConditionDto(userId, requestDto));
        return ResponseEntity.ok(mapper.toExpenditureListResponse(expenditureListByCondition));
    }
}
