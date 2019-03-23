package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(categoryRepository.count().block() == 0) {
            loadData();
        }
    }

    private void loadData() {
        log.debug("Loading bootstrap data");

        categoryRepository.save(Category.builder().description("Fruits").build()).block();
        categoryRepository.save(Category.builder().description("Nuts").build()).block();
        categoryRepository.save(Category.builder().description("Breads").build()).block();
        categoryRepository.save(Category.builder().description("Meats").build()).block();
        categoryRepository.save(Category.builder().description("Eggs").build()).block();

        log.debug("Loaded categories: " + categoryRepository.count().block());

        vendorRepository.save(Vendor.builder().firstName("Joe").lastName("Buck").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Michael").lastName("Weston").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Jessie").lastName("Waters").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Bill").lastName("Nershi").build()).block();

        log.debug("Loaded vendors: " + vendorRepository.count().block());
    }
}
