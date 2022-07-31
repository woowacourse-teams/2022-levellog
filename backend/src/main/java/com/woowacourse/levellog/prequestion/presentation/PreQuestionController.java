package com.woowacourse.levellog.prequestion.presentation;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.prequestion.application.PreQuestionService;
import com.woowacourse.levellog.prequestion.dto.PreQuestionCreateDto;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/levellogs/{levellogId}/pre-questions")
@RequiredArgsConstructor
public class PreQuestionController {

    private final PreQuestionService preQuestionService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final PreQuestionCreateDto createDto,
                                     @PathVariable final Long levellogId,
                                     @Authentic final Long memberId) {
        final Long preQuestionId = preQuestionService.save(createDto, levellogId, memberId);
        return ResponseEntity.created(
                URI.create("/api/levellogs/" + levellogId + "/pre-questions/" + preQuestionId)).build();
    }
}
