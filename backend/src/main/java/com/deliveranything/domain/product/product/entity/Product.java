package com.deliveranything.domain.product.product.entity;

import com.deliveranything.domain.product.stock.entity.Stock;
import com.deliveranything.domain.store.store.entity.Store;
import com.deliveranything.global.entity.BaseEntity;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "products",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_products_name_per_store", columnNames = {"store_id","name"})
    },
    indexes = {
        @Index(name = "idx_products_store_status_created", columnList = "store_id, status, created_at, id"),
        @Index(name = "idx_products_store_status_price",   columnList = "store_id, status, price, id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "fk_products_store"))
  private Store store;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private Integer price;

  @Column(name = "image_url", nullable = false, length = 256)
  private String imageUrl;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "product_keywords",
      joinColumns = @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_product_keywords_product")),
      indexes = {
          @Index(name = "idx_product_keywords_keyword", columnList = "keyword")
      }
  )
  private List<String> keywords = new ArrayList<>();

  @Setter
  @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private Stock stock;

  public void setKeywords(String trim) {
    this.keywords.clear();
    String[] splits = trim.split(",");
    for (String s : splits) {
      String keyword = s.trim();
      if (!keyword.isEmpty()) {
        this.keywords.add(keyword);
      }
    }
  }

  @Builder
  public Product(Store store, String name, String description, Integer price, String imageUrl, Integer initialStock) {
    if (store == null || name == null || price == null || imageUrl == null) {
      throw new IllegalArgumentException("필수 필드 누락");
    }

    this.store = store;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imageUrl = imageUrl;

    this.stock = new Stock(this, initialStock);
  }

  public void update(String name, String description, Integer price, String imageUrl) {
    if (name != null) {
      this.name = name;
    }
    if (description != null) {
      this.description = description;
    }
    if (price != null) {
      this.price = price;
    }
    if (imageUrl != null) {
      this.imageUrl = imageUrl;
    }
  }

  public void validateStore(Long storeId) {
    if (!this.store.getId().equals(storeId)) {
      throw new CustomException(ErrorCode.PRODUCT_STORE_MISMATCH);
    }
  }
}