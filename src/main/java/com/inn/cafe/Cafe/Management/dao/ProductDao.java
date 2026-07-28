package com.inn.cafe.Cafe.Management.dao;

import com.inn.cafe.Cafe.Management.dto.response.ProductBasicResponse;
import com.inn.cafe.Cafe.Management.dto.response.ProductMiniResponse;
import com.inn.cafe.Cafe.Management.dto.response.ProductResponse;
import com.inn.cafe.Cafe.Management.pojo.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product,Integer> {

    @Query("""
        SELECT new com.inn.cafe.Cafe.Management.dto.response.ProductResponse(
            p.id,
            p.name,
            p.description,
            p.price,
            p.status,
            p.category.id,
            p.category.name
        )
        FROM Product p
    """)
    List<ProductResponse> getAllProducts();


    @Query("update Product p set p.status = :status where p.id = :id")
    @Modifying
    @Transactional
    void productStatusUpdate(@Param("status") String status, @Param("id") Integer id);


    @Query("""
        SELECT new com.inn.cafe.Cafe.Management.dto.response.ProductMiniResponse(
            p.id,
            p.name
        )
        FROM Product p where p.category.id = :id and p.status ='true'
    """)
    List<ProductMiniResponse> getByCategory(@Param("id") Integer id);


    @Query("""
    SELECT new com.inn.cafe.Cafe.Management.dto.response.ProductBasicResponse(
        p.id,
        p.name,
        p.description,
        p.price
    )
    FROM Product p
    WHERE p.id = :id
""")
    ProductBasicResponse getProductById(@Param("id") Integer id);
}
