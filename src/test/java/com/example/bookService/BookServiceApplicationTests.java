package com.example.bookService;

import com.example.bookService.grpc.BookServiceGrpcImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private BookServiceGrpcImpl bookServiceGrpc;
}
