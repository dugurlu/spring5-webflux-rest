package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CategoryControllerTest {

    WebTestClient client;
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryController categoryController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        client = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {
        given(categoryRepository.findAll()).willReturn(Flux.just(
                Category.builder().description("Category1").build(),
                Category.builder().description("Category2").build()));

        client.get()
                .uri(CategoryController.BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        given(categoryRepository.findById(anyString())).willReturn(Mono.just(
                Category.builder().description("Category").build()));
        client.get()
                .uri(CategoryController.BASE_URL + "/foo")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void create() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> category = Mono.just(Category.builder().description("MyCategory").build());

        client.post()
                .uri(CategoryController.BASE_URL)
                .body(category, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void update() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> category = Mono.just(Category.builder().description("SomeCategory").build());

        client.put()
                .uri(CategoryController.BASE_URL + "/foo")
                .body(category, Category.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Category.class);
    }

    @Test
    public void patch() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> category = Mono.just(Category.builder().description("Patchme").build());

        client.patch()
                .uri(CategoryController.BASE_URL + "/foo")
                .body(category, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void patchNoChanges() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> category = Mono.just(Category.builder().build());

        client.patch()
                .uri(CategoryController.BASE_URL + "/foo")
                .body(category, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any(Category.class));
    }
}