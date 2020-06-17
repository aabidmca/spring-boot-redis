package com.sample.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.util.RedisUtil;
import com.sample.util.RedisUtil2;

/**
 * Created by OmiD.HaghighatgoO on 8/4/2019.
 */

@RestController
@RequestMapping("/v1/catalog/categories")
public class TreeController {

	@Autowired
    RedisUtil redisUtil;
	
	@Autowired
    RedisUtil2 redisUtil2;

	@GetMapping("/tree")
    public ResponseEntity<?> createTree() {
    	return new ResponseEntity<>(redisUtil.fetchTree(), HttpStatus.OK);
    }
    
    @GetMapping("/tree2")
    public ResponseEntity<?> createTree2() {
    	return new ResponseEntity<>(redisUtil2.fetchTree(), HttpStatus.OK);
    }
    
    @GetMapping("/tree/{parentId}/store/{storeId}")
    public ResponseEntity<?> getChildren(@PathVariable("parentId") Integer entityId, @PathVariable("storeId") Integer storeId) {
    	return new ResponseEntity<>(redisUtil.getChilds(entityId, storeId), HttpStatus.OK);
    }
    
}

