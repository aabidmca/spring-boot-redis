package com.sample.model;

public class SqlQueries2 {

	public static final String query = 
			"SELECT \n" + 
    		"	s.id as id, s.row_id as rowId, s.position as position, group_concat(s.childEntityIds order by s.childEntityIds) as childEntityIds, \n" + 
    		"	group_concat(s.childRowIds order by s.childRowIds)  as childRowIds, group_concat(s.childPositions order by s.childPositions) as childPositions \n" + 
    		"FROM (\n" + 
    		"		SELECT \n" + 
    		"			cce.parent_id  as id, cce.entity_id as childEntityIds, cce.row_id as childRowIds, cce.position as childPositions, \n" + 
    		"			e.row_id, e.position \n" + 
    		"		FROM catalog_category_entity cce \n" + 
    		"		LEFT JOIN catalog_category_entity e on e.entity_id = cce.parent_id \n" + 
    		"		ORDER BY cce.parent_id, cce.entity_id \n" + 
    		"	) s , (SELECT @pv \\:= 1) initialisation \n" + 
    		"WHERE   FIND_IN_SET(s.id, @pv) \n" + 
    		"AND     LENGTH(@pv \\:= CONCAT(@pv, ',', s.childEntityIds)) GROUP BY s.row_id";
	
	public static final String GET_CHILD = "" +
	    	"select  s.entity_id, \n" + 
	    	"        s.row_id, \n" + 
	    	"        s.parent_id \n" + 
	    	"from    (select * from catalog_category_entity order by parent_id, entity_id) s , \n" + 
	    	"		(select @pv \\:= :id) initialisation \n" + 
	    	"where   find_in_set(parent_id, @pv) \n" + 
	    	"and     length(@pv \\:= concat(@pv, ',', s.entity_id)) ";
	
	public static final String FETCH_CATEGORY_TREE = "\n" +
	    	"SELECT \n" + 
	    	"	cce.parent_id as parentId, cce.entity_id as entityId, cce.position as position, ccp.products, \n" + 
	    	"   group_concat(var.store_id) as stores,\n" + 
	    	"	group_concat(var.value) as categoryName \n" + 
	    	"FROM catalog_category_entity cce \n" + 
	    	"INNER JOIN catalog_category_entity_varchar var ON \n" + 
	    	"	var.row_id = cce.row_id AND \n" + 
	    	"	var.attribute_id = 41 AND store_id IN (0,1,3) \n" +
	    	"LEFT JOIN " +
	    	"(SELECT category_id, count(category_id) as products from catalog_category_product GROUP BY category_id) ccp " + 
	    	"	ON ccp.category_id = cce.entity_id " +
	    	"WHERE (:categoryId is null OR category_id = :categoryId) \n" +
	    	"GROUP BY cce.entity_id \n" + 
	    	"ORDER BY cce.parent_id, cce.position";
	
	public static final String PRODUCT_COUNT_IN_CATEGORY = 
		"SELECT category_id as entityId, count(category_id) as products from catalog_category_product \n" + 
		"WHERE (:categoryId is null OR category_id = :categoryId) GROUP BY category_id";
	
	public static final String sql = "SELECT eat.entity_type_code, ea.attribute_code, ea.attribute_id \n" +
	"FROM eav_attribute ea INNER JOIN eav_entity_type eat on ea.entity_type_id = eat.entity_type_id";
}
