package com.example.springbootmall.dao.impl;

import com.example.springbootmall.dao.OrderDao;
import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.dto.OrderQueryParams;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.model.OrderItem;
import com.example.springbootmall.rowmapper.OrderItemRowMapper;
import com.example.springbootmall.rowmapper.OrderRowMapper;
import com.example.springbootmall.rowmapper.ProductRowMapper;
import org.apache.catalina.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countOrders(OrderQueryParams orderQueryParams) {
        String sql = "select count(*) from `order` WHERE 1=1";

        Map<String, Object> map = new HashMap<>();
        sql = addFilteringSql(sql, map, orderQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date from `order` WHERE 1=1";

        Map<String, Object> map = new HashMap<>();
        sql = addFilteringSql(sql, map, orderQueryParams);

        // 排序
        sql += " order by created_date DESC";

        // 分頁
        sql += " limit :limit offset :offset";
        map.put("limit", orderQueryParams.getLimit());
        map.put("offset", orderQueryParams.getOffset());

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql = "select order_id, user_id, total_amount, created_date, last_modified_date" +
                " from `order` where order_id =:orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        if (!orderList.isEmpty()) {
            return orderList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsById(Integer orderId) {
        String sql = "select oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url " +
                " from order_item as oi " +
                " left join product as p on oi.product_id = p.product_id" +
                " where order_id =:orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemList;

    }

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql = "Insert into `order`(user_id, total_amount, created_date, last_modified_date) " +
                "values(:userId, :totalAmount, :createdDate, :lastModifiedDate)";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("totalAmount", totalAmount);

        Date now = new Date();
        params.put("createdDate", now);
        params.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);

        int orderId = keyHolder.getKey().intValue();
        return orderId;
    }


    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
        String sql = "insert into order_item(order_id, product_id, quantity, amount) " +
                "values(:orderId, :productId, :quantity, :amount)";

        MapSqlParameterSource[] mapSqlParameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            mapSqlParameterSources[i] = new MapSqlParameterSource();
            mapSqlParameterSources[i].addValue("orderId", orderId);
            mapSqlParameterSources[i].addValue("productId", orderItem.getProductId());
            mapSqlParameterSources[i].addValue("quantity", orderItem.getQuantity());
            mapSqlParameterSources[i].addValue("amount", orderItem.getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, mapSqlParameterSources);
    }

    private String addFilteringSql(String sql, Map<String, Object> map, OrderQueryParams orderQueryParams) {
        if (orderQueryParams.getUserId() != null) {
            sql += " AND user_id = :userId";
            map.put("userId", orderQueryParams.getUserId());
        }
        return sql;
    }

}
