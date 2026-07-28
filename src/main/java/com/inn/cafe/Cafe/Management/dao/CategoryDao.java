package com.inn.cafe.Cafe.Management.dao;

import com.inn.cafe.Cafe.Management.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

    @Query("""
       select distinct c
       from Category c
       where c.id in (
           select p.category.id
           from Product p
           where p.status = 'true'
       )
       """)
    List<Category> getAllCategory();


}
