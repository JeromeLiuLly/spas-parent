package com.candao.spas.convert.sample.mapstruct;

import com.candao.spas.convert.sample.mapstruct.bean.dto.StudentDto;
import com.candao.spas.convert.sample.mapstruct.bean.vo.Project;
import com.candao.spas.convert.sample.mapstruct.bean.vo.Student;
import com.candao.spas.convert.sample.mapstruct.converter.StudentConverter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MapStructTest {

	public static void main(String[] args) {

		Student student = new Student();
		student.setName("jeromeLiu");
		student.setAge(18);
		student.setConsigneePhones("123456789");
		student.setBirth("1618456195000");
		student.setHeight(178.113);

		List<String> list = new ArrayList<String>();
		list.add("铅笔");
		list.add("课本");
		list.add("橡皮擦");
		student.setBag(list);

		Project yuwen = new Project();
		yuwen.setName("语文");
		yuwen.setTeacherId("1140120");

		Project shuxue = new Project();
		shuxue.setName("数学");
		shuxue.setTeacherId("1140121");

		List<Project> projects = new ArrayList<Project>();
		projects.add(yuwen);
		projects.add(shuxue);
		student.setProjects(projects);

		StudentDto studentDto = StudentConverter.INSTANCE.domain2dto(student);
		
		System.out.println("转换前："+new Gson().toJson(student));
		System.out.println("转换后："+new Gson().toJson(studentDto));
		
	}

}
