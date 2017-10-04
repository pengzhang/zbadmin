#CNENVS

##目录结构

   * index - 项目的主程序
   * module - 项目依赖的相关模块
        * core - 核心模块
        * cms - 内容管理
        * pay - 支付管理
        * mall - 商城管理

### 环境搭建

    1. 下载JDK1.8 安装配置环境变量
    2. 下载play framework 1.4.4 
        [http://www.playframework.com]
    3. 解压playframework.zip到指定目录
    4. 将playframework目录添加到Path中
    5. 测试play安装  play --version

### 程序启动

    1. 下载项目代码
    2. cd framework/module/core
    3. play deps
    4. play build-module 直接回车到完成
    5. cd framework/module/cms
    6. play deps
    7. 依次进入每个模块中执行play deps
    8. cd framework/index
    9. play deps
    10. play run 
    11. 打开浏览器: http://127.0.0.1:9000
    
### 修改index项目配置
    
    1. conf/application.conf  
    
       * db.default = 数据库连接
          * fs 使用H2数据库文件模式
          * mem 使用H2数据库内存模式
          * msyql 参考项目中格式
          * postgres 参考项目中格式
        
        * memcache=enabled

###帮助文档
http://open.cnenvs.com

