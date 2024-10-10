package com.example.bookService.grpc;

import com.example.bookService.models.Book;
import com.example.bookService.service.BookService;
import com.example.bookservice.grpc.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BookServiceGrpcImpl extends BookServiceGrpc.BookServiceImplBase {

  private final BookService bookService;

  public BookServiceGrpcImpl(BookService bookService) {
    this.bookService = bookService;
  }

  @Override
  public void getBook(GetBookRequest request, StreamObserver<GetBookResponse> responseObserver) {
    Long bookId = request.getId();
    bookService.getBookById(bookId).ifPresentOrElse(book -> {
      GetBookResponse response = GetBookResponse.newBuilder()
          .setBook(com.example.bookservice.grpc.Book.newBuilder()
              .setId(book.getId())
              .setTitle(book.getTitle())
              .setAuthor(book.getAuthor())
              .setPrice(book.getPrice())
              .build())
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }, () -> {
      responseObserver.onError(Status.NOT_FOUND
          .withDescription("Book with ID " + bookId + " not found")
          .asRuntimeException());
    });
  }

  @Override
  public void createBook(CreateBookRequest request, StreamObserver<CreateBookResponse> responseObserver) {

    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setPrice(request.getPrice());

    Book createdBook = bookService.createBook(book);

    CreateBookResponse response = CreateBookResponse.newBuilder()
        .setBook(com.example.bookservice.grpc.Book.newBuilder() // Fully qualified name
            .setId(createdBook.getId())
            .setTitle(createdBook.getTitle())
            .setAuthor(createdBook.getAuthor())
            .setPrice(createdBook.getPrice())
            .build())
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }


  @Override
  public void listBooks(ListBooksRequest request, StreamObserver<ListBooksResponse> responseObserver) {
    ListBooksResponse.Builder responseBuilder = ListBooksResponse.newBuilder();
    bookService.listBooks().forEach(book -> {
      responseBuilder.addBooks(
          com.example.bookservice.grpc.Book.newBuilder()
              .setId(book.getId())
              .setTitle(book.getTitle())
              .setAuthor(book.getAuthor())
              .setPrice(book.getPrice())
              .build()
      );
    });
    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }

  public GetBookResponse convertToProto (Book book) {
    return GetBookResponse.newBuilder()
        .setBook(com.example.bookservice.grpc.Book.newBuilder()
            .setId(book.getId())
            .setTitle(book.getTitle())
            .setAuthor(book.getAuthor())
            .setPrice(book.getPrice())
            .build())
        .build();
  }
}
