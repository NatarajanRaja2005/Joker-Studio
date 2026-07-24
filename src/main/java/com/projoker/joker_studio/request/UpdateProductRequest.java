package com.projoker.joker_studio.request;

import com.projoker.joker_studio.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    private String name;
    private String description;
    private String materialType;
    private int height;
    private int width;
    private int inventory;
    private BigDecimal price;
    private ProductType productType;
}
