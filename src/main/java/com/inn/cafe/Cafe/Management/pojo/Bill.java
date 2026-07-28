package com.inn.cafe.Cafe.Management.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "bill")
public class Bill implements Serializable {


    private static final long serialVersionUID =1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name ="email")
    private String email;

    @Column(name ="contact number")
    private String contactNumber;

    @Column(name ="payment method")
    private String paymentMethod;

    @Column(name ="total")
    private String total;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "product details", columnDefinition = "TEXT")
    private String productDetails;



}
