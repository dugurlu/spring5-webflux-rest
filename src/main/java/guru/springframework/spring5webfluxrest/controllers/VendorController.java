package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor vendorToPatch = vendorRepository.findById(id).block();

        if ((vendor.getFirstName() == null && vendorToPatch.getFirstName() != null) ||
                (vendor.getFirstName() != null && !vendor.getFirstName().equals(vendorToPatch.getFirstName()))) {
            vendorToPatch.setFirstName(vendor.getFirstName());
            return vendorRepository.save(vendorToPatch);
        }

        if ((vendor.getLastName() == null && vendorToPatch.getLastName() != null) ||
                (vendor.getLastName() != null && !vendor.getLastName().equals(vendorToPatch.getLastName()))) {
            vendorToPatch.setLastName(vendor.getLastName());
            return vendorRepository.save(vendorToPatch);
        }

        return Mono.just(vendorToPatch);
    }
}
