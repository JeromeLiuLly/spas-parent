package com.candao.spas.convert.sample.mapstruct.bean.vo;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {
	
	private String name;
	
	private Integer age;

	private Double height;
	
	private String consigneePhones;
	
	private String birth;
	
	private List<Project> projects;

	private List<String> bag;
}
