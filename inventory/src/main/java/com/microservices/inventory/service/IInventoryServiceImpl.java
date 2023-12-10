package com.microservices.inventory.service;

import com.microservices.inventory.dto.BaseResponse;
import com.microservices.inventory.dto.OrderItemRequest;
import com.microservices.inventory.model.Inventory;
import com.microservices.inventory.repository.IInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IInventoryServiceImpl implements IInventoryService {

    private final IInventoryRepository inventoryRepository;

    @Override
    public boolean isInStock(String sku) {
        var inventory = inventoryRepository.findBySku(sku);
        return inventory.filter(value -> value.getQuantity() > 0).isPresent();
    }

    @Override
    public BaseResponse areInStock(List<OrderItemRequest> orderItems) {
        var errorList = new ArrayList<String>();

        List<String> skuList = orderItems.stream().map(OrderItemRequest::getSku).toList();

        List<Inventory> inventoryList = inventoryRepository.findBySkuIn(skuList);

        orderItems.forEach(orderItem -> {
            var inventory = inventoryList.stream().filter(value -> value.getSku().equals(orderItem.getSku())).findFirst();
            if (inventory.isEmpty()) {
                errorList.add("Sku not found: " + orderItem.getSku());
            } else if (inventory.get().getQuantity() < orderItem.getQuantity()) {
                errorList.add("Insufficient quantity for sku: " + orderItem.getSku());
            }
        });

        return errorList.size() > 0 ? new BaseResponse(errorList.toArray(new String[0])) : new BaseResponse(null);
    }
}




