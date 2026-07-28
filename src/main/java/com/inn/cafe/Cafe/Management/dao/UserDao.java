package com.inn.cafe.Cafe.Management.dao;

import com.inn.cafe.Cafe.Management.dto.response.UserResponse;
import com.inn.cafe.Cafe.Management.pojo.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User,Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    @Query("SELECT new com.inn.cafe.Cafe.Management.dto.response.UserResponse(u.id, u.name, u.email, u.contactNumber, u.status) " +
            "FROM User u WHERE u.role = 'user'")
    List<UserResponse> getAllUser();


    @Modifying
    @Transactional
    @Query("update User u set u.status = :status where u.id = :id")
    int updateStatus(@Param("status") String status, @Param("id") Integer id);

    @Query("SELECT u.email FROM User u WHERE u.role = 'admin'")
    List<String> getAllAdminEmails();


}
