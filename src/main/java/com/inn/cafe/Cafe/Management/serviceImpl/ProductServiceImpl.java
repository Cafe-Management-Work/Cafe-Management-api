package com.inn.cafe.Cafe.Management.serviceImpl;

import com.inn.cafe.Cafe.Management.constants.CafeConstants;
import com.inn.cafe.Cafe.Management.customExceptions.CategoryNotFoundException;
import com.inn.cafe.Cafe.Management.customExceptions.ProductNotFoundException;
import com.inn.cafe.Cafe.Management.customExceptions.UserUnAuthorizedAccessException;
import com.inn.cafe.Cafe.Management.dao.CategoryDao;
import com.inn.cafe.Cafe.Management.dao.ProductDao;
import com.inn.cafe.Cafe.Management.dto.response.ProductBasicResponse;
import com.inn.cafe.Cafe.Management.dto.request.ProductRequest;
import com.inn.cafe.Cafe.Management.dto.request.ProductUpdateRequest;
import com.inn.cafe.Cafe.Management.dto.request.StatusUpdateRequest;
import com.inn.cafe.Cafe.Management.dto.response.ProductMiniResponse;
import com.inn.cafe.Cafe.Management.dto.response.ProductResponse;
import com.inn.cafe.Cafe.Management.jwt.JwtFilter;
import com.inn.cafe.Cafe.Management.pojo.Category;
import com.inn.cafe.Cafe.Management.pojo.Product;
import com.inn.cafe.Cafe.Management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public String addNewProduct(ProductRequest productRequest) {

        if (!jwtFilter.isAdmin()) {
            throw new UserUnAuthorizedAccessException(CafeConstants.ADMIN_ACCESS);
        }

        Category category = categoryDao.findById(productRequest.categoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                String.format(
                                        CafeConstants.CATEGORY_NOT_FOUND,
                                        productRequest.categoryId()
                                )
                        )
                );

        Product product = new Product();
        product.setName(productRequest.name());
        product.setPrice(productRequest.price());
        product.setCategory(category);
        product.setStatus(String.valueOf(true));
        product.setDescription(productRequest.description());

        productDao.save(product);

        return CafeConstants.PRODUCT_ADDED_SUCCESSFULLY;

    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public String updateProduct(ProductUpdateRequest request) {

        if (!jwtFilter.isAdmin()) {
            throw new UserUnAuthorizedAccessException(CafeConstants.ADMIN_ACCESS);
        }

        Product product = productDao.findById(request.id())
                .orElseThrow(() ->
                        new ProductNotFoundException(String.format(CafeConstants.PRODUCT_NOT_FOUND , request.id()))
                );

        // ✅ Update only provided fields
        if (request.name() != null && !request.name().isBlank()) {
            product.setName(request.name());
        }

        if (request.description() != null) {
            product.setDescription(request.description());
        }

        if (request.price() != null) {
            product.setPrice(request.price());
        }

        if (request.status() != null) {
            product.setStatus(request.status());
        }

        if (request.categoryId() != null) {
            Category category = categoryDao.findById(request.categoryId())
                    .orElseThrow(() ->
                            new CategoryNotFoundException(
                                    String.format(
                                            CafeConstants.CATEGORY_NOT_FOUND,
                                            request.categoryId()
                                    )
                            )
                    );
            product.setCategory(category);
        }

        productDao.save(product);

        return CafeConstants.PRODUCT_UPDATED;
    }

    @Override
    public String deleteProduct(Integer id) {
        if (!jwtFilter.isAdmin()) {
            throw new UserUnAuthorizedAccessException(CafeConstants.ADMIN_ACCESS);
        }
        productDao.findById(id).orElseThrow(()->
                new ProductNotFoundException(String.format(
                        CafeConstants.PRODUCT_NOT_FOUND,id
                ))
        );
        productDao.deleteById(id);
        return String.format(CafeConstants.PRODUCT_DELETED);
    }

    @Override
    public String updateProductStatus(StatusUpdateRequest productStatusUpdate) {
        if (!jwtFilter.isAdmin()) {
            throw new UserUnAuthorizedAccessException(CafeConstants.ADMIN_ACCESS);
        }
        productDao.findById(productStatusUpdate.id()).orElseThrow(()->
                new ProductNotFoundException(String.format(
                        CafeConstants.PRODUCT_NOT_FOUND,productStatusUpdate.id()
                ))
        );
        productDao.productStatusUpdate(productStatusUpdate.status(),
                productStatusUpdate.id());

        return String.format(CafeConstants.PRODUCT_STATUS_UPDATED);
    }

    @Override
    public List<ProductMiniResponse> getByCategory(Integer id) {

        return productDao.getByCategory(id);
    }

    @Override
    public ProductBasicResponse getById(Integer id) {
        Optional<ProductBasicResponse> productBasicResponse = Optional.ofNullable(productDao.getProductById(id));
        if(!productBasicResponse.isPresent()){
            throw new ProductNotFoundException(
                    String.format(CafeConstants.PRODUCT_NOT_FOUND,
                            id)
            );
        }
        return productBasicResponse.get();
    }

}
