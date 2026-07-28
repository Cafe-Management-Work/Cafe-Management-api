package com.inn.cafe.Cafe.Management.serviceImpl;

import com.inn.cafe.Cafe.Management.constants.CafeConstants;
import com.inn.cafe.Cafe.Management.customExceptions.CategoryNotFoundException;
import com.inn.cafe.Cafe.Management.customExceptions.UserUnAuthorizedAccessException;
import com.inn.cafe.Cafe.Management.dao.CategoryDao;
import com.inn.cafe.Cafe.Management.dto.request.CategoryDto;
import com.inn.cafe.Cafe.Management.jwt.JwtFilter;
import com.inn.cafe.Cafe.Management.pojo.Category;
import com.inn.cafe.Cafe.Management.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public String addCategory(CategoryDto categoryDto) {

        if (!jwtFilter.isAdmin()) {
            throw new UserUnAuthorizedAccessException(CafeConstants.ADMIN_ACCESS);
        }

        Category category = new Category();
        category.setName(categoryDto.name());
        categoryDao.save(category);

        return CafeConstants.CATEGORY_ADDED;
    }


    @Override
    public List<Category> getAllCategory(String filterValue) {

        if(!isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true"))
        {
            return categoryDao.getAllCategory();
        }
        else{
            return categoryDao.findAll();
        }
    }

    @Override
    public String updateCategory(Integer id, CategoryDto categoryDto) {

        if (!jwtFilter.isAdmin()) {
            throw new UserUnAuthorizedAccessException(CafeConstants.ADMIN_ACCESS);
        }

        if (id == null) {
            throw new IllegalArgumentException(CafeConstants.CATEGORY_ID_NOT_NULL);
        }

        Category category = categoryDao.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                String.format(
                                        CafeConstants.CATEGORY_NOT_FOUND,
                                        id
                                )
                        ));

        category.setName(categoryDto.name());
        categoryDao.save(category);

        return CafeConstants.CATEGORY_UPDATED;
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
