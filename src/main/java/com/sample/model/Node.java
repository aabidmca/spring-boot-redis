package com.sample.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor

public class Node implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private Integer rowId;
	private Integer entityId;
	private Integer position;
	private Integer parentId;
	List<Node> child;
	
}
