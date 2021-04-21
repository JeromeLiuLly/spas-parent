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

针对可视化配置，需要事先上传json文件到指定目录（in.json、out.json）  
in.json: 源json数据示例  
out.json: 目标json数据示例

![生成输出示例](https://raw.githubusercontent.com/JeromeLiuLly/spas-parent/master/doc/生成输出示例.png)
根据以上两个文件,生成两端的转换对象。  
spen-in.json: 对象转换协议  
spen-out.json: 转换后的对象规则  

![操作实例](https://raw.githubusercontent.com/JeromeLiuLly/spas-parent/master/doc/操作实例.gif)

进行在线转换验证（spas-convert-spring-boot-starter）
将in.json、spen-in.json 内容传入指定文本框内

![可视化验证](https://raw.githubusercontent.com/JeromeLiuLly/spas-parent/master/doc/可视化验证.gif)

```
<dependency>
    <groupId>com.candao.spas</groupId>
    <artifactId>spas-convert-sdk</artifactId>
    <version>${revision}</version>
</dependency>
```

## 基于WebSwing实现swing服务化
```
WebSwing官网：https://www.webswing.org/
war部署：https://storage.googleapis.com/builds.webswing.org/releases/webswing-examples-eval-21.1-distribution.zip
或者详见 doc/webswing下的目录，有对应压缩包。具体部署和操作，请参考官网即可。
```