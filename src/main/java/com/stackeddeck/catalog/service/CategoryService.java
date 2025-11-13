package com.stackeddeck.catalog.service;

import com.stackeddeck.catalog.Category;
import com.stackeddeck.catalog.dto.CategoryDto;
import com.stackeddeck.catalog.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categories;

    public Category findById(UUID id) {
        return categories.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public List<CategoryDto> treeRoot() {
        return categories.findByParentIsNullOrderByPositionAsc()
                .stream().map(this::toDto).toList();
    }

    public CategoryDto create(CategoryDto dto) {
        if (categories.findBySlug(dto.slug()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists");
        var c = new Category();
        c.setName(dto.name());
        c.setSlug(dto.slug());
        c.setPosition(dto.position() != null ? dto.position() : 0);
        if (dto.parentId() != null) {
            c.setParent(categories.findById(dto.parentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parent")));
        }
        return toDto(categories.save(c));
    }

    public CategoryDto update(UUID id, CategoryDto dto) {
        var c = categories.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        c.setName(dto.name());
        if (categories.findBySlug(dto.slug()).isPresent() && !dto.slug().equals(c.getSlug()))  // NOUVEAU : Check change slug
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already exists");
        c.setSlug(dto.slug());
        c.setPosition(dto.position()!=null? dto.position():0);
        if (dto.parentId()!=null) c.setParent(categories.findById(dto.parentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parent")));
        else c.setParent(null);
        return toDto(categories.save(c));
    }

    public void delete(UUID id) {
        if (!categories.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        categories.deleteById(id);
    }

    private CategoryDto toDto(Category c) {
        return new CategoryDto(c.getId(), c.getName(), c.getSlug(), c.getParent()!=null? c.getParent().getId():null, c.getPosition());
    }
}