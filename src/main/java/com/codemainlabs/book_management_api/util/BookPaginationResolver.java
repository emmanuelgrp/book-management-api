package com.codemainlabs.book_management_api.util;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import java.util.HashMap;
import java.util.Map;

public class BookPaginationResolver extends PageableHandlerMethodArgumentResolver {

    private static final Map<String, String> SORT_MAPPING = new HashMap<>();

    static {
        SORT_MAPPING.put("bookID", "id");
    }

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        Sort originalSort = pageable.getSort();
        Sort newSort = Sort.unsorted();

        for (Sort.Order order : originalSort) {
            String property = order.getProperty();
            if (SORT_MAPPING.containsKey(property)) {
                newSort = newSort.and(Sort.by(order.getDirection(), SORT_MAPPING.get(property)));
            } else {
                newSort = newSort.and(Sort.by(order.getDirection(), property));
            }
        }

        if (newSort.isSorted()) {
            return org.springframework.data.domain.PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);
        }

        return pageable;
    }
}