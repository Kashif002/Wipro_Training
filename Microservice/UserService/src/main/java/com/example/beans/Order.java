package com.example.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	Integer orderId;
	String orderName;
	Double orderPrice;
	Integer userId;
}
