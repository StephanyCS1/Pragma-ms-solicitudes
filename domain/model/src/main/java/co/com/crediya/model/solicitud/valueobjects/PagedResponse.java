package co.com.crediya.model.solicitud.valueobjects;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
    public static <T> PagedResponse<T> of(List<T> content, int page, int size, long total) {
        int totalPages = (int) Math.ceil((double) total / size);
        return new PagedResponse<>(content, page, size, total, totalPages, page >= totalPages - 1);
    }
}