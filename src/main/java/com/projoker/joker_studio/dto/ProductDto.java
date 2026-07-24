package com.projoker.joker_studio.dto;

import com.projoker.joker_studio.model.ProductType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String materialType;
    private int height;
    private int width;
    private int inventory;
    private BigDecimal price;
    private List<ImageDto> images;
    private ProductType productType;
}
