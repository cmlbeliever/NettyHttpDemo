# 基于Netty的仿SpringMvc服务器 #
    项目中主要的核心代码来自于Netty官方demo，经过改造后搭建成自己的服务器。
	基于SpringMVC实现原理，完成代码结构搭建。
	其中肯定有许多需要优化和修正的，欢迎指正。
## 实现功能 ##
1. 支持静态文件访问
2. 支持表单提交处理
3. 支持重定向

## 程序入口 ##
- NettyHttpServer：为程序入口，运行后即可启动服务
- 访问地址为localhost:8080

## UML图 ##
比较少使用UML图，如果有不对的地方，欢迎指正。
![首页](https://github.com/cmlbeliever/NettyHttpDemo/blob/master/wiki/img/uml.png)

## 有图有真相 ##
![首页](https://github.com/cmlbeliever/NettyHttpDemo/blob/master/wiki/img/index.png)
![登录完成界面](https://github.com/cmlbeliever/NettyHttpDemo/blob/master/wiki/img/loginResult.png)

## TODO ##
1. 使用Log替代System.out
1. 添加拦截器等功能