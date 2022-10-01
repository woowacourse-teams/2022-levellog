package com.woowacourse.levellog.prequestion.presentation;

import com.woowacourse.levellog.authentication.support.FromToken;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.prequestion.application.PreQuestionService;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.dto.PreQuestionWriteDto;
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
    public ResponseEntity<Void> save(@RequestBody @Valid final PreQuestionWriteDto request,
                                     @PathVariable final Long levellogId,
                                     @FromToken final LoginStatus loginStatus) {
        final Long preQuestionId = preQuestionService.save(request, levellogId, loginStatus);
        return ResponseEntity.created(
                URI.create("/api/levellogs/" + levellogId + "/pre-questions/" + preQuestionId)).build();
    }

    @GetMapping("/my")
    public ResponseEntity<PreQuestionDto> findMy(@PathVariable final Long levellogId,
                                                 @FromToken final LoginStatus loginStatus) {
        final PreQuestionDto response = preQuestionService.findMy(levellogId, loginStatus);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{preQuestionId}")
    public ResponseEntity<Void> update(@RequestBody @Valid final PreQuestionWriteDto request,
                                       @PathVariable final Long levellogId,
                                       @PathVariable final Long preQuestionId,
                                       @FromToken final LoginStatus loginStatus) {
        preQuestionService.update(request, preQuestionId, levellogId, loginStatus);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{preQuestionId}")
    public ResponseEntity<Void> delete(@PathVariable final Long levellogId,
                                       @PathVariable final Long preQuestionId,
                                       @FromToken final LoginStatus loginStatus) {
        preQuestionService.deleteById(preQuestionId, levellogId, loginStatus);
        return ResponseEntity.noContent().build();
    }
}
