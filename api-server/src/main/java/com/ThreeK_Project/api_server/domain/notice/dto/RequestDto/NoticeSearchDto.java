package com.ThreeK_Project.api_server.domain.notice.dto.RequestDto;

import com.ThreeK_Project.api_server.domain.notice.enums.NoticeSortType;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class NoticeSearchDto {
    @Min(value = 0,  message = "음수 페이지 입력 불가")
    private int page = 0;
    @Range(min = 1, max = 50, message = "0~50 사이의 사이즈만 입력 가능")
    private int size = 10;
    private NoticeSortType sortBy = NoticeSortType.CREATED_AT;
    private Boolean ascending = false;
    private String keyword;
}
