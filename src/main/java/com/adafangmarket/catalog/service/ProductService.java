package com.adafangmarket.catalog.service;

import com.adafangmarket.catalog.Category;
import com.adafangmarket.catalog.Inventory;
import com.adafangmarket.catalog.Product;
import com.adafangmarket.catalog.dto.ProductCreateRequest;
import com.adafangmarket.catalog.dto.ProductDto;
import com.adafangmarket.catalog.enums.ProductType;
import com.adafangmarket.catalog.mapper.CatalogMapper;
import com.adafangmarket.catalog.repo.CategoryRepository;
import com.adafangmarket.catalog.repo.InventoryRepository;
import com.adafangmarket.catalog.repo.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository products;
    private final CategoryService categories;
    private final CatalogMapper mapper;
    private final InventoryRepository inventory;
    private final CategoryRepository categoryRepository;

    public Page<ProductDto> search(String q, ProductType type, UUID categoryId, Boolean inStock, Pageable pg ) {
        Specification<Product> spec = activeTrue();

        if (q != null && !q.isBlank()) spec = spec.and(nameSkuLike(q));
        if (type != null) spec = spec.and(typeEq(type));
        if( categoryId != null ) spec = spec.and(categoryEq(categoryId));
        if (Boolean.TRUE.equals(inStock)) spec = spec.and(InventoryAvailable());

        return products.findAll(spec, pg).map(mapper::toDto);
    }

    public ProductDto getBySlug(String slug) {
        return products.findBySlug(slug).map(mapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @Transactional
    public ProductDto create(ProductCreateRequest req) {
        if (products.findBySku(req.sku()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SKU already exists");

        Category cat = null;
        if (req.categoryId() != null) {
            cat = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
        }

        var p = new Product();
        mapper.updateEntity(p, req, cat);
        var saved = products.save(p);

        inventory.findBySku(saved.getSku()).orElseGet(() -> {
            var inv = Inventory.builder().sku(saved.getSku()).quantityAvailable(0).quantityReserved(0).build();
            return inventory.save(inv);
        });

        return mapper.toDto(saved);
    }

    @Transactional
    public ProductDto update(UUID id, ProductCreateRequest req) {
        var p = products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        Category cat = null;
        if (req.categoryId() != null) {
            cat = categoryRepository.findById(req.categoryId()) // Utilise le repository directement
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Category"));
        }

        mapper.updateEntity(p, req, cat);
        return mapper.toDto(products.save(p));
    }

    @Transactional
    public void delete(UUID id) {
        var p = products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        products.delete(p);
    }

    private Specification<Product> activeTrue() {
        return (root, cq, cb) -> cb.isTrue(root.get("active"));

    }

    private Specification<Product> nameSkuLike(String q) {
        return (root, cq, cb) -> {
            var like = "%" + q.toLowerCase() + "%";
            return cb.or(cb.like(cb.lower(root.get("name")), like), cb.like(cb.lower(root.get("sku")), like));
        };
    }

    private Specification<Product> typeEq(ProductType t) {
        return (root, cq, cb) -> cb.equal(root.get("type"), t);
    }

    private Specification<Product> categoryEq(UUID id) {
        return (root, cq, cb) -> cb.equal(root.get("category").get("id"), id);
    }

    private Specification<Product> InventoryAvailable() {
        return (root, cq, cb) -> {
            assert cq != null;
            var sub = cq.subquery(Long.class);
            var inv = sub.from(Inventory.class);
            sub.select(cb.count(inv))
                    .where(cb.and(
                            cb.equal(inv.get("sku"), root.get("sku")),
                            cb.greaterThan(cb.diff(inv.get("quantityAvailable"), inv.get("quantityReserved")), 0)
                    ));
            return cb.greaterThan(sub, 0L);
        };
    }
}
