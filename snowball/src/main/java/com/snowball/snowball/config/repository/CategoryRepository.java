package com.snowball.snowball.config.repository;

import com.snowball.snowball.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}