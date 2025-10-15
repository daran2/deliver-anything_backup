package com.deliveranything.global.common;

import java.util.List;

public record CursorPageResponse<T>(
    List<T> content,
    String nextPageToken,
    boolean hasNext
) {

}
