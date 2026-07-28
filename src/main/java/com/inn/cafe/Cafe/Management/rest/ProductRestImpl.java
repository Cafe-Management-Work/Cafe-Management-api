package com.inn.cafe.Cafe.Management.rest;

import com.inn.cafe.Cafe.Management.dto.response.ProductBasicResponse;
import com.inn.cafe.Cafe.Management.dto.request.ProductRequest;
import com.inn.cafe.Cafe.Management.dto.request.ProductUpdateRequest;
import com.inn.cafe.Cafe.Management.dto.request.StatusUpdateRequest;
import com.inn.cafe.Cafe.Management.dto.response.ProductMiniResponse;
import com.inn.cafe.Cafe.Management.dto.response.ProductResponse;
import com.inn.cafe.Cafe.Management.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductRestImpl {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<String> addNewProduct(@Valid @RequestBody ProductRequest productRequest){
        return ResponseEntity.ok(productService.addNewProduct(productRequest));
    }

    @GetMapping("/get")
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductUpdateRequest productUpdateRequest){
        return ResponseEntity.ok(productService.updateProduct(productUpdateRequest));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @PostMapping("/statusUpdate")
    public ResponseEntity<String> productStatusUpdate(@RequestBody StatusUpdateRequest productStatusUpdateRequest){
        return ResponseEntity.ok(productService.updateProductStatus(productStatusUpdateRequest));
    }

    @GetMapping("/getByCategory/{id}")
    public ResponseEntity<List<ProductMiniResponse>> getByCategory(@PathVariable("id") Integer id){
        return ResponseEntity.ok(productService.getByCategory(id));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductBasicResponse> getById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(productService.getById(id));
    }


}
