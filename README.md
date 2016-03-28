# Thrift-Gradle-Demo

- aim: 利用Gradle构建Thrift开发环境,无需开发者一个一个下载依赖包
  - example: [thrift实例以及所需jar包](http://download.csdn.net/detail/hjx_1000/8374829#comment)就预先下载好依赖包，可是并不适合实际情况，开发者难以动态配置
  - 本篇核心就是`gradle.buile`文件:
    - Eclipse编译过程: 
      - configure -> convert to Gradle(STS) Project
      - Gradle -> Refresh All -> Refresh Dependency: Gradle会自动下载依赖包
      - Run as Application

`gradle.buile` 文件：

```
apply plugin: 'java'
repositories { mavenCentral() }  
dependencies { compile 'org.apache.thrift:libthrift:0.9.2'
			   compile  'org.slf4j:slf4j-log4j12:1.7.19'
				compile  'log4j:log4j:1.2.17'
				} 
```

