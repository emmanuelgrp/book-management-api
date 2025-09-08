package com.codemainlabs.book_management_api.config;

import com.codemainlabs.book_management_api.model.dto.author.AuthorResponseDTO;
import com.codemainlabs.book_management_api.model.dto.book.BookResponseDTO;
import com.codemainlabs.book_management_api.util.BookPaginationResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public PagedResourcesAssembler<BookResponseDTO> bookPagedResourcesAssembler() {
        return new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), null);
    }

    @Bean
    public PagedResourcesAssembler<AuthorResponseDTO> authorPagedResourcesAssembler() {
        return new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), null);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new BookPaginationResolver());
    }
}