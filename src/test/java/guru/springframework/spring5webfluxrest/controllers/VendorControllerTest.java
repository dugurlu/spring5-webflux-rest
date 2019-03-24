package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

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
}