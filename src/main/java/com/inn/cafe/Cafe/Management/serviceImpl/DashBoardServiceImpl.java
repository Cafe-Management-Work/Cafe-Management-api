package com.inn.cafe.Cafe.Management.serviceImpl;

import com.inn.cafe.Cafe.Management.dao.BillDao;
import com.inn.cafe.Cafe.Management.dao.CategoryDao;
import com.inn.cafe.Cafe.Management.dao.ProductDao;
import com.inn.cafe.Cafe.Management.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    BillDao billDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    ProductDao productDao;

    @Override
    public Map<String, Object> getCount() {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("Bill",billDao.count());
        map.put("Category",categoryDao.count());
        map.put("Product",productDao.count());
        return map;
    }
}
