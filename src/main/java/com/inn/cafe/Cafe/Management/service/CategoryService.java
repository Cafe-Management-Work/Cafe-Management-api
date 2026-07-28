package com.inn.cafe.Cafe.Management.service;

import com.inn.cafe.Cafe.Management.dto.request.CategoryDto;
import com.inn.cafe.Cafe.Management.pojo.Category;

import java.util.List;

public interface CategoryService {

    String addCategory(CategoryDto categoryDto);

    List<Category> getAllCategory(String filterValue);

    public String updateCategory(Integer id, CategoryDto categoryDto);
}
