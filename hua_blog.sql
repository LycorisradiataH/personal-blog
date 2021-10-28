/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : hua_blog

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 23/10/2021 18:59:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hua_article
-- ----------------------------
DROP TABLE IF EXISTS `hua_article`;
CREATE TABLE `hua_article`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `author_id` int NOT NULL COMMENT '作者id',
  `category_id` int NULL DEFAULT NULL COMMENT '文章分类id',
  `article_cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章缩略图',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `type` tinyint(1) NULL DEFAULT 1 COMMENT '文章类型 1原创 2转载 3翻译',
  `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原文链接',
  `is_top` tinyint(1) NULL DEFAULT 0 COMMENT '是否置顶 0否 1是',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态值 1公开 2私密 3评论可见',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除  0否 1是',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_article
-- ----------------------------
INSERT INTO `hua_article` VALUES (1, 1, 1, 'http://r19jqirt4.hn-bkt.clouddn.com/article/c77d8277-4cb4-45e8-b945-721ed75052d6.jpg', '测试标题', '# 测试\n## 测试添加文章\n### 测试添加图片\n![1.jpg](http://r19jqirt4.hn-bkt.clouddn.com/article/097d5789-2ad8-4a7b-856c-b26ef32e157d.jpg)', 1, NULL, 1, 1, 0, '2021-10-11 16:49:40', '2021-10-20 14:39:49');

-- ----------------------------
-- Table structure for hua_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `hua_article_tag`;
CREATE TABLE `hua_article_tag`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL COMMENT '文章id',
  `tag_id` int NOT NULL COMMENT '标签id',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id`) USING BTREE,
  INDEX `idx_tag_id`(`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_article_tag
-- ----------------------------
INSERT INTO `hua_article_tag` VALUES (1, 1, 1, '2021-10-11 16:49:24', '2021-10-11 16:49:24');

-- ----------------------------
-- Table structure for hua_category
-- ----------------------------
DROP TABLE IF EXISTS `hua_category`;
CREATE TABLE `hua_category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_category
-- ----------------------------
INSERT INTO `hua_category` VALUES (1, '测试分类', '2021-10-11 16:48:55', '2021-10-11 16:49:13');

-- ----------------------------
-- Table structure for hua_comment
-- ----------------------------
DROP TABLE IF EXISTS `hua_comment`;
CREATE TABLE `hua_comment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '评论用户Id',
  `article_id` int NULL DEFAULT NULL COMMENT '评论文章id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `reply_id` int NULL DEFAULT NULL COMMENT '回复用户id',
  `parent_id` int NULL DEFAULT NULL COMMENT '父评论id',
  `is_audit` tinyint(1) NULL DEFAULT 1 COMMENT '是否审核',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_comment_user`(`user_id`) USING BTREE,
  INDEX `idx_comment_article`(`article_id`) USING BTREE,
  INDEX `idx_comment_parent`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_comment
-- ----------------------------
INSERT INTO `hua_comment` VALUES (1, 1, 1, '测试评论', NULL, NULL, 1, '2021-10-12 19:19:55', '2021-10-13 23:01:26');
INSERT INTO `hua_comment` VALUES (2, 1, 1, '测试回复', 1, 1, 1, '2021-10-12 19:20:06', '2021-10-13 23:01:27');
INSERT INTO `hua_comment` VALUES (3, 1, 1, '测试回复的回复', 1, 1, 1, '2021-10-12 19:20:14', '2021-10-14 00:11:40');

-- ----------------------------
-- Table structure for hua_menu
-- ----------------------------
DROP TABLE IF EXISTS `hua_menu`;
CREATE TABLE `hua_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单路径',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组件',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单icon',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `order_num` tinyint(1) NOT NULL COMMENT '排序',
  `parent_id` int NULL DEFAULT NULL COMMENT '父id',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏  0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_menu
-- ----------------------------
INSERT INTO `hua_menu` VALUES (1, '首页', '/', '/home/Home.vue', 'el-icon-myshouye', '2021-10-16 17:25:30', '2021-10-16 17:25:30', 1, NULL, 0);
INSERT INTO `hua_menu` VALUES (2, '文章管理', '/article-submenu', 'layout', 'el-icon-mywenzhang-copy', '2021-10-16 17:25:30', '2021-10-16 17:27:02', 2, NULL, 0);
INSERT INTO `hua_menu` VALUES (3, '消息管理', '/message-submenu', 'layout', 'el-icon-myxiaoxi', '2021-10-16 17:25:30', '2021-10-16 17:27:01', 3, NULL, 0);
INSERT INTO `hua_menu` VALUES (4, '系统管理', '/system-submenu', 'layout', 'el-icon-myshezhi', '2021-10-16 17:25:30', '2021-10-16 17:27:00', 5, NULL, 0);
INSERT INTO `hua_menu` VALUES (5, '个人中心', '/setting', '/setting/Setting.vue', 'el-icon-myuser', '2021-10-16 17:25:30', '2021-10-16 17:25:30', 7, NULL, 0);
INSERT INTO `hua_menu` VALUES (6, '发布文章', '/article', '/article/Article.vue', 'el-icon-myfabiaowenzhang', '2021-10-16 17:25:30', '2021-10-16 20:44:54', 1, 2, 0);
INSERT INTO `hua_menu` VALUES (7, '修改文章', '/article/*', '/article/Article.vue', 'el-icon-myfabiaowenzhang', '2021-10-16 17:25:30', '2021-10-16 20:44:56', 2, 2, 1);
INSERT INTO `hua_menu` VALUES (8, '文章列表', '/article-list', '/article/ArticleList.vue', 'el-icon-mywenzhangliebiao', '2021-10-16 17:25:30', '2021-10-16 17:25:30', 3, 2, 0);
INSERT INTO `hua_menu` VALUES (9, '分类管理', '/category', '/category/Category.vue', 'el-icon-myfenlei', '2021-10-16 17:25:30', '2021-10-16 20:44:59', 4, 2, 0);
INSERT INTO `hua_menu` VALUES (10, '标签管理', '/tag', '/tag/Tag.vue', 'el-icon-myicontag', '2021-10-16 17:25:30', '2021-10-16 20:45:00', 5, 2, 0);
INSERT INTO `hua_menu` VALUES (11, '评论管理', '/comment', '/comment/Comment.vue', 'el-icon-mypinglunzu', '2021-10-16 17:25:30', '2021-10-16 20:45:01', 1, 3, 0);
INSERT INTO `hua_menu` VALUES (12, '留言管理', '/message', '/message/Message.vue', 'el-icon-myliuyan', '2021-10-16 17:25:30', '2021-10-16 20:45:02', 2, 3, 0);
INSERT INTO `hua_menu` VALUES (13, '用户列表', '/user', '/user/User.vue', 'el-icon-myyonghuliebiao', '2021-10-16 17:25:30', '2021-10-16 20:45:02', 1, 22, 0);
INSERT INTO `hua_menu` VALUES (14, '角色管理', '/role', '/role/Role.vue', 'el-icon-myjiaoseliebiao', '2021-10-16 17:25:30', '2021-10-16 20:45:04', 2, 28, 0);
INSERT INTO `hua_menu` VALUES (15, '接口管理', '/resource', '/resource/Resource.vue', 'el-icon-myjiekouguanli', '2021-10-16 17:25:30', '2021-10-16 20:45:05', 2, 28, 0);
INSERT INTO `hua_menu` VALUES (16, '菜单管理', '/menu', '/menu/Menu.vue', 'el-icon-mycaidan', '2021-10-16 17:25:30', '2021-10-16 20:45:06', 2, 28, 0);
INSERT INTO `hua_menu` VALUES (18, '关于我', '/about', '/about/About.vue', 'el-icon-myguanyuwo', '2021-10-16 17:25:30', '2021-10-16 17:25:30', 4, 4, 0);
INSERT INTO `hua_menu` VALUES (19, '日志管理', '/log-submenu', 'layout', 'el-icon-myguanyuwo', '2021-10-16 17:25:30', '2021-10-16 17:26:59', 6, NULL, 0);
INSERT INTO `hua_menu` VALUES (20, '操作日志', '/operation/log', '/log/Operation.vue', 'el-icon-myguanyuwo', '2021-10-16 17:25:30', '2021-10-16 17:25:30', 1, 19, 0);
INSERT INTO `hua_menu` VALUES (21, '在线用户', '/online/user', '/user/Online.vue', 'el-icon-myyonghuliebiao', '2021-10-16 17:25:30', '2021-10-16 20:45:11', 7, 22, 0);
INSERT INTO `hua_menu` VALUES (22, '用户管理', '/user-submenu', 'layout', 'el-icon-myyonghuliebiao', '2021-10-16 17:25:30', '2021-10-16 20:45:20', 4, NULL, 0);
INSERT INTO `hua_menu` VALUES (26, '页面管理', '/page', '/page/Page.vue', 'el-icon-myyemianpeizhi', '2021-10-16 17:25:30', '2021-10-16 20:45:26', 2, 4, 0);
INSERT INTO `hua_menu` VALUES (28, '权限管理', '/permission-submenu', 'layout', 'el-icon-mydaohanglantubiao_quanxianguanli', '2021-10-16 17:25:30', '2021-10-16 17:26:54', 4, NULL, 0);
INSERT INTO `hua_menu` VALUES (29, '网站管理', '/website', '/website/Website.vue', 'el-icon-myxitong', '2021-10-16 17:25:30', '2021-10-16 17:25:30', 1, 4, 0);

-- ----------------------------
-- Table structure for hua_message
-- ----------------------------
DROP TABLE IF EXISTS `hua_message`;
CREATE TABLE `hua_message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像',
  `message_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言内容',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户地址',
  `time` tinyint(1) NULL DEFAULT NULL COMMENT '弹幕速度',
  `is_audit` tinyint(1) NULL DEFAULT 1 COMMENT '是否审核',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_message
-- ----------------------------
INSERT INTO `hua_message` VALUES (1, '游客', 'https://gravatar.loli.net/avatar/d41d8cd98f00b204e9800998ecf8427e?d=mp&v=1.4.14', '测试添加留言', '127.0.0.1', '', 7, 1, '2021-10-14 14:21:14', '2021-10-14 14:21:13');
INSERT INTO `hua_message` VALUES (2, '游客', 'https://www.static.talkxj.com/photos/0bca52afdb2b9998132355d716390c9f.png', '测试', '127.0.0.1', '', 9, 1, '2021-10-19 22:30:41', '2021-10-19 22:30:41');

-- ----------------------------
-- Table structure for hua_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `hua_operation_log`;
CREATE TABLE `hua_operation_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `opt_module` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作模块',
  `opt_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型',
  `opt_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作url',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作方法',
  `opt_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作描述',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求参数',
  `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '返回数据',
  `user_id` int NOT NULL COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `ip_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作地址',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for hua_page
-- ----------------------------
DROP TABLE IF EXISTS `hua_page`;
CREATE TABLE `hua_page`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '页面id',
  `page_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '页面名',
  `page_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面标签',
  `page_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '页面封面',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '页面' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_page
-- ----------------------------
INSERT INTO `hua_page` VALUES (1, '首页', 'home', 'http://r19jqirt4.hn-bkt.clouddn.com/home.jpg', '2021-10-20 17:37:08', '2021-10-20 17:37:08');
INSERT INTO `hua_page` VALUES (2, '归档', 'archive', 'http://r19jqirt4.hn-bkt.clouddn.com/archive.jpg', '2021-10-20 17:37:32', '2021-10-20 17:37:32');
INSERT INTO `hua_page` VALUES (3, '分类', 'category', 'http://r19jqirt4.hn-bkt.clouddn.com/category.jpg', '2021-10-20 17:37:45', '2021-10-20 17:39:05');
INSERT INTO `hua_page` VALUES (4, '标签', 'tag', 'http://r19jqirt4.hn-bkt.clouddn.com/tag.jpg', '2021-10-20 17:37:54', '2021-10-20 17:39:09');
INSERT INTO `hua_page` VALUES (5, '关于', 'about', 'http://r19jqirt4.hn-bkt.clouddn.com/tag.jpg', '2021-10-20 17:38:10', '2021-10-20 17:39:13');
INSERT INTO `hua_page` VALUES (6, '留言', 'message', 'http://r19jqirt4.hn-bkt.clouddn.com/message.jpg', '2021-10-20 17:38:25', '2021-10-20 17:40:43');
INSERT INTO `hua_page` VALUES (7, '个人中心', 'user', 'http://r19jqirt4.hn-bkt.clouddn.com/userInfo.jpg', '2021-10-20 17:38:36', '2021-10-20 17:41:06');
INSERT INTO `hua_page` VALUES (8, '文章列表', 'articleList', 'http://r19jqirt4.hn-bkt.clouddn.com/articleList.jpg', '2021-10-20 17:38:50', '2021-10-20 17:45:32');

-- ----------------------------
-- Table structure for hua_resource
-- ----------------------------
DROP TABLE IF EXISTS `hua_resource`;
CREATE TABLE `hua_resource`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限路径',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `parent_id` int NULL DEFAULT NULL COMMENT '父权限id',
  `is_anonymous` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否匿名访问 0否 1是',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_resource
-- ----------------------------
INSERT INTO `hua_resource` VALUES (1, '分类模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (2, '博客信息模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (3, '文章模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (4, '日志模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (5, '标签模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (6, '用户模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (7, '留言模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (8, '菜单模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (9, '角色模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (10, '评论模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (11, '资源模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (12, '页面模块', NULL, NULL, NULL, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (13, '查看博客信息', '/', 'GET', 2, 1, '2021-10-20 16:25:35', '2021-10-20 17:26:28');
INSERT INTO `hua_resource` VALUES (14, '查看关于我信息', '/about', 'GET', 2, 1, '2021-10-20 16:25:35', '2021-10-20 17:26:29');
INSERT INTO `hua_resource` VALUES (15, '查看后台信息', '/admin', 'GET', 2, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (16, '修改关于我信息', '/admin/about', 'PUT', 2, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (17, '查看后台文章', '/admin/article', 'GET', 3, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (18, '添加或修改文章', '/admin/article', 'POST', 3, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (19, '恢复或删除文章', '/admin/article', 'PUT', 3, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (20, '物理删除文章', '/admin/article', 'DELETE', 3, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (21, '上传文章图片', '/admin/article/image', 'POST', 3, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (22, '修改文章置顶', '/admin/article/top', 'PUT', 3, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (23, '根据id查看后台文章', '/admin/article/*', 'GET', 3, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (24, '查看后台分类列表', '/admin/category', 'GET', 1, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (25, '添加或修改分类', '/admin/category', 'POST', 1, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (26, '删除分类', '/admin/category', 'DELETE', 1, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (27, '搜索文章分类', '/admin/category/search', 'GET', 1, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (28, '查询后台评论', '/admin/comment', 'GET', 10, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (29, '删除评论', '/admin/comment', 'DELETE', 10, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (30, '审核评论', '/admin/comment/audit', 'PUT', 10, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (31, '上传博客配置图片', '/admin/config/images', 'POST', 2, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (32, '查看操作日志', '/admin/log', 'GET', 4, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (33, '删除操作日志', '/admin/log', 'DELETE', 4, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (34, '查看菜单列表', '/admin/menu', 'GET', 8, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (35, '新增或修改菜单', '/admin/menu', 'POST', 8, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (36, '删除菜单', '/admin/menu/*', 'DELETE', 8, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (37, '查看后台留言列表', '/admin/message', 'GET', 7, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (38, '删除留言', '/admin/message', 'DELETE', 7, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (39, '审核留言', '/admin/messages/audit', 'PUT', 7, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (40, '获取页面列表', '/admin/page', 'GET', 12, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (41, '保存或更新页面', '/admin/page', 'POST', 12, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (42, '删除页面', '/admin/page/*', 'DELETE', 12, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (43, '查看资源列表', '/admin/resource', 'GET', 11, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (44, '新增或修改资源', '/admin/resource', 'POST', 11, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (45, '导入swagger接口', '/admin/resource/import/swagger', 'GET', 11, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (46, '删除资源', '/admin/resources/*', 'DELETE', 11, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (47, '查询角色列表', '/admin/role', 'GET', 9, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (48, '保存或更新角色', '/admin/role', 'POST', 9, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (49, '删除角色', '/admin/role', 'DELETE', 9, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (50, '查看角色菜单选项', '/admin/role/menu', 'GET', 8, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (51, '查看角色资源选项', '/admin/role/resource', 'GET', 11, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (52, '查询后台标签列表', '/admin/tag', 'GET', 5, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (53, '添加或修改标签', '/admin/tag', 'POST', 5, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (54, '删除标签', '/admin/tag', 'DELETE', 5, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (55, '搜索文章标签', '/admin/tag/search', 'GET', 5, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (56, '查询后台用户列表', '/admin/user', 'GET', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (57, '获取用户区域分布', '/admin/user/area', 'GET', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (58, '查看当前用户菜单', '/admin/user/menu', 'GET', 8, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (59, '查看在线用户', '/admin/user/online', 'GET', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (60, '下线用户', '/admin/user/online/*', 'DELETE', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (61, '修改管理员密码', '/admin/user/password', 'PUT', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (62, '查询用户角色选项', '/admin/user/role', 'GET', 9, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (63, '修改用户角色', '/admin/user/role', 'PUT', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (64, '修改用户禁用状态', '/admin/user/*', 'PUT', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (65, '获取网站配置', '/admin/website/config', 'GET', 2, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (66, '更新网站配置', '/admin/website/config', 'PUT', 2, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (67, '查看文章归档', '/article/archive', 'GET', 3, 1, '2021-10-20 16:25:35', '2021-10-20 17:27:45');
INSERT INTO `hua_resource` VALUES (68, '点赞文章', '/article/like/*', 'POST', 3, 0, '2021-10-20 16:25:35', '2021-10-20 17:27:37');
INSERT INTO `hua_resource` VALUES (69, '根据条件查询文章', '/article/queryParam', 'GET', 3, 1, '2021-10-20 16:25:35', '2021-10-20 17:27:31');
INSERT INTO `hua_resource` VALUES (70, '搜索文章', '/article/search', 'GET', 3, 1, '2021-10-20 16:25:35', '2021-10-20 17:27:26');
INSERT INTO `hua_resource` VALUES (71, '根据id查看文章', '/article/*', 'GET', 3, 1, '2021-10-20 16:25:35', '2021-10-20 17:27:25');
INSERT INTO `hua_resource` VALUES (72, '查看分类列表', '/category', 'GET', 1, 1, '2021-10-20 16:25:35', '2021-10-20 16:41:49');
INSERT INTO `hua_resource` VALUES (73, '评论点赞', '/comment/like/*', 'POST', 10, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (74, '查询评论下的回复', '/comment/listReply/*', 'GET', 10, 1, '2021-10-20 16:25:35', '2021-10-20 17:29:44');
INSERT INTO `hua_resource` VALUES (75, '添加评论', '/comment/saveComment', 'POST', 10, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (76, '查询评论', '/comment/*', 'GET', 10, 1, '2021-10-20 16:25:35', '2021-10-20 17:29:45');
INSERT INTO `hua_resource` VALUES (77, '查看首页文章', '/home', 'GET', 3, 1, '2021-10-20 16:25:35', '2021-10-20 17:27:21');
INSERT INTO `hua_resource` VALUES (78, '查看留言列表', '/message', 'GET', 7, 1, '2021-10-20 16:25:35', '2021-10-20 17:29:15');
INSERT INTO `hua_resource` VALUES (79, '添加留言', '/message', 'POST', 7, 1, '2021-10-20 16:25:35', '2021-10-20 17:29:16');
INSERT INTO `hua_resource` VALUES (80, '上传访客信息', '/report', 'POST', 2, 1, '2021-10-20 16:25:35', '2021-10-20 17:26:44');
INSERT INTO `hua_resource` VALUES (81, '查询标签列表', '/tag', 'GET', 5, 1, '2021-10-20 16:25:35', '2021-10-20 17:28:14');
INSERT INTO `hua_resource` VALUES (82, '更新用户头像', '/user/avatar', 'POST', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (83, '发送邮箱验证码', '/user/code', 'GET', 6, 1, '2021-10-20 16:25:35', '2021-10-20 17:28:41');
INSERT INTO `hua_resource` VALUES (84, '修改密码', '/user/forget', 'PUT', 6, 1, '2021-10-20 16:25:35', '2021-10-20 17:28:35');
INSERT INTO `hua_resource` VALUES (85, '用户注册', '/user/register', 'POST', 6, 1, '2021-10-20 16:25:35', '2021-10-20 17:28:33');
INSERT INTO `hua_resource` VALUES (86, '绑定用户邮箱', '/user/updateEmail', 'PUT', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');
INSERT INTO `hua_resource` VALUES (87, '更新用户信息', '/user/updateUserInfo', 'PUT', 6, 0, '2021-10-20 16:25:35', '2021-10-20 16:25:35');

-- ----------------------------
-- Table structure for hua_role
-- ----------------------------
DROP TABLE IF EXISTS `hua_role`;
CREATE TABLE `hua_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `role_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `is_disable` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁用  0否 1是',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '发表时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_role
-- ----------------------------
INSERT INTO `hua_role` VALUES (1, '管理员', 'admin', 0, '2021-10-10 14:12:41', '2021-10-20 16:55:42');
INSERT INTO `hua_role` VALUES (2, '用户', 'user', 0, '2021-10-10 14:12:41', '2021-10-20 17:20:50');
INSERT INTO `hua_role` VALUES (3, '测试', 'test', 0, '2021-10-10 14:12:41', '2021-10-20 17:25:03');

-- ----------------------------
-- Table structure for hua_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `hua_role_menu`;
CREATE TABLE `hua_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '发表时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 168 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_role_menu
-- ----------------------------
INSERT INTO `hua_role_menu` VALUES (115, 3, 1, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (116, 3, 2, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (117, 3, 6, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (118, 3, 7, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (119, 3, 8, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (120, 3, 9, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (121, 3, 10, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (122, 3, 3, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (123, 3, 11, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (124, 3, 12, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (125, 3, 22, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (126, 3, 13, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (127, 3, 21, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (128, 3, 28, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (129, 3, 14, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (130, 3, 15, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (131, 3, 16, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (132, 3, 4, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (133, 3, 29, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (134, 3, 26, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (135, 3, 18, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (136, 3, 24, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (137, 3, 25, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (138, 3, 27, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (139, 3, 19, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (140, 3, 20, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (141, 3, 5, '2021-10-20 16:55:38', '2021-10-20 16:55:38');
INSERT INTO `hua_role_menu` VALUES (142, 1, 1, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (143, 1, 2, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (144, 1, 6, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (145, 1, 7, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (146, 1, 8, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (147, 1, 9, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (148, 1, 10, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (149, 1, 3, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (150, 1, 11, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (151, 1, 12, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (152, 1, 22, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (153, 1, 13, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (154, 1, 21, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (155, 1, 28, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (156, 1, 14, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (157, 1, 15, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (158, 1, 16, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (159, 1, 4, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (160, 1, 29, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (161, 1, 26, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (162, 1, 18, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (163, 1, 24, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (164, 1, 25, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (165, 1, 27, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (166, 1, 19, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (167, 1, 20, '2021-10-20 16:55:42', '2021-10-20 16:55:42');
INSERT INTO `hua_role_menu` VALUES (168, 1, 5, '2021-10-20 16:55:42', '2021-10-20 16:55:42');

-- ----------------------------
-- Table structure for hua_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `hua_role_resource`;
CREATE TABLE `hua_role_resource`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `resource_id` int NULL DEFAULT NULL COMMENT '权限id',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '发表时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 285 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_role_resource
-- ----------------------------
INSERT INTO `hua_role_resource` VALUES (128, 1, 1, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (129, 1, 2, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (130, 1, 3, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (131, 1, 4, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (132, 1, 5, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (133, 1, 6, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (134, 1, 7, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (135, 1, 8, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (136, 1, 9, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (137, 1, 10, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (138, 1, 11, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (139, 1, 12, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (140, 1, 13, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (141, 1, 14, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (142, 1, 15, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (143, 1, 16, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (144, 1, 17, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (145, 1, 18, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (146, 1, 19, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (147, 1, 20, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (148, 1, 21, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (149, 1, 22, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (150, 1, 23, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (151, 1, 24, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (152, 1, 25, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (153, 1, 26, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (154, 1, 27, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (155, 1, 28, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (156, 1, 29, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (157, 1, 30, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (158, 1, 31, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (159, 1, 32, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (160, 1, 33, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (161, 1, 34, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (162, 1, 35, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (163, 1, 36, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (164, 1, 37, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (165, 1, 38, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (166, 1, 39, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (167, 1, 40, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (168, 1, 41, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (169, 1, 42, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (170, 1, 43, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (171, 1, 44, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (172, 1, 45, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (173, 1, 46, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (174, 1, 47, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (175, 1, 48, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (176, 1, 49, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (177, 1, 50, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (178, 1, 51, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (179, 1, 52, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (180, 1, 53, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (181, 1, 54, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (182, 1, 55, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (183, 1, 56, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (184, 1, 57, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (185, 1, 58, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (186, 1, 59, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (187, 1, 60, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (188, 1, 61, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (189, 1, 62, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (190, 1, 63, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (191, 1, 64, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (192, 1, 65, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (193, 1, 66, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (194, 1, 67, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (195, 1, 68, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (196, 1, 69, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (197, 1, 70, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (198, 1, 71, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (199, 1, 72, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (200, 1, 73, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (201, 1, 74, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (202, 1, 75, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (203, 1, 76, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (204, 1, 77, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (205, 1, 78, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (206, 1, 79, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (207, 1, 80, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (208, 1, 81, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (209, 1, 82, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (210, 1, 83, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (211, 1, 84, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (212, 1, 85, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (213, 1, 86, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (214, 1, 87, '2021-10-20 16:26:17', '2021-10-20 16:26:17');
INSERT INTO `hua_role_resource` VALUES (255, 2, 68, '2021-10-20 17:20:50', '2021-10-20 17:20:50');
INSERT INTO `hua_role_resource` VALUES (256, 2, 82, '2021-10-20 17:20:50', '2021-10-20 17:20:50');
INSERT INTO `hua_role_resource` VALUES (257, 2, 86, '2021-10-20 17:20:50', '2021-10-20 17:20:50');
INSERT INTO `hua_role_resource` VALUES (258, 2, 87, '2021-10-20 17:20:50', '2021-10-20 17:20:50');
INSERT INTO `hua_role_resource` VALUES (259, 2, 73, '2021-10-20 17:20:50', '2021-10-20 17:20:50');
INSERT INTO `hua_role_resource` VALUES (260, 2, 75, '2021-10-20 17:20:50', '2021-10-20 17:20:50');
INSERT INTO `hua_role_resource` VALUES (261, 3, 24, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (262, 3, 27, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (263, 3, 15, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (264, 3, 65, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (265, 3, 17, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (266, 3, 23, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (267, 3, 68, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (268, 3, 32, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (269, 3, 52, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (270, 3, 55, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (271, 3, 56, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (272, 3, 57, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (273, 3, 59, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (274, 3, 37, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (275, 3, 34, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (276, 3, 50, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (277, 3, 58, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (278, 3, 47, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (279, 3, 62, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (280, 3, 28, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (281, 3, 73, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (282, 3, 75, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (283, 3, 43, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (284, 3, 51, '2021-10-20 17:25:03', '2021-10-20 17:25:03');
INSERT INTO `hua_role_resource` VALUES (285, 3, 40, '2021-10-20 17:25:03', '2021-10-20 17:25:03');

-- ----------------------------
-- Table structure for hua_tag
-- ----------------------------
DROP TABLE IF EXISTS `hua_tag`;
CREATE TABLE `hua_tag`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_tag
-- ----------------------------
INSERT INTO `hua_tag` VALUES (1, '测试标签', '2021-10-11 16:49:06', '2021-10-11 16:49:06');

-- ----------------------------
-- Table structure for hua_unique_view
-- ----------------------------
DROP TABLE IF EXISTS `hua_unique_view`;
CREATE TABLE `hua_unique_view`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `view_count` int NOT NULL COMMENT '访问量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_unique_view
-- ----------------------------
INSERT INTO `hua_unique_view` VALUES (1, 2, '2021-10-23 0:00:00', '2021-10-23 0:00:00');
INSERT INTO `hua_unique_view` VALUES (2, 6, '2021-10-24 0:00:00', '2021-10-24 0:00:00');
INSERT INTO `hua_unique_view` VALUES (3, 3, '2021-10-25 0:00:00', '2021-10-25 0:00:00');

-- ----------------------------
-- Table structure for hua_user
-- ----------------------------
DROP TABLE IF EXISTS `hua_user`;
CREATE TABLE `hua_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱，用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户简介',
  `web_site` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人网站',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户登录ip',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `is_disable` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁用 0正常 1禁用',
  `last_login_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '上次登录时间',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_user
-- ----------------------------
INSERT INTO `hua_user` VALUES (1, 'admin@qq.com', '$2a$10$TxzLJwN4fcf0ZUO7ma5q9.GVAB.DXY62QtOqSb2efabaogwH8DZGS', 'http://r19jqirt4.hn-bkt.clouddn.com/avatar/3a2d0201-dbe0-446c-953d-8f3d6e4fecd3.jpg', '管理员', '管理员账号', NULL, '127.0.0.1', '', 0, '2021-10-21 17:59:55', '2021-10-11 15:01:14', '2021-10-21 17:59:55');
INSERT INTO `hua_user` VALUES (2, 'test@qq.com', '$2a$10$TxzLJwN4fcf0ZUO7ma5q9.GVAB.DXY62QtOqSb2efabaogwH8DZGS', 'http://r19jqirt4.hn-bkt.clouddn.com/avatar/3a2d0201-dbe0-446c-953d-8f3d6e4fecd3.jpg', '测试账号', '仅用作测试', NULL, '127.0.0.1', '', 0, '2021-10-21 18:01:51', '2021-10-11 15:01:14', '2021-10-21 18:01:51');

-- ----------------------------
-- Table structure for hua_user_role
-- ----------------------------
DROP TABLE IF EXISTS `hua_user_role`;
CREATE TABLE `hua_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_user_role
-- ----------------------------
INSERT INTO `hua_user_role` VALUES (1, 1, 1, '2021-10-11 15:01:13', '2021-10-14 15:02:22');
INSERT INTO `hua_user_role` VALUES (2, 2, 3, '2021-10-15 17:54:24', '2021-10-15 17:54:24');

-- ----------------------------
-- Table structure for hua_website_config
-- ----------------------------
DROP TABLE IF EXISTS `hua_website_config`;
CREATE TABLE `hua_website_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `config` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置信息',
  `gmt_create` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hua_website_config
-- ----------------------------
INSERT INTO `hua_website_config` VALUES (1, '{\"alipayQRCode\":\"http://r19jqirt4.hn-bkt.clouddn.com/config/d363fc6c-9b08-45b0-841d-abd4acb7c937.png\",\"gitee\":\"https://gitee.com/lhMorri\",\"github\":\"https://github.com/LycorisradiataH\",\"isCommentAudit\":0,\"isEmailNotice\":1,\"isMessageAudit\":0,\"isReward\":1,\"socialUrlList\":[\"github\",\"gitee\"],\"touristAvatar\":\"http://r19jqirt4.hn-bkt.clouddn.com/config/911d429e-0312-4802-ad42-bb88a00ac587.png\",\"weChatQRCode\":\"http://r19jqirt4.hn-bkt.clouddn.com/config/8b4917c2-9bf7-4638-be1f-fe153ca75ddf.png\",\"websiteAuthor\":\"Lycorisradiata\",\"websiteAvatar\":\"http://r19jqirt4.hn-bkt.clouddn.com/config/c524477c-7da4-43db-8c54-b4cd84759c5f.jpg\",\"websiteCreateTime\":\"2021-10-01\",\"websiteIntro\":\"秋天的风也能遇见春天的花\",\"websiteName\":\"Hua的个人博客\",\"websiteNotice\":\"网站刚刚上线，有bug很正常，多多见谅\",\"websiteRecordNo\":\"备案号\"}', '2021-10-19 20:04:13', '2021-10-21 17:55:55');

SET FOREIGN_KEY_CHECKS = 1;
