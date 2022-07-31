package com.woowacourse.levellog.prequestion.dto;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
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
public class PreQuestionDto {

    @NotBlank
    private String preQuestion;

    public static PreQuestionDto from(final PreQuestion preQuestion) {
        return new PreQuestionDto(preQuestion.getPreQuestion());
    }

    public PreQuestion toEntity(final Levellog levellog, final Member from) {
        return new PreQuestion(levellog, from, preQuestion);
    }
}
