package com.sample.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sample.model.SqlQueries2;
import com.sample.model.projections.TreeNode;
import com.sample.model.projections.TreeProjection;
import com.sample.repository.TestRepository;

@Component
public class RedisUtil2 {

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	TestRepository repo;

	@Value("${redis.categories.expiry.time}")
	private Long expireIn;
	public final String NODE = "NODE";
	public final String CHILD_KEY = "CHILDREN";
	
	@PostConstruct
	public void initialize() {
		
		//fetchTree();
		System.out.println("-- Tree Created --");
	}

	public Object fetchTree() {
		long start = System.currentTimeMillis();
		System.out.println(SqlQueries2.FETCH_CATEGORY_TREE);
		
		List<TreeProjection> list = repo.fetchCategoriesAsTree2();
		Map<Integer, ArrayList<TreeNode>> childrenMap = new HashMap<>(500);
		Map<Integer, TreeNode> categoryInfo = new HashMap<>(500);
		DozerBeanMapper mapper = DozerMapperUtil.getBeanMapper(TreeProjection.class, TreeNode.class);
		for(TreeProjection category : list) {
			if(Objects.nonNull(category) || Objects.nonNull(category.getEntityId())) {
				TreeNode t = new TreeNode();
				mapper.map(category, t);
				categoryInfo.put(t.getEntityId(), t);
			}
		}
		redisTemplate.opsForHash().putAll(NODE, categoryInfo);
		
		for(Map.Entry<Integer, TreeNode> category : categoryInfo.entrySet()) {
			if(Objects.nonNull(category) || Objects.nonNull(category.getKey())) {
				childrenMap.computeIfAbsent(category.getValue().getParentId(), d -> new ArrayList<TreeNode>()).add(category.getValue());
			}
		}
		redisTemplate.opsForHash().putAll(CHILD_KEY, childrenMap);
		
		redisTemplate.expire(NODE, expireIn, TimeUnit.SECONDS);
		redisTemplate.expire(CHILD_KEY, expireIn, TimeUnit.SECONDS);
		
		System.out.println("Total Time :" + (System.currentTimeMillis() - start));
		
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public Object getChilds(Integer parent, Integer storeId) {
		HashMap<Object, Object> map = new HashMap<>();
		TreeNode parentEntityId = (TreeNode)redisTemplate.opsForHash().get(NODE, parent);
		if(Objects.isNull(parentEntityId)) {
			System.out.println("----------------- Going to reload cache <<------->> ");
			fetchTree();
			System.out.println("----------------- Reloading cache done -----------------");
			parentEntityId = (TreeNode)redisTemplate.opsForHash().get(NODE, parent);
			if(Objects.isNull(parentEntityId)) {
				System.out.println("No data found for this category :[" + parent + "]");
				return null;
			}
		}
		parentEntityId.buildForRequestedStore(storeId);
		List<TreeNode> childs = (List<TreeNode>)redisTemplate.opsForHash().get(CHILD_KEY, parent);
		childs.forEach(c -> c.buildForRequestedStore(storeId));
		map.put(parentEntityId, childs);
		return map;
	}
}
