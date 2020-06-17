package com.sample.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sample.entities.Test;
import com.sample.model.SqlQueries;
import com.sample.model.SqlQueries2;
import com.sample.model.projections.TreeProjection;

/**
 * Created by OmiD.HaghighatgoO on 8/21/2019.
 */

@Repository
public interface TestRepository extends JpaRepository<Test, Long>  {
	
	@Query(value = SqlQueries.sql, nativeQuery = true)
    public List<Test> findByName(String name) ;
    
    @Query(value = SqlQueries.GET_CHILD, nativeQuery = true)
    List<Object[]> fetchAllChilds(@Param("id") Integer id);
    
    @Query(value = SqlQueries.FETCH_CATEGORY_TREE, nativeQuery = true)
    List<TreeProjection> fetchCategoriesAsTree();
    
    @Query(value = SqlQueries.FETCH_CATEGORY_TREE, nativeQuery = true)
    List<Object[]> fetchCategoryTree();
    
    @Query(value = SqlQueries2.FETCH_CATEGORY_TREE, nativeQuery = true)
    List<TreeProjection> fetchCategoriesAsTree2();
    
    @Query(value = SqlQueries.PRODUCT_COUNT_IN_CATEGORY, nativeQuery = true)
    List<TreeProjection> fetchProductsCountInCategory();
}
