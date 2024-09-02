package com.example.springbootmall.dto;

import com.example.springbootmall.model.OrderItem;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateOrderRequest {

    @NotEmpty
    private List<BuyItem> buyItemList;

    public @NotEmpty List<BuyItem> getBuyItemList() {
        return buyItemList;
    }

    public void setBuyItemList(@NotEmpty List<BuyItem> buyItemList) {
        this.buyItemList = buyItemList;
    }
}
