package com.obolonyk.webflux_playground.sec09.mapper;

import com.obolonyk.webflux_playground.sec09.dto.ProductDto;
import com.obolonyk.webflux_playground.sec09.entity.Product;

public class EntityDtoMapper {
    public static ProductDto entityToDto(Product product) {
        return new ProductDto(product.getId(), product.getDescription(), product.getPrice());
    }

    public static Product dtoToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.id());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        return product;
    }
}
