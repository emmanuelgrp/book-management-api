package com.codemainlabs.book_management_api;

import com.codemainlabs.book_management_api.model.dto.author.AuthorResponseDTO;
import com.codemainlabs.book_management_api.model.dto.book.BookResponseDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;

@SpringBootApplication
@EnableCaching
public class BookManagementApiApplication {
	public static void main(String[] args) {SpringApplication.run(BookManagementApiApplication.class, args);}

	@Bean
	public PagedResourcesAssembler<BookResponseDTO> bookPagedResourcesAssembler() {
		return new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), null);
	}

	@Bean
	public PagedResourcesAssembler<AuthorResponseDTO> authorPagedResourcesAssembler() {
		return new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), null);
	}
}
