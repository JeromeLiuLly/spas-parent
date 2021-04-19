# 项目结构
  - spas-convert-core: 项目工具集
  - spas-convert-sample: 项目示例(包含mapstruct实体映射框架)
  - spas-convert-sdk: 项目核心业务逻辑
  - spas-convert-spring-boot-starter: 项目web服务,端口: 9999
  - spas-convert-swing: 项目界面化配置
  - spas-dependencies: 项目版本引用

## 架构设计和界面
   

可视化界面转换配置
![实例界面](https://raw.githubusercontent.com/JeromeLiuLly/spas-parent/master/doc/实例界面.png)

可视化界面验证结果
![界面示例](https://raw.githubusercontent.com/JeromeLiuLly/spas-parent/master/doc/结果示例.png)

## 使用示例 
```
<dependency>
    <groupId>com.candao.spas</groupId>
    <artifactId>spas-convert-sdk</artifactId>
    <version>${revision}</version>
</dependency>
```
JsonCovertUtils.convert(inJson,spec);


