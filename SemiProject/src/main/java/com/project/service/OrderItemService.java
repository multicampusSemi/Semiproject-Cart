package com.project.service;

import java.util.HashMap;
import java.util.List; 
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.project.mapper.LjmSemiMapper;
import com.project.model.OrderItem;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {
	private final LjmSemiMapper orderMapper;
	
	public void saveOrder(int userId, List<OrderItem> selectedProducts) {
	    int totalPrice = 0;
	    for (OrderItem product : selectedProducts) {
	    	System.out.println("Selected product: productId = " + product.getProductId() + 
                    ", quantity = " + product.getQuantity() +
                    ", bookingId = " + product.getBookingId());
	    }
	    
	    // 사용자에 해당하는 bookingId 리스트 조회
	    List<Integer> bookingIds = orderMapper.getBookingIdByUserId(userId);
	    AtomicReference<Integer> bookingId = new AtomicReference<>(null); 

	    if (bookingIds.isEmpty()) {
	        // bookingId가 없다면, 새로운 예약을 생성
	        bookingId.set(orderMapper.generateBookingId());  // DB에서 자동 생성된 booking_id를 가져오는 메서드
	    } else {
	        bookingId.set(bookingIds.get(0));  // 첫 번째 bookingId를 사용
	    }
	    
	    // 선택된 각 제품에 대해 총 금액 계산
	    for (OrderItem product : selectedProducts) {
	        int quantity = product.getQuantity();
	        int productPrice = product.getProductPrice();
	        int totalAmount = productPrice * quantity;  // 제품의 총 금액 계산
	        product.setTotalAmount(totalAmount);  // 제품 모델에 총 금액 설정
	        totalPrice += totalAmount;  // 전체 총 금액에 추가
	        System.out.println("Selected product: productId = " + product.getProductId() + 
                    ", quantity = " + product.getQuantity() +
                    ", bookingId = " + product.getBookingId());
	    }

	    String status = "PENDING"; // 기본 상태를 'PENDING'으로 설정

	    // 주문 총액을 삽입
	    Map<String, Object> params = new HashMap<>();
	    params.put("bookingId", bookingId.get());  // AtomicReference에서 값 가져오기
	    params.put("userId", userId);
	    params.put("totalPrice", totalPrice);
	    params.put("status", status);
	    orderMapper.insertOrderTotal(params);

	    // 각 제품에 대한 주문 항목 저장
	    selectedProducts.forEach(product -> {
	        product.setUserId(userId);
	        product.setBookingId(bookingId.get());  // AtomicReference에서 값 가져오기
	        orderMapper.insertOrder(bookingId.get(), product.getProductId(), product.getQuantity());  // 주문 항목 삽입
	    });
	}
	
	
	 public List<OrderItem> getOrders(int userId) {
		 List<OrderItem> orderItems = orderMapper.getAllOrders(userId);
		 return orderItems;
	    }

	    
	 
	 public int calculateTotalPrice(List<OrderItem> orders) {
	        int totalPrice = 0;
	        for (OrderItem order : orders) {
	            totalPrice += order.getProductPrice() * order.getQuantity();
	        }
	        return totalPrice;
	    }
}