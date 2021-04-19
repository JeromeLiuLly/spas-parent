package com.candao.spas.convert.sample.jsonc;

import com.candao.spas.convert.sample.jsonc.bean.Project;
import com.candao.spas.convert.sample.jsonc.bean.Student;
import com.candao.spas.convert.sdk.utils.JsonCovertUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonConvertTest {
	
	public static void main(String[] args) throws Exception {
        Student student = new Student();
        student.setName("jeromeLiu");
        student.setAge(18);
        student.setConsigneePhones("123456789");
        student.setBirth("1618456195000");
        student.setBag(new String[]{"铅笔","课本","橡皮擦"});
        
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
		
        String inJson = new Gson().toJson(student);
        
        String spec = "{ \"name\":\"userName\",\"age\":\"age\",\"consigneePhones\":\"phone\",\"birth,cvt,numToDateStr,yyyy-MM-dd HH:mm:ss\":\"birthday\",\"projects\":{\"*\":{\"name\":\"pro.*.project\",\"teacherId\":\"pro.*.value\"}},\"bag\": {\"*\": \"back.*.name\"}}";


        Map<?,?> result = JsonCovertUtils.convert(inJson,spec);
        
        System.out.println("输入json对象:\n"+new Gson().toJson(student));
        System.out.println("转换协议:\n"+spec);
        System.out.println("转换后json对象:\n"+new Gson().toJson(result));
    }
	
}
