package com.stackeddeck.catalog.repo;

import com.stackeddeck.catalog.Product;
import com.stackeddeck.catalog.enums.Rarity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySku(String sku);

    Page<Product> findByActiveTrue(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId AND p.active = true")
    long countByCategoryId(@Param("categoryId") UUID categoryId);

    @Query("SELECT DISTINCT p.rarity FROM Product p WHERE p.active = true ORDER BY p.rarity")
    List<Rarity> findDistinctRarities();

    // Obtenir les raretés disponibles pour une catégorie
    @Query("SELECT DISTINCT p.rarity FROM Product p WHERE p.category.id = :categoryId AND p.active = true ORDER BY p.rarity")
    List<Rarity> findDistinctRaritiesByCategoryId(@Param("categoryId") UUID categoryId);

    // Produits featured (pour la page d'accueil)
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.type = 'SINGLE_CARD' ORDER BY p.createdAt DESC")
    Page<Product> findFeaturedProducts(Pageable pageable);

}