package com.inn.cafe.Cafe.Management.rest;

import com.inn.cafe.Cafe.Management.dto.request.CategoryDto;
import com.inn.cafe.Cafe.Management.pojo.Category;
import com.inn.cafe.Cafe.Management.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryRestImpl {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDto categoryDto){
        return ResponseEntity.ok(categoryService.addCategory(categoryDto));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false)
                                                         String filterValue){
        return ResponseEntity.ok(categoryService.getAllCategory(filterValue));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CategoryDto dto
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id,dto));
    }


}
