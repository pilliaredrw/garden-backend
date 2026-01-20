## 创建项目

现在以一个个人博客、Memos 以及别的什么能想到的东西的汇聚端来练习 Springboot，并且借此机会 Java 复健。项目刘长春大概是从 RSS，Memos api 等方式得到数据，将数据同步之后塞入数据库练习 mybatis（或者 mybatis-plus）。

首先创建项目，git 直接初始化四连 git 提交。进行`git init`, `git add .`, `git commit -m "init"`, 之后进行`git checkout -b feat/rss-fetch`进入支线开发。 

## 构建最小跑通代码

创建基本 MCS(对于前后端分离而言, MVC 中的 V 被交于前端。而 S 是 service)，如下编写

1. 创建 model, 使用 lombok 自动构建 getter 和 setter 等。

   - @Data 注解，可以快速标记类为实体，自动构建 getter 和 setter 等。

2. 创建 service, 创建一个假方法获取 mock 数据测试连通性

   - @service 注解

3. 创建 controller, 使用 

   - 

## 本地化IDE

### 终端

- 终端快捷键修改为 `ctrl + shift + T`, 取代了某个查找。
- 修改终端字体为 10 大小.

### 代码格式

- 类和方法缩进变成下一行

### 系统快捷键



### 编辑器快捷键

- 添加`alt + A`自动换行
- 添加`ctrl + /`注释
- 移除`ctrl + alt + X`原有"添加审查意见"修改为"切换折叠"。


### 主题

## epoch1: 拉取 RSS

## 完成工作推送 git(以 github 为例)

将本地的 main 主线推送到远端的 main 主线。

```bash
git remote add origin https://github.com/你的用户名/garden-backend.git
git branch -M main
git push -u origin main
```



## epoch2: 最小跑通 DAO

1. 本地创建数据库`CREATE DATABASE garden_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`

2. maven(pom.xml) 引入 JDBC 驱动和 MyBatis。

##
