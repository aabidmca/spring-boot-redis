package com.sample.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sample.model.SqlQueries;
import com.sample.model.projections.TreeNode;
import com.sample.model.projections.TreeProjection;
import com.sample.repository.TestRepository;

@Component
public class RedisUtil {

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	TestRepository repo;

	@Value("${redis.categories.expiry.time}")
	private Long expiryTimeInSecond;
	public final String NODE = "NODE";
	public final String CHILD_KEY = "CHILDREN";
	
	@PostConstruct
	public void initialize() {
		//fetchTree();
		System.out.println("-- Tree Created --");
	}

	public Object fetchTree() {
		long start = System.currentTimeMillis();
		System.out.println(SqlQueries.FETCH_CATEGORY_TREE);
		
		CompletableFuture<Map<Integer,Integer>> countListF = CompletableFuture.supplyAsync(() -> getCategoryCounts());
		//List<TreeProjection> list = repo.fetchCategoriesAsTree();
		List<Object[]> list = repo.fetchCategoryTree();
		
		Map<Integer, ArrayList<TreeNode>> childrenMap = new HashMap<>(500);
		Map<Integer, TreeNode> categoryInfo = new HashMap<>(500);
		Map<Integer,Integer> categoryCounts = countListF.join();
		
		for(Object[] category : list) {
			if(Objects.nonNull(category[1])) {
				//TreeNode t = new TreeNode(category, categoryCounts.get(category.getEntityId()));
				TreeNode t = new TreeNode(category, categoryCounts.get((Integer)category[1]));
				categoryInfo.put(t.getEntityId(), t);
			}
		}
		CompletableFuture<?> putAllNodeInRedisF = CompletableFuture.runAsync(() -> redisTemplate.opsForHash().putAll(NODE, categoryInfo));
		CompletableFuture<?> putChildInRedisF = CompletableFuture.runAsync(() -> populateCacheWithChildren(childrenMap, categoryInfo));
		putChildInRedisF.thenRun(() -> redisTemplate.opsForHash().putAll(CHILD_KEY, childrenMap));
		
		CompletableFuture<?> all = CompletableFuture.allOf(putAllNodeInRedisF, putChildInRedisF)
			.handle((result, ex) -> {
				if(Objects.nonNull(ex)) {
					ex.printStackTrace();
					return ex.getMessage();
				}
				return StringUtils.EMPTY;
			});
		Object result = all.join();
		if(Objects.nonNull(result) && result instanceof String && StringUtils.isNoneBlank((String)result)) {
			throw new RuntimeException("error while creating cache " + result);
		}
		
		redisTemplate.expire(NODE, expiryTimeInSecond, TimeUnit.SECONDS);
		redisTemplate.expire(CHILD_KEY, expiryTimeInSecond, TimeUnit.SECONDS);
		
		System.out.println("Total Time :" + (System.currentTimeMillis() - start));
		return "done";
	}

	private void populateCacheWithChildren(Map<Integer, ArrayList<TreeNode>> childrenMap,
			Map<Integer, TreeNode> categoryInfo) {
		for(Map.Entry<Integer, TreeNode> category : categoryInfo.entrySet()) {
			if(Objects.nonNull(category) || Objects.nonNull(category.getKey())) {
				childrenMap.computeIfAbsent(category.getValue().getParentId(), d -> new ArrayList<TreeNode>()).add(category.getValue());
			}
		}
	}

	public Map<Integer,Integer> getCategoryCounts() {
		long start = System.currentTimeMillis();
		List<TreeProjection> countList = repo.fetchProductsCountInCategory();
		long start1 = System.currentTimeMillis();
		Map<Integer,Integer> map = countList.parallelStream().collect(Collectors.toMap(
				TreeProjection::getEntityId, TreeProjection::getProducts));
		System.out.println("Total Time to execute getCategoryCounts() :" + (System.currentTimeMillis() - start));
		return map;
		
	}
	
	@SuppressWarnings("unchecked")
	public Object getChilds(Integer parent, Integer storeId) {
		HashMap<Object, Object> map = new HashMap<>();
		TreeNode parentEntityId = (TreeNode)redisTemplate.opsForHash().get(NODE, parent);
		if(Objects.isNull(parentEntityId)) {
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
