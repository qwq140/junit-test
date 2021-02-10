package com.cos.book.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}
	
	@Test
	public void save_테스트() throws Exception{
		//given
		Book book = new Book(1, "스프링", 4.8, 20000);
		String content = new ObjectMapper().writeValueAsString(book); // json 데이터
		
		//when
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("스프링"))
				.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	public void findById_테스트() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(1,"스프링부트", 4.8, 20000));
		books.add(new Book(2,"리액트", 4.3, 15000));
		books.add(new Book(3,"JUnit", 4.6, 12000));
		bookRepository.saveAll(books);
		
		int id = 2;
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("리액트"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void updateById_테스트() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(1,"스프링부트", 4.8, 20000));
		books.add(new Book(2,"리액트", 4.3, 15000));
		books.add(new Book(3,"JUnit", 4.6, 12000));
		bookRepository.saveAll(books);
		
		int id = 1;
		Book book = new Book(1,"안드로이드", 4.1, 14000);
		String content = new ObjectMapper().writeValueAsString(book);
		
		// when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.title").value("안드로이드"))
			.andDo(MockMvcResultHandlers.print());
	}

	 @Test
		public void deleteById_테스트() throws Exception {
			// given
			int id=1;
			
			List<Book> books = new ArrayList<>();
			books.add(new Book(1,"스프링부트", 4.8, 20000));
			books.add(new Book(2,"리액트", 4.3, 15000));
			books.add(new Book(3,"JUnit", 4.6, 12000));
			bookRepository.saveAll(books);
			
			// when
			ResultActions resultAction = mockMvc.perform(delete("/book/{id}",id)
					.accept(MediaType.APPLICATION_JSON_UTF8));
			
			// then
			resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").value("ok"))
				.andDo(MockMvcResultHandlers.print());
			
			
		}



}
