package com.example.bookService.grpc;

import com.example.bookService.models.Book;
import com.example.bookService.service.BookService;
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
    bookService.getBookById(bookId).ifPresentOrElse(
        book -> {
          GetBookResponse response = GetBookResponse.newBuilder()
              .setBook(convertToProto(book))
              .build();
          responseObserver.onNext(response);
          responseObserver.onCompleted();
        },
        () -> {
          responseObserver.onError(Status.NOT_FOUND
              .withDescription("Book with ID " + bookId + " not found")
              .asRuntimeException());
        }
    );
  }

  @Override
  public void createBook(CreateBookRequest request, StreamObserver<CreateBookResponse> responseObserver) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setPrice(request.getPrice());

    Book createdBook = bookService.createBook(book);

    CreateBookResponse response = CreateBookResponse.newBuilder()
        .setBook(convertToProto(createdBook))
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void listBooks(ListBooksRequest request, StreamObserver<ListBooksResponse> responseObserver) {
    ListBooksResponse.Builder responseBuilder = ListBooksResponse.newBuilder();
    bookService.listBooks().forEach(book -> {
      responseBuilder.addBooks(convertToProto(book));
    });
    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }

  private com.example.bookService.grpc.Book convertToProto(Book book) {
    return com.example.bookService.grpc.Book.newBuilder()
        .setId(book.getId())
        .setTitle(book.getTitle())
        .setAuthor(book.getAuthor())
        .setPrice(book.getPrice())
        .build();
  }
}
