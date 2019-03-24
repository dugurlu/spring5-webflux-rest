package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class VendorControllerTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorController vendorController;

    WebTestClient client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        client = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        given(vendorRepository.findAll()).willReturn(Flux.just(
                Vendor.builder().firstName("Joe").lastName("Buck").build(),
                Vendor.builder().firstName("Jane").lastName("Doe").build()));

        client.get()
                .uri(VendorController.BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        given(vendorRepository.findById(anyString())).willReturn(Mono.just(
                Vendor.builder().firstName("Joe").lastName("Buck").build()));

        client.get()
                .uri(VendorController.BASE_URL + "/foo")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void create() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendor = Mono.just(Vendor.builder().firstName("Jimmy").lastName("Johns").build());

        client.post()
                .uri(VendorController.BASE_URL)
                .body(vendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void update() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendor = Mono.just(Vendor.builder().firstName("Jim").lastName("Smith").build());

        client.put()
                .uri(VendorController.BASE_URL + "/foo")
                .body(vendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vendor.class);
    }

    @Test
    public void patch() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendor = Mono.just(Vendor.builder().firstName("PatchmeFirstname").build());

        client.patch()
                .uri(VendorController.BASE_URL + "/foo")
                .body(vendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any(Vendor.class));
    }

    @Test
    public void patchNoChanges() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendor = Mono.just(Vendor.builder().build());

        client.patch()
                .uri(VendorController.BASE_URL + "/foo")
                .body(vendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any(Vendor.class));
    }
}