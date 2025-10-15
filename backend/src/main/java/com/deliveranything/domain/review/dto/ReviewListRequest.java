package com.deliveranything.domain.review.dto;

import com.deliveranything.domain.review.enums.MyReviewSortType;

public record ReviewListRequest(
    MyReviewSortType sort,
    String cursor,
    Integer size
) {

}
