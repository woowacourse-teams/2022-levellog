package com.woowacourse.levellog.prequestion.dto.request;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PreQuestionWriteRequest {

    @NotBlank
    private String content;

    public PreQuestion toEntity(final Levellog levellog, final Long fromId) {
        return new PreQuestion(levellog, fromId, content);
    }
}
