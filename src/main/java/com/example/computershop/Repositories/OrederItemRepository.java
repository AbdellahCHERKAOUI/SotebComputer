package com.example.computershop.Repositories;

import com.example.computershop.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrederItemRepository extends JpaRepository<OrderItem,Long> {
}
