package com.jie.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/**
 * Paginated response wrapper.
 */
@Getter
@Schema(description = "分页响应体")
public class PageResult<T> {

    @Schema(description = "当前页数据")
    private final List<T> records;

    @Schema(description = "总记录数")
    private final long total;

    @Schema(description = "当前页")
    private final long current;

    @Schema(description = "每页条数")
    private final long size;

    public PageResult(Page<?> page, List<T> records) {
        this.records = records;
        this.total = page.getTotal();
        this.current = page.getCurrent();
        this.size = page.getSize();
    }

    public static <T> PageResult<T> of(Page<?> page, List<T> records) {
        return new PageResult<>(page, records);
    }
}
