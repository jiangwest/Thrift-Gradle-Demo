# Thrift-Gradle-Demo

- aim: 利用Gradle构建Thrift开发环境,无需开发者一个一个下载依赖包
  - example: 示例[thrift实例以及所需jar包](http://download.csdn.net/detail/hjx_1000/8374829#comment)就预先下载好依赖包，可是并不适合实际情况，开发者难以动态配置
  - 本篇核心就是`gradle.buile`文件:
  	- Eclipse Marketplace 下载 gradle for eclipse插件: `Buildship Gradle integration`
     	- Eclipse编译过程: 
      	- configure -> convert to Gradle(STS) Project
      	- Gradle -> Refresh All -> Refresh Dependency: Gradle会自动下载依赖包
	- Run as Application
	

- Client: 客户端
- Server: 服务端
- 运行: 先Server，再Client	


`gradle.buile` 文件：

```
apply plugin: 'java'
repositories { mavenCentral() }  
dependencies { compile 'org.apache.thrift:libthrift:0.9.3'
			   compile  'org.slf4j:slf4j-log4j12:1.7.19'
				compile  'log4j:log4j:1.2.17'
				} 
```

- 注意build.gradle文件 `compile`中  `org.apache.thrift:libthrift:0.9.3` 就是所需jar包
	- 项目右键，点击 Gradle -> Refresh All
	- 依赖包 -> 网站 [mvnrepository](http://mvnrepository.com/),包括Maven和Gradle格式
- gradle清除下载包
 - 右键选择 Gradle -> Disable Dependency Manage
