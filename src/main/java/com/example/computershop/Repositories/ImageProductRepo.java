package com.example.computershop.Repositories;

import com.example.computershop.Entities.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductRepo extends JpaRepository<ImageProduct,Long> {
    List<ImageProduct> getAllByFileName(String imageName);

    ImageProduct getImageByFileName(String imageName);

    void deleteImageProductByFileName(String filName);
}
