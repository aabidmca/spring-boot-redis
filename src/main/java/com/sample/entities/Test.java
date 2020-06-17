package com.sample.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by OmiD.HaghighatgoO on 8/21/2019.
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "test")
public class Test implements Serializable{

	private static final long serialVersionUID = -8243145429438016231L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="test_id")
    private Long testId;

    @Column(name="name")
    private String name;
    
    @Column(name="created_at")
    private Timestamp createdAt;

    public Test(Long id, String name) {
        this.testId = id;
        this.name = name;
    }
}
