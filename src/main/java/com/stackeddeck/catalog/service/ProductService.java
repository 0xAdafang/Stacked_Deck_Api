package com.stackeddeck.catalog.service;

import com.stackeddeck.catalog.Category;
import com.stackeddeck.catalog.Inventory;
import com.stackeddeck.catalog.Product;
import com.stackeddeck.catalog.dto.ProductCreateRequest;
import com.stackeddeck.catalog.dto.ProductDto;
import com.stackeddeck.catalog.enums.CardCondition;
import com.stackeddeck.catalog.enums.ProductType;
import com.stackeddeck.catalog.enums.Rarity;
import com.stackeddeck.catalog.mapper.CatalogMapper;
import com.stackeddeck.catalog.repo.CategoryRepository;
import com.stackeddeck.catalog.repo.InventoryRepository;
import com.stackeddeck.catalog.repo.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository products;
    private final CategoryService categories;
    private final CatalogMapper mapper;
    private final InventoryRepository inventory;
    private final CategoryRepository categoryRepository;

    public Page<ProductDto> search(String q, ProductType type, UUID categoryId,
                                   Long minPrice, Long maxPrice,
                                   Rarity rarity, CardCondition condition, Boolean inStock, Pageable pageable) {

        Specification<Product> spec = ProductSpecification.withFilters(
                q, type, categoryId, minPrice, maxPrice, rarity, condition, inStock
        );

        return products.findAll(spec, pageable)
                .map(mapper::toDto);
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

    public List<Rarity> getAvailableRarities(UUID categoryId) {
        if (categoryId != null) {
            return products.findDistinctRaritiesByCategoryId(categoryId);
        }
        return products.findDistinctRarities();
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

    private Specification<Product> rarityEq(Rarity r) {
        return (root, cq, cb) -> cb.equal(root.get("rarity"), r);
    }

    private Specification<Product> conditionEq(CardCondition c) {
        return (root, cq, cb) -> cb.equal(root.get("condition"), c);
    }

    private Specification<Product> InventoryAvailable() {
        return (root, cq, cb) -> {
            return cb.greaterThan(root.get("stockQuantity"), 0);
        };
    }

    public Page<ProductDto> featured(Pageable pageable) {

        return search(
                null,
                ProductType.SINGLE,
                null,
                null,
                null,
                null,
                null,
                true,
                pageable
        );
    }
}
