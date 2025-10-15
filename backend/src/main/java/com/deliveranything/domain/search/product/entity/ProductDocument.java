//package com.deliveranything.domain.search.product.entity;
//
//import java.util.List;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//public class ProductDocument {
//
//  @Id
//  private Long id;
//
//  @Field(type = FieldType.Text, name = "name")
//  private String name;
//
//  @Field(type = FieldType.Text, name = "description")
//  private String description;
//
//  @Field(type = FieldType.Integer, name = "price")
//  private int price;
//
//  @Field(type = FieldType.Keyword, name = "keywords")
//  private List<String> keywords;
//
//}