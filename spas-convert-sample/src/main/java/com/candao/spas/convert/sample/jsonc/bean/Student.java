package com.candao.spas.convert.sample.jsonc.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
	
	private String name;
	
	private Integer age;
	
	private String consigneePhones;
	
	private String birth;
	
	private List<Project> projects;

	private String[] bag;
}