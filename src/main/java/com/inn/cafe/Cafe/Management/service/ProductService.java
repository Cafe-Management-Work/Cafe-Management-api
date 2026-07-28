package com.inn.cafe.Cafe.Management.service;

import com.inn.cafe.Cafe.Management.dto.response.ProductBasicResponse;
import com.inn.cafe.Cafe.Management.dto.request.ProductRequest;
import com.inn.cafe.Cafe.Management.dto.request.ProductUpdateRequest;
import com.inn.cafe.Cafe.Management.dto.request.StatusUpdateRequest;
import com.inn.cafe.Cafe.Management.dto.response.ProductMiniResponse;
import com.inn.cafe.Cafe.Management.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    String addNewProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    String updateProduct(ProductUpdateRequest productUpdateRequest);

    String deleteProduct(Integer id);

    String updateProductStatus(StatusUpdateRequest productStatusUpdate);

    List<ProductMiniResponse> getByCategory(Integer id);

    ProductBasicResponse getById(Integer id);
}
