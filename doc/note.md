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

由于我们

### 附: 当错误提交之后的补救措施

当身处一个错误的分支，例如需要在 branch:feat-rss 开发，并且完成后通过 add 和 commit 进行暂存(add)和提交(commit)。但是有时会一不小心 add 甚至 commit 到预期以外的线（例如个人开发中的 main 线或者团队的 dev 线（如果有足够权限））。此时就需要下面这些方式来救场。首先通过`git status`来确认状态，通过`git branch`检查目前本地有几条线。切换过去之后，通过`git log --oneline --decorate --max-count=10`来查看这条线上的提交内容。

1. 当仅仅只是在错误的 branch 上进行 add 操作，那么只需要`git switch <BRANCH NAME>`就行了。或者`git checkout <BRANCH NAME>`（git checkout -b 是新建）。

2. 当已经完成一次 commit，那么首先通过`git reset --soft HEAD^`进行一步撤销（回到本次 commit 所创建的 head 指针所指向的前一个结点），其中`--soft`表示将更改放回暂存区，`HEAD^`就是上一个版本的指针。执行后，情况回到了 1. 所示。

## epoch2: 最小跑通 DAO

1. 本地创建数据库`CREATE DATABASE garden_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`

2. maven(pom.xml) 引入 JDBC 驱动和 MyBatis。

##
