package com.sample.model.projections;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer parentId;
	private Integer entityId;
	private Integer position;
	private Integer products;
	private Integer totalProducts;
	private String stores;
	private String categoryName;

	public TreeNode(Object[] category, Integer products2) {
		this.parentId = (Integer)category[0];
		this.entityId = (Integer)category[1];
		this.position = (Integer)category[2];
		this.stores = (String)category[3];
		this.categoryName = (String)category[4];
		this.products = products2;
	}

	public void buildForRequestedStore(Integer storeId) {
		String[] storeIds = stores.split(",");
		this.stores = null;
		if(Objects.nonNull(categoryName) && categoryName.indexOf(",") > -1) {
			String[] categories = categoryName.split(",");
			if(storeId == 0) {
				this.categoryName = categories[0];
			} else {
				boolean isFound = false;
				for(int index = 0; index < storeIds.length; index ++) {
					if(Integer.parseInt(storeIds[index]) == storeId) {
						isFound = true;
						this.categoryName = categories[index]; break;
					}
				}
				if(isFound == false)
					this.categoryName = categories[0];
			}
		}
	}

	
}
