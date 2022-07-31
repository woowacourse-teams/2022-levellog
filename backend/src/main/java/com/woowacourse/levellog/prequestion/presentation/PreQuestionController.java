package com.woowacourse.levellog.prequestion.presentation;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import com.woowacourse.levellog.prequestion.application.PreQuestionService;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/levellogs/{levellogId}/pre-questions")
@RequiredArgsConstructor
public class PreQuestionController {

    private final PreQuestionService preQuestionService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final PreQuestionDto createDto,
                                     @PathVariable final Long levellogId,
                                     @Authentic final Long memberId) {
        final Long preQuestionId = preQuestionService.save(createDto, levellogId, memberId);
        return ResponseEntity.created(
                URI.create("/api/levellogs/" + levellogId + "/pre-questions/" + preQuestionId)).build();
    }

    @GetMapping("/{preQuestionId}")
    @PublicAPI
    public ResponseEntity<PreQuestionDto> findById(@PathVariable final Long levellogId,
                                                   @PathVariable final Long preQuestionId) {
        final PreQuestionDto response = preQuestionService.findById(preQuestionId, levellogId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{preQuestionId}")
    public ResponseEntity<Void> update(@PathVariable final Long preQuestionId,
                                       @RequestBody @Valid final PreQuestionDto request,
                                       @PathVariable final Long levellogId,
                                       @Authentic final Long memberId) {
        preQuestionService.update(request, preQuestionId, levellogId, memberId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{preQuestionId}")
    public ResponseEntity<Void> delete(@PathVariable final Long preQuestionId,
                                       @PathVariable final Long levellogId,
                                       @Authentic final Long memberId) {
        preQuestionService.deleteById(preQuestionId, levellogId, memberId);
        return ResponseEntity.noContent().build();
    }
}
