package com.example.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	@Id
 	Integer orderId;	
	String product;
	Integer price;
	Integer quantity;
	Integer userId;

}
