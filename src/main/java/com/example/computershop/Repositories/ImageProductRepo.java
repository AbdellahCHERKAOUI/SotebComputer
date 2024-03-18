package com.example.computershop.Repositories;

import com.example.computershop.Entities.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageProductRepo extends JpaRepository<ImageProduct,Long> {
}
