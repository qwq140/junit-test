package com.cos.book.web;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.cos.book.domain.CommonRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookController {

	private final BookRepository bookRepository;
	
	@Transactional
	@PostMapping("/book")
	public ResponseEntity<?> save(@RequestBody Book book){
		return new ResponseEntity<>(bookRepository.save(book),HttpStatus.CREATED);
	}
	
	@Transactional(readOnly = true)
	@GetMapping("/book")
	public ResponseEntity<?> findAll(){
		return new ResponseEntity<>(bookRepository.findAll(),HttpStatus.OK);
	}
	
	@Transactional(readOnly = true)
	@GetMapping("/book/{id}")
	public ResponseEntity<?> findById(@PathVariable int id){
		return new ResponseEntity<>(bookRepository.findById(id),HttpStatus.OK);
	}
	
	
	@Transactional
	@PutMapping("/book/{id}")
	public ResponseEntity<?> updateById(@PathVariable int id, @RequestBody Book book){
		Book bookEntity = bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id를 확인해주세요"));
		bookEntity.setTitle(book.getTitle());
		bookEntity.setRating(book.getRating());
		bookEntity.setPrice(book.getPrice());
		return new ResponseEntity<>(bookEntity,HttpStatus.OK);
	}
	
	@Transactional
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteById(@PathVariable int id){
		bookRepository.deleteById(id);
		CommonRespDto<String> dto = new CommonRespDto<>();
		dto.setData("ok");
		return new ResponseEntity<>(dto,HttpStatus.OK);
	}
}
