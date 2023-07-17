package com.application.rest.controllers;

import com.application.rest.controllers.dto.ProductDTO;
import com.application.rest.entities.Product;
import com.application.rest.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Product")
public class ProductController {

    @Autowired
    private IProductService productService;


    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductDTO productDTO = ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .build();

            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<ProductDTO> productDTOList = productService.findAll()
                .stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .maker(product.getMaker())
                        .build())
                .toList();
        return ResponseEntity.ok(productDTOList);
    }

    @GetMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        if (productDTO.getName().isBlank() || productDTO.getPrice() == null || productDTO.getMaker() == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .maker(productDTO.getMaker())
                .build();

        productService.save(product);

        return ResponseEntity.created(new URI("/api/Product/save")).build();
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(product.getName());
            product.setPrice(productDTO.getPrice());
            product.setMaker(productDTO.getMaker());

            return ResponseEntity.ok("Registro actualizado correctamente");
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (id != null) {
            productService.deleteById(id);
            return ResponseEntity.ok("Registro eliminado correctamente");
        }
        return ResponseEntity.badRequest().build();
    }
}

