/*
 Navicat Premium Data Transfer

 Source Server         : mysql8
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : db_fanyang_lake_animal

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 01/12/2025 12:06:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '轮播图表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (6, '天光一色', '/file/files/650454dafcba4458b63bcb42e0d54be9.png', '2025-08-30 08:49:13', '2025-09-17 23:00:25');
INSERT INTO `banner` VALUES (7, '鹤归', '/file/files/29a0c908b1fc4a00abe1245a064e6cfe.png', '2025-08-30 08:49:21', '2025-09-17 23:00:13');
INSERT INTO `banner` VALUES (8, '晚霞', '/file/files/f1b427278c24420fa55e79ba568b77ee.png', '2025-08-30 08:49:34', '2025-09-17 22:59:55');

-- ----------------------------
-- Table structure for animal
-- ----------------------------
DROP TABLE IF EXISTS `animal`;
CREATE TABLE `animal`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_id` int(11) NOT NULL COMMENT '物种ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '动物名称',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '简介',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '描述',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `season` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最佳观赏季节',
  `view_count` int(11) NULL DEFAULT NULL COMMENT '浏览量',
  `tags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '标签json数组',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '动物信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of animal
-- ----------------------------
INSERT INTO `animal` VALUES (1, 14, '白鹤', '国家一级保护动物，鄱阳湖的明星动物', '<p>白鹤是大型涉禽，全身洁白，飞羽黑色。每年冬季有超过4000只白鹤在鄱阳湖越冬，占全球白鹤总数的98%以上。白鹤主要以植物根茎为食，喜欢在浅水区域觅食。</p>', '/file/files/animal1.jpg', '10月-次年3月', 1250, '[\"国家一级保护\", \"濒危物种\", \"鄱阳湖明星\"]', '2025-09-17 22:49:35', '2025-09-20 15:39:38');
INSERT INTO `animal` VALUES (2, 14, '灰鹤', '优雅的大型涉禽，群体生活', '<p>灰鹤体型较大，全身灰色，头顶有红色肉冠。性格机警，常成群活动。在鄱阳湖主要以植物种子、根茎和小鱼虾为食。每年约有6000-8000只灰鹤在此越冬。</p>', '/file/files/animal2.jpg', '11月-次年2月', 890, '[\"大型涉禽\", \"群居\", \"机警\"]', '2025-09-17 22:49:35', '2025-09-20 15:39:33');
INSERT INTO `animal` VALUES (3, 14, '白头鹤', '头部白色的美丽鹤类', '<p>白头鹤是国家一级保护动物，头部和颈部为白色，身体灰黑色。主要栖息在湿地和草原，以植物根茎、种子为主食。在鄱阳湖的数量相对较少，约300-500只。</p>', '/file/files/animal3.jpg', '10月-次年3月', 567, '[\"国家一级保护\", \"稀少\", \"美丽\"]', '2025-09-17 22:49:35', '2025-09-20 15:39:29');
INSERT INTO `animal` VALUES (4, 14, '丹顶鹤', '仙鹤之称的优雅动物', '<p>丹顶鹤头顶有鲜红色肉冠，全身白色，翅膀边缘黑色。被誉为长寿和吉祥的象征。在鄱阳湖数量不多，主要在保护区核心区域活动。</p>', '/file/files/animal4.jpg', '11月-次年2月', 723, '[\"吉祥象征\", \"优雅\", \"珍贵\"]', '2025-09-17 22:49:35', '2025-09-20 15:39:23');
INSERT INTO `animal` VALUES (5, 15, '小天鹅', '优美的白色水鸟', '<p>小天鹅全身洁白，颈部较长，嘴部黄黑相间。每年约有6万只小天鹅在鄱阳湖越冬，是数量最多的天鹅种类。主要以水生植物为食。</p>', '/file/files/animal5.jpg', '10月-次年3月', 1456, '[\"数量众多\", \"洁白美丽\", \"水生植物\"]', '2025-09-17 22:49:35', '2025-09-20 15:39:20');
INSERT INTO `animal` VALUES (6, 15, '大天鹅', '体型最大的天鹅', '<p>大天鹅是天鹅中体型最大的，全身洁白，嘴部全黄。在鄱阳湖数量相对较少，约2000-3000只。飞行时发出响亮的叫声。</p>', '/file/files/animal6.jpg', '11月-次年2月', 834, '[\"体型最大\", \"响亮叫声\", \"珍贵\"]', '2025-09-17 22:49:35', '2025-09-20 15:39:15');
INSERT INTO `animal` VALUES (7, 19, '豆雁', '常见的雁类，群体觅食', '<p>豆雁体型中等，全身灰褐色，嘴部橙黄色有黑斑。常成大群活动，在农田和湿地觅食。是鄱阳湖最常见的雁类之一。</p>', '/file/files/animal7.jpg', '10月-次年3月', 945, '[\"常见\", \"群体活动\", \"农田觅食\"]', '2025-09-17 22:49:35', '2025-09-20 15:38:56');
INSERT INTO `animal` VALUES (8, 19, '白额雁', '额部有白斑的雁类', '<p>白额雁前额有明显的白色斑块，腹部有黑色横纹。喜欢在草地和农田觅食，以植物种子和嫩芽为主食。</p>', '/file/files/animal8.jpg', '11月-次年2月', 678, '[\"白色额斑\", \"农田\", \"植物种子\"]', '2025-09-17 22:49:35', '2025-09-20 15:38:48');
INSERT INTO `animal` VALUES (9, 15, '赤麻鸭', '颜色鲜艳的鸭类', '<p>赤麻鸭全身赤褐色，翅膀有白色斑块。既能游泳也能在陆地行走，食性较杂，包括植物和小动物。</p>', '/file/files/animal9.jpg', '全年', 1123, '[\"颜色鲜艳\", \"食性杂\", \"适应性强\"]', '2025-09-17 22:49:35', '2025-09-20 15:38:42');
INSERT INTO `animal` VALUES (10, 15, '绿头鸭', '最常见的野鸭', '绿头鸭雄鸟头部绿色有金属光泽，雌鸟全身褐色。是最常见的野鸭种类，适应性很强，在各种水域都能生存。', '/file/files/animal10.jpg', '全年', 1567, '[\"最常见\", \"适应性强\", \"金属光泽\"]', '2025-09-17 22:49:35', '2025-09-20 15:38:38');
INSERT INTO `animal` VALUES (11, 16, '大白鹭', '优雅的白色涉禽', '<p>大白鹭全身洁白，颈部细长，腿部黑色。主要以鱼类为食，常在浅水区域单独觅食。飞行时颈部呈S形弯曲。</p>', '/file/files/9565e362632a4f04bbdc04dc9566c706.png', '全年', 892, '[\"洁白优雅\", \"单独觅食\", \"S形颈部\"]', '2025-09-17 22:49:35', '2025-09-17 23:46:13');
INSERT INTO `animal` VALUES (12, 16, '中白鹭', '中等体型的白鹭', '<p>中白鹭体型介于大白鹭和小白鹭之间，全身白色，嘴部黄色。繁殖期嘴部会变成黑色，背部有丝状饰羽。</p>', '/file/files/66406ccb312a459abf69fe5910fdc841.png', '全年', 634, '[\"中等体型\", \"繁殖期变色\", \"丝状饰羽\"]', '2025-09-17 22:49:35', '2025-09-17 23:46:20');
INSERT INTO `animal` VALUES (13, 16, '苍鹭', '灰色的大型鹭类', '<p>苍鹭是最大的鹭类，全身灰色，头部有黑色冠羽。主要以鱼类为食，常站立在水边等待猎物。飞行缓慢而有力。</p>', '/file/files/2db242ced7524265916a905c5ffcfb3f.png', '全年', 756, '[\"最大鹭类\", \"耐心等待\", \"飞行有力\"]', '2025-09-17 22:49:35', '2025-09-17 23:46:27');
INSERT INTO `animal` VALUES (14, 16, '夜鹭', '夜间活动的鹭类', '<p>夜鹭背部黑色，腹部白色，眼睛红色。主要在夜间活动觅食，白天多在树上休息。叫声粗哑，像\"呱呱\"声。</p>', '/file/files/4ffa1a7893f64285b58eb26eec5bacdd.png', '全年', 445, '[\"夜间活动\", \"红色眼睛\", \"粗哑叫声\"]', '2025-09-17 22:49:35', '2025-09-17 23:46:33');
INSERT INTO `animal` VALUES (15, 18, '普通鸬鹚', '优秀的潜水捕鱼能手', '<p>普通鸬鹚全身黑色，颈部细长。是出色的潜水员，能在水下追捕鱼类。常成群在水面游泳或站在岸边晾晒翅膀。</p>', '/file/files/4824bf6905734227b646801b25127803.png', '全年', 1090, '[\"潜水高手\", \"成群活动\", \"晾晒翅膀\"]', '2025-09-17 22:49:35', '2025-09-18 22:00:09');
INSERT INTO `animal` VALUES (16, 17, '红嘴鸥', '美丽的湖面精灵', '<p>红嘴鸥嘴部和腿部鲜红色，翅膀灰白相间。飞行轻盈优美，常在湖面低飞捕食小鱼。是鄱阳湖冬季的常见动物。</p>', '/file/files/3d13ee454dbd45199b0e11a4ac8a71e1.png', '10月-次年3月', 924, '[\"红色嘴腿\", \"飞行优美\", \"湖面精灵\"]', '2025-09-17 22:49:35', '2025-09-27 16:24:11');
INSERT INTO `animal` VALUES (17, 20, '黑翅长脚鹬', '腿部修长的涉禽', '<p>黑翅长脚鹬腿部极长呈红色，翅膀黑色，身体白色。主要在浅水中觅食小鱼虾和水生昆虫。叫声尖锐。</p>', '/file/files/acf11167370143679b45d94d3182d53f.png', '4月-10月', 568, '[\"腿部修长\", \"黑白分明\", \"尖锐叫声\"]', '2025-09-17 22:49:35', '2025-09-27 16:24:08');
INSERT INTO `animal` VALUES (18, 15, '反嘴鹬', '嘴部向上弯曲的鹬类', '<p>反嘴鹬最显著的特征是嘴部向上弯曲，腿部蓝灰色。主要以小型甲壳动物和水生昆虫为食。在泥滩上觅食时会左右摆动头部。</p>', '/file/files/a1de34e594d8401997544f3405f0d4a7.png', '4月-9月', 391, '[\"嘴部弯曲\", \"蓝灰色腿\", \"摆动觅食\"]', '2025-09-17 22:49:35', '2025-09-27 16:31:22');
INSERT INTO `animal` VALUES (19, 21, '金眶鸻', '眼部有金色环的小型鸟', '<p>金眶鸻眼部周围有明显的金黄色环，腿部橙红色。主要在泥滩和浅水区觅食小型无脊椎动物。行动敏捷。</p>', '/file/files/1c69b08962124c72a95527da2f3bf499.png', '3月-10月', 239, '[\"金色眼环\", \"橙红色腿\", \"行动敏捷\"]', '2025-09-17 22:49:35', '2025-09-27 16:34:20');
INSERT INTO `animal` VALUES (20, 14, '白琵鹭', '嘴部扁平如琵琶', '白琵鹭全身洁白，嘴部扁平呈琵琶状。主要以小鱼虾为食，觅食时会在水中左右摆动嘴部过滤食物。', '/file/files/50aea7e8a4a248b2820623bfc4b48490.png', '全年', 471, '[\"琵琶状嘴\", \"过滤觅食\", \"洁白美丽\"]', '2025-09-17 22:49:35', '2025-09-27 16:32:05');

-- ----------------------------
-- Table structure for animal_location
-- ----------------------------
DROP TABLE IF EXISTS `animal_location`;
CREATE TABLE `animal_location`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地点名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '描述',
  `lat` decimal(10, 8) NOT NULL COMMENT '纬度',
  `lon` decimal(11, 8) NOT NULL COMMENT '经度',
  `best_season` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最佳观赏季节',
  `common_animals` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '常见动物',
  `facilities` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设施信息',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '观赏地点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of animal_location
-- ----------------------------
INSERT INTO `animal_location` VALUES (1, '鄱阳湖国家级自然保护区', '中国最大的淡水湖湿地，是候鸟的重要栖息地和越冬地', 29.24883700, 116.19377100, '10月-次年3月', '白鹤,灰鹤,白头鹤,东方白鹳', '观赏台,停车场,游客中心,望远镜租赁', '/file/files/4b487b4a7c314dfd852cd8f20f4931d3.png', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_location` VALUES (2, '都昌候鸟自然保护区', '鄱阳湖重要的候鸟栖息地，以白鹤闻名', 29.36543200, 116.27894500, '11月-次年2月', '白鹤,灰鹤,白枕鹤,丹顶鹤', '观赏屋,木栈道,停车场', '/file/files/3ed8cb7c7f2f4304bf081cdcd61bb8c3.png', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_location` VALUES (3, '永修吴城候鸟保护区', '鄱阳湖核心区域，候鸟种类丰富', 29.18765400, 115.87654300, '10月-次年4月', '白鹤,东方白鹳,黑鹳,大天鹅', '观赏塔,休息亭,科普展示厅', '/file/files/29d82f59b4f141b98f87cbd94c0860a1.png', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_location` VALUES (4, '南矶湿地国家级自然保护区', '重要的水鸟栖息地，湿地生态完整', 28.87654300, 116.34567800, '9月-次年3月', '小天鹅,豆雁,白额雁,赤麻鸭', '观赏平台,生态步道,游客服务中心', '/file/files/49f23cc9309c49c3bc956c72d83424b0.png', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_location` VALUES (5, '鄱阳湖湿地公园', '集观赏、科普、休闲于一体的湿地公园', 29.12345600, 116.45678900, '全年', '白鹭,苍鹭,夜鹭,池鹭', '观赏廊道,科普馆,餐厅,住宿', '/file/files/eafec69dd7e14652801767c8c73fc0fe.png', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_location` VALUES (6, '余干康山大堤', '观赏水鸟的绝佳地点，视野开阔', 28.76543200, 116.56789000, '10月-次年2月', '鸬鹚,鸭类,鸥类,鹬类', '观赏点,停车区', '/file/files/bfc655eb31634eb3aa3ba2ff51c7f501.png', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_location` VALUES (7, '湖口石钟山', '历史文化与自然观赏相结合的景点', 29.74378300, 116.22260500, '全年', '白鹭,夜鹭,翠鸟,普通翠鸟', '观景台,文化展示,停车场', '/file/files/d126e52db2004c2d942619adeb224247.png', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_location` VALUES (8, '进贤青岚湖', '鄱阳湖支流湿地，候鸟栖息地', 28.45678900, 116.12345600, '11月-次年3月', '白琵鹭,黑脸琵鹭,反嘴鹬,黑翅长脚鹬', '观赏小屋,步行道', '/file/files/8e55288d3d0648fd8842734fe1663797.png', '2025-09-17 22:49:35', '2025-09-17 22:49:35');

-- ----------------------------
-- Table structure for animal_type
-- ----------------------------
DROP TABLE IF EXISTS `animal_type`;
CREATE TABLE `animal_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物种名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '动物物种表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of animal_type
-- ----------------------------
INSERT INTO `animal_type` VALUES (14, '鹤类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (15, '鸭类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (16, '鹭类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (17, '鸥类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (18, '鸬鹚类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (19, '雁类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (20, '鹬类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (21, '鸻类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (22, '鸢类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (23, '鹰类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (24, '隼类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `animal_type` VALUES (25, '鸦类', '2025-09-17 22:49:35', '2025-09-17 22:49:35');

-- ----------------------------
-- Table structure for animal_weight
-- ----------------------------
DROP TABLE IF EXISTS `animal_weight`;
CREATE TABLE `animal_weight`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `animal_id` int(11) NULL DEFAULT NULL COMMENT '鸟数据ID',
  `behavior` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '行为（collect、like、view）',
  `score` int(11) NULL DEFAULT NULL COMMENT '分值（collect=3、like=2，view=1）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '动物权重表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of animal_weight
-- ----------------------------
INSERT INTO `animal_weight` VALUES (1, 4, 20, 'view', 1, '2025-09-27 16:26:48', '2025-09-27 16:26:48');
INSERT INTO `animal_weight` VALUES (2, 4, 20, 'collect', 3, '2025-09-27 16:27:04', '2025-09-27 16:27:04');
INSERT INTO `animal_weight` VALUES (3, 4, 20, 'like', 2, '2025-09-27 16:31:16', '2025-09-27 16:31:16');
INSERT INTO `animal_weight` VALUES (4, 4, 18, 'view', 1, '2025-09-27 16:31:22', '2025-09-27 16:31:22');
INSERT INTO `animal_weight` VALUES (5, 4, 18, 'collect', 3, '2025-09-27 16:31:24', '2025-09-27 16:31:24');
INSERT INTO `animal_weight` VALUES (6, 4, 18, 'like', 2, '2025-09-27 16:31:25', '2025-09-27 16:31:25');
INSERT INTO `animal_weight` VALUES (7, 4, 19, 'view', 1, '2025-09-27 16:31:28', '2025-09-27 16:31:28');
INSERT INTO `animal_weight` VALUES (8, 4, 19, 'collect', 3, '2025-09-27 16:31:31', '2025-09-27 16:31:31');
INSERT INTO `animal_weight` VALUES (9, 4, 19, 'like', 2, '2025-09-27 16:31:32', '2025-09-27 16:31:32');
INSERT INTO `animal_weight` VALUES (10, 6, 20, 'view', 1, '2025-09-27 16:32:05', '2025-09-27 16:32:05');
INSERT INTO `animal_weight` VALUES (11, 6, 20, 'like', 2, '2025-09-27 16:32:06', '2025-09-27 16:32:06');
INSERT INTO `animal_weight` VALUES (12, 6, 20, 'collect', 3, '2025-09-27 16:32:07', '2025-09-27 16:32:07');
INSERT INTO `animal_weight` VALUES (13, 6, 19, 'view', 1, '2025-09-27 16:34:04', '2025-09-27 16:34:04');
INSERT INTO `animal_weight` VALUES (14, 6, 19, 'collect', 3, '2025-09-27 16:34:06', '2025-09-27 16:34:06');
INSERT INTO `animal_weight` VALUES (15, 6, 19, 'like', 2, '2025-09-27 16:34:06', '2025-09-27 16:34:06');
INSERT INTO `animal_weight` VALUES (16, 7, 19, 'view', 1, '2025-09-27 16:34:20', '2025-09-27 16:34:20');

-- ----------------------------
-- Table structure for conservation_news
-- ----------------------------
DROP TABLE IF EXISTS `conservation_news`;
CREATE TABLE `conservation_news`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '简介',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型（新闻、政策、法规）',
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览数',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态（0-禁用，1-正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '生态保护新闻表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of conservation_news
-- ----------------------------
INSERT INTO `conservation_news` VALUES (1, '测试', '测试', '测试', '生态保护', '测试', '/file/files/da7fba6f26354a9cb40ed2675977fc6c.png', 29, 1, '2025-09-20 08:01:53', '2025-10-08 15:42:10');

-- ----------------------------
-- Table structure for db_collect
-- ----------------------------
DROP TABLE IF EXISTS `db_collect`;
CREATE TABLE `db_collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收藏类型（animal, location, post）',
  `target_id` int(11) NOT NULL COMMENT '目标ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_type_target`(`user_id` ASC, `type` ASC, `target_id` ASC) USING BTREE,
  INDEX `idx_type_target`(`type` ASC, `target_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_collect
-- ----------------------------
INSERT INTO `db_collect` VALUES (1, 1, 'animal', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (2, 1, 'animal', 5, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (3, 1, 'animal', 15, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (4, 2, 'animal', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (5, 2, 'animal', 16, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (6, 2, 'animal', 5, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (7, 4, 'animal', 11, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (8, 4, 'animal', 17, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (9, 4, 'animal', 3, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (10, 5, 'animal', 14, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (11, 5, 'animal', 19, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (12, 5, 'animal', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (13, 1, 'location', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (14, 1, 'location', 2, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (15, 2, 'location', 3, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (16, 2, 'location', 6, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (17, 4, 'location', 5, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (19, 5, 'location', 7, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (20, 5, 'location', 4, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (21, 1, 'post', 2, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (22, 1, 'post', 6, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (23, 2, 'post', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (24, 2, 'post', 8, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (25, 4, 'post', 3, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (26, 4, 'post', 9, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (27, 5, 'post', 4, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (28, 5, 'post', 10, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_collect` VALUES (32, 7, 'location', 8, '2025-09-20 00:33:49', '2025-09-20 00:33:49');
INSERT INTO `db_collect` VALUES (34, 4, 'travel', 10, '2025-09-20 13:56:16', '2025-09-20 13:56:16');
INSERT INTO `db_collect` VALUES (35, 4, 'travel', 2, '2025-09-20 13:56:37', '2025-09-20 13:56:37');
INSERT INTO `db_collect` VALUES (36, 4, 'travel', 3, '2025-09-20 14:04:43', '2025-09-20 14:04:43');
INSERT INTO `db_collect` VALUES (37, 24, 'travel', 1, '2025-09-20 14:05:22', '2025-09-20 14:05:22');
INSERT INTO `db_collect` VALUES (38, 24, 'travel', 10, '2025-09-20 14:07:15', '2025-09-20 14:07:15');
INSERT INTO `db_collect` VALUES (43, 4, '2', 10, '2025-09-21 11:04:27', '2025-09-21 11:04:27');
INSERT INTO `db_collect` VALUES (46, 4, 'animal', 16, '2025-09-27 16:24:13', '2025-09-27 16:24:13');
INSERT INTO `db_collect` VALUES (48, 4, 'animal', 18, '2025-09-27 16:31:24', '2025-09-27 16:31:24');
INSERT INTO `db_collect` VALUES (49, 4, 'animal', 19, '2025-09-27 16:31:31', '2025-09-27 16:31:31');
INSERT INTO `db_collect` VALUES (50, 6, 'animal', 20, '2025-09-27 16:32:07', '2025-09-27 16:32:07');
INSERT INTO `db_collect` VALUES (51, 6, 'animal', 19, '2025-09-27 16:34:05', '2025-09-27 16:34:05');
INSERT INTO `db_collect` VALUES (52, 7, 'travel', 10, '2025-09-27 16:37:40', '2025-09-27 16:37:40');
INSERT INTO `db_collect` VALUES (53, 4, 'location', 8, '2025-10-08 15:40:56', '2025-10-08 15:40:56');

-- ----------------------------
-- Table structure for db_like
-- ----------------------------
DROP TABLE IF EXISTS `db_like`;
CREATE TABLE `db_like`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收藏类型（animal, location, post, travel_guide）',
  `target_id` int(11) NOT NULL COMMENT '目标ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_like
-- ----------------------------
INSERT INTO `db_like` VALUES (1, 1, 'animal', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (2, 1, 'animal', 5, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (3, 1, 'animal', 11, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (4, 1, 'animal', 15, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (5, 2, 'animal', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (6, 2, 'animal', 2, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (7, 2, 'animal', 5, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (8, 2, 'animal', 16, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (9, 4, 'animal', 3, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (10, 4, 'animal', 7, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (11, 4, 'animal', 11, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (12, 4, 'animal', 17, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (13, 5, 'animal', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (14, 5, 'animal', 14, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (15, 5, 'animal', 18, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (16, 5, 'animal', 19, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (17, 1, 'location', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (18, 1, 'location', 2, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (19, 1, 'location', 5, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (20, 2, 'location', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (21, 2, 'location', 3, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (22, 2, 'location', 6, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (23, 4, 'location', 4, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (24, 4, 'location', 5, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (26, 5, 'location', 2, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (27, 5, 'location', 7, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (28, 5, 'location', 8, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (29, 1, 'post', 2, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (30, 1, 'post', 6, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (31, 1, 'post', 7, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (32, 2, 'post', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (33, 2, 'post', 3, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (34, 2, 'post', 8, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (35, 4, 'post', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (36, 4, 'post', 2, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (37, 4, 'post', 9, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (38, 5, 'post', 4, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (39, 5, 'post', 5, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (40, 5, 'post', 10, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `db_like` VALUES (45, 4, '2', 10, '2025-09-18 23:21:31', '2025-09-18 23:21:31');
INSERT INTO `db_like` VALUES (46, 7, 'location', 8, '2025-09-20 00:33:48', '2025-09-20 00:33:48');
INSERT INTO `db_like` VALUES (51, 4, 'travel', 2, '2025-09-20 13:56:36', '2025-09-20 13:56:36');
INSERT INTO `db_like` VALUES (52, 4, 'travel', 3, '2025-09-20 14:04:42', '2025-09-20 14:04:42');
INSERT INTO `db_like` VALUES (53, 24, 'travel', 1, '2025-09-20 14:05:21', '2025-09-20 14:05:21');
INSERT INTO `db_like` VALUES (54, 24, 'travel', 10, '2025-09-20 14:07:14', '2025-09-20 14:07:14');
INSERT INTO `db_like` VALUES (55, 25, 'travel', 1, '2025-09-20 14:07:31', '2025-09-20 14:07:31');
INSERT INTO `db_like` VALUES (56, 4, '2', 5, '2025-09-20 15:58:23', '2025-09-20 15:58:23');
INSERT INTO `db_like` VALUES (61, 4, 'animal', 16, '2025-09-27 16:24:12', '2025-09-27 16:24:12');
INSERT INTO `db_like` VALUES (64, 4, 'animal', 20, '2025-09-27 16:31:16', '2025-09-27 16:31:16');
INSERT INTO `db_like` VALUES (65, 4, 'animal', 18, '2025-09-27 16:31:25', '2025-09-27 16:31:25');
INSERT INTO `db_like` VALUES (66, 4, 'animal', 19, '2025-09-27 16:31:32', '2025-09-27 16:31:32');
INSERT INTO `db_like` VALUES (67, 6, 'animal', 20, '2025-09-27 16:32:06', '2025-09-27 16:32:06');
INSERT INTO `db_like` VALUES (68, 6, 'animal', 19, '2025-09-27 16:34:06', '2025-09-27 16:34:06');
INSERT INTO `db_like` VALUES (69, 7, 'travel', 10, '2025-09-27 16:37:39', '2025-09-27 16:37:39');
INSERT INTO `db_like` VALUES (70, 4, 'location', 8, '2025-10-08 15:40:55', '2025-10-08 15:40:55');

-- ----------------------------
-- Table structure for forum_post
-- ----------------------------
DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE `forum_post`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '简介',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `animal_id` int(11) NULL DEFAULT NULL COMMENT '关联动物ID',
  `location_id` int(11) NULL DEFAULT NULL COMMENT '观赏地点ID',
  `observation_time` datetime NULL DEFAULT NULL COMMENT '观赏时间',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_animal_id`(`animal_id` ASC) USING BTREE,
  INDEX `idx_location_id`(`location_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '论坛帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_post
-- ----------------------------
INSERT INTO `forum_post` VALUES (1, 1, '鄱阳湖观赏初体验：与白鹤的美丽邂逅', '第一次来到鄱阳湖观赏，就被成群的白鹤震撼了', '今天是我第一次来到鄱阳湖观赏，选择了都昌候鸟自然保护区。早上6点就到达了观赏点，雾气还没有完全散去，湖面朦朦胧胧的。随着太阳升起，雾气渐渐散去，眼前的景象让我震撼不已：成百上千只白鹤在湖面上觅食、飞翔。它们洁白的羽毛在阳光下闪闪发光，优雅的姿态让人陶醉。特别是看到一群白鹤同时起飞的场面，那种壮观真的无法用言语形容。这次观赏之旅让我深深爱上了这些美丽的生灵，也更加意识到保护它们栖息地的重要性。', 1, 2, '2024-12-15 06:30:00', '/file/files/post1.jpg', 156, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `forum_post` VALUES (2, 2, '冬日鄱阳湖：小天鹅的天堂', '数万只小天鹅聚集在鄱阳湖，场面壮观', '12月的鄱阳湖是小天鹅的天堂。今天在永修吴城候鸟保护区观察到了大约2万只小天鹅，它们有的在水中游泳觅食，有的在岸边休息理羽。最让我印象深刻的是它们的叫声，清脆悦耳，此起彼伏。当一群小天鹅同时起飞时，翅膀拍打的声音如雷鸣般响亮。它们飞行时排成人字形或一字形，非常有序。通过望远镜观察，可以清楚地看到它们优美的颈部曲线和洁白的羽毛。这些来自西伯利亚的客人每年都会准时到达鄱阳湖过冬，它们的到来为这片湿地增添了无限生机。', 5, 3, '2024-12-10 07:15:00', '/file/files/post2.jpg', 203, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `forum_post` VALUES (3, 4, '春季观赏记：鹭类的繁殖季节', '春天的鄱阳湖，各种鹭类开始繁殖', '春天来了，鄱阳湖的鹭类开始进入繁殖季节。在鄱阳湖湿地公园观察到了大白鹭、中白鹭、苍鹭等多种鹭类。它们开始换上繁殖羽，背部长出美丽的丝状饰羽，颜色也变得更加鲜艳。最有趣的是观察它们的求偶行为，雄鸟会展开翅膀，伸长脖子，发出特殊的叫声来吸引雌鸟。有些鹭类已经开始筑巢，它们选择在高大的树上建造巢穴，用树枝和水草编织而成。看到一对苍鹭正在合作筑巢，雄鸟负责收集材料，雌鸟负责编织，配合得非常默契。', 11, 5, '2024-03-20 08:00:00', '/file/files/post3.jpg', 89, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `forum_post` VALUES (4, 5, '夜鹭的夜生活：夜间观赏体验', '夜晚的鄱阳湖别有一番风味，夜鹭开始活跃', '为了观察夜鹭的生活习性，我选择了夜间观赏。傍晚时分来到湖口石钟山附近，白天还在树上休息的夜鹭开始活跃起来。它们发出粗哑的\"呱呱\"声，从栖息的树上飞到水边觅食。夜鹭的眼睛在夜晚显得特别明亮，红色的眼睛在手电筒光下闪闪发光。它们觅食时非常专注，会长时间站在水边一动不动，等待鱼儿游过。当发现目标时，会迅速伸出脖子，准确地捕获猎物。夜间的鄱阳湖很安静，只有夜鹭的叫声和水波声，这种体验非常特别。', 14, 7, '2024-11-05 19:30:00', '/file/files/post4.jpg', 67, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `forum_post` VALUES (5, 1, '鸬鹚的捕鱼技巧：水下的猎手', '观察鸬鹚捕鱼，它们的技巧令人惊叹', '今天专门观察了鸬鹚的捕鱼行为，它们真的是水下的猎手。鸬鹚潜水前会先在水面游泳，寻找鱼群。一旦发现目标，它们会迅速潜入水中，在水下追逐鱼类。通过水下摄像设备，我看到鸬鹚在水中的游泳速度非常快，翅膀在水中像桨一样划动。它们能在水下停留30-60秒，捕获的鱼类通常会被整个吞下。捕鱼成功后，鸬鹚会游到岸边或站在木桩上，张开翅膀晾干羽毛。这是因为鸬鹚的羽毛不像其他水鸟那样防水，需要定期晾干。', 15, 6, '2024-10-28 09:45:00', '/file/files/post5.jpg', 135, '2025-09-17 22:49:35', '2025-09-20 15:58:01');
INSERT INTO `forum_post` VALUES (6, 2, '红嘴鸥的优雅飞行：湖面上的舞者', '红嘴鸥在湖面上的飞行姿态优美如舞', '红嘴鸥是鄱阳湖冬季最优雅的动物之一。它们的飞行技巧令人赞叹，能够在湖面上进行各种高难度的飞行动作。今天观察到一群红嘴鸥在湖面上觅食，它们时而低飞掠过水面，时而高飞盘旋。最精彩的是它们的俯冲捕鱼，从高空俯冲下来，准确地抓住水中的小鱼，然后优雅地飞起。红嘴鸥的嘴部和腿部是鲜艳的红色，在阳光下特别醒目。它们的叫声清脆悦耳，为宁静的湖面增添了生机。看着它们在湖面上自由飞翔，真的像是在观看一场精彩的空中芭蕾。', 16, 1, '2024-12-08 10:20:00', '/file/files/post6.jpg', 178, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `forum_post` VALUES (7, 4, '豆雁大军：农田里的访客', '成千上万的豆雁在农田里觅食', '冬季的鄱阳湖周边农田成了豆雁的天堂。今天在余干康山大堤附近的农田里，看到了成千上万只豆雁在觅食。它们主要以农田里遗留的稻谷和其他植物种子为食。豆雁的警觉性很高，一有风吹草动就会集体起飞。当几千只豆雁同时起飞时，那种场面非常壮观，翅膀拍打的声音如雷鸣般响亮。它们飞行时会排成V字形，这样可以减少飞行阻力，节省体力。豆雁的叫声很有特色，\"嘎嘎\"声此起彼伏，在很远的地方都能听到。', 7, 6, '2024-11-20 14:30:00', '/file/files/post7.jpg', 92, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `forum_post` VALUES (8, 5, '黑翅长脚鹬：泥滩上的芭蕾舞者', '黑翅长脚鹬在泥滩上觅食的优美姿态', '在进贤青岚湖的泥滩上，观察到了黑翅长脚鹬的觅食行为。它们有着极其修长的红色腿部，在泥滩上行走时就像芭蕾舞者一样优雅。黑翅长脚鹬主要以小鱼虾和水生昆虫为食，它们会在浅水中慢慢行走，用细长的嘴部在水中搜寻食物。当发现猎物时，会迅速伸出嘴部捕获。它们的叫声很尖锐，听起来像\"叽叽\"声。黑翅长脚鹬的黑白分明的羽毛在阳光下形成强烈的对比，非常美丽。看着它们在泥滩上优雅地觅食，真的是一种视觉享受。', 17, 8, '2024-05-15 11:00:00', '/file/files/post8.jpg', 56, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `forum_post` VALUES (9, 1, '反嘴鹬的独特觅食方式', '反嘴鹬用弯曲的嘴部在水中觅食', '反嘴鹬是一种非常有趣的动物，它们最显著的特征就是向上弯曲的嘴部。今天在南矶湿地观察到了它们独特的觅食方式。反嘴鹬会在浅水中左右摆动头部，用弯曲的嘴部在水中搜寻小型甲壳动物和水生昆虫。这种觅食方式非常有效，它们能够在泥沙中找到其他动物找不到的食物。反嘴鹬的腿部是蓝灰色的，在水中行走时非常稳定。它们通常成小群活动，觅食时会保持一定的距离，避免相互干扰。看着它们摆动头部觅食的样子，真的很有趣。', 18, 4, '2024-06-10 09:15:00', '/file/files/post9.jpg', 74, '2025-09-17 22:49:35', '2025-09-20 16:07:56');
INSERT INTO `forum_post` VALUES (10, 2, '金眶鸻：泥滩上的小精灵', '金眶鸻在泥滩上快速奔跑觅食', '<p>金眶鸻是一种小型的鸻类，它们最显著的特征是眼部周围的金黄色环。今天在鄱阳湖湿地公园的泥滩上观察到了这些小精灵。金眶鸻的行动非常敏捷，它们会在泥滩上快速奔跑，突然停下来啄食小型无脊椎动物，然后又继续奔跑。它们的腿部是橙红色的，在泥滩上非常醒目。金眶鸻的警觉性很高，一有危险就会迅速飞走。它们的飞行速度很快，飞行时翅膀拍打频率很高。虽然体型小，但它们的生命力很顽强，能够适应各种环境。</p>', 19, 5, '2024-07-22 16:45:00', '/file/files/fd6bf574d0594c189c5e4e15b6746e97.png', 71, '2025-09-17 22:49:35', '2025-09-21 11:04:25');
INSERT INTO `forum_post` VALUES (13, 4, '我的帖子222', '我的帖子我的帖子', '<p><br></p>', 1, 2, '2025-09-21 11:05:54', '', 0, '2025-09-21 11:05:57', '2025-09-21 11:07:26');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (3, '国庆节快乐', '欢度国庆，喜迎中秋，双节欢度', 2, '2025-10-05 08:22:11', '2025-10-05 08:22:11');
INSERT INTO `notice` VALUES (2, '保护生态', '保护生态，人人有责', 2, '2025-09-20 15:37:58', '2025-09-20 15:37:58');

-- ----------------------------
-- Table structure for posts_comment
-- ----------------------------
DROP TABLE IF EXISTS `posts_comment`;
CREATE TABLE `posts_comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '评论用户ID',
  `posts_id` int(11) NOT NULL COMMENT '帖子ID',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '评论内容',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父级评论',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 87 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '帖子评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of posts_comment
-- ----------------------------
INSERT INTO `posts_comment` VALUES (58, 2, 1, '太棒了！我也想去看白鹤，请问最佳观赏时间是什么时候？', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (59, 1, 1, '一般早上6-8点和下午4-6点是最佳时间，动物比较活跃', 1, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (60, 4, 1, '白鹤真的很美，去年我也在都昌看到过，震撼！', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (61, 5, 2, '小天鹅的数量真的很壮观，感谢分享！', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (62, 1, 2, '是的，每年冬天都有数万只小天鹅来鄱阳湖过冬', 4, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (63, 4, 3, '春季观赏确实很有意思，繁殖期的动物更加美丽', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (64, 2, 3, '求偶行为观察起来很有趣，下次我也要去看看', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (65, 1, 4, '夜间观赏是个不错的体验，很少有人尝试', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (66, 5, 4, '夜鹭的红眼睛确实很特别，我也见过', 8, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (67, 2, 5, '鸬鹚的潜水技巧真的很厉害，是天生的捕鱼高手', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (68, 4, 6, '红嘴鸥的飞行确实很优雅，像在跳舞一样', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (69, 1, 6, '它们的俯冲捕鱼动作特别精彩', 11, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (70, 5, 7, '豆雁大军的场面一定很壮观，我也想去看看', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (71, 2, 8, '黑翅长脚鹬的腿真的很长，走路姿态很优美', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (72, 1, 9, '反嘴鹬的嘴部构造很神奇，适应了特殊的觅食方式', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (73, 4, 10, '金眶鸻虽然小，但很可爱，行动很敏捷', NULL, '2025-09-17 22:49:35', '2025-09-17 22:49:35');
INSERT INTO `posts_comment` VALUES (74, 4, 10, '你好', NULL, '2025-09-18 23:50:42', NULL);
INSERT INTO `posts_comment` VALUES (77, 4, 10, '谢谢', 74, '2025-09-18 23:54:28', NULL);
INSERT INTO `posts_comment` VALUES (76, 4, 10, '谢谢', 75, '2025-09-18 23:54:14', NULL);
INSERT INTO `posts_comment` VALUES (78, 4, 10, '222', 77, '2025-09-18 23:54:34', NULL);
INSERT INTO `posts_comment` VALUES (79, 4, 10, '很好看', 74, '2025-09-19 00:02:07', NULL);
INSERT INTO `posts_comment` VALUES (80, 7, 10, '你好呀', 73, '2025-09-20 00:26:57', NULL);
INSERT INTO `posts_comment` VALUES (82, 4, 5, '是啊，太厉害', 67, '2025-09-20 15:58:18', NULL);
INSERT INTO `posts_comment` VALUES (83, 4, 10, '你好', NULL, '2025-09-21 11:04:34', NULL);
INSERT INTO `posts_comment` VALUES (86, 4, 15, '你好', 85, '2025-10-08 15:40:22', NULL);

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID（可匿名）',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地点',
  `occurrence_time` datetime NULL DEFAULT NULL COMMENT '发生时间',
  `img_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片证据',
  `contact_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系方式',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态（0-待处理，1-处理中，2-已处理）',
  `process_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '处理结果',
  `is_anonymous` tinyint(1) NULL DEFAULT 0 COMMENT '是否匿名',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '举报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report
-- ----------------------------
INSERT INTO `report` VALUES (1, 4, '测试', '222', '22', '2025-09-20 07:16:02', '', '', 2, '谢谢您的举报，我们将这些非法行为严惩', 1, '2025-09-20 07:16:04', '2025-09-20 07:23:56');
INSERT INTO `report` VALUES (2, 4, '举报', '举报', '翻阳湖', '2025-09-20 07:24:48', '/file/files/5439d50b4fb840a280617b38e9a072b4.png', '19876543214', 2, '处理完成', 0, '2025-09-20 07:24:59', '2025-09-20 07:26:56');
INSERT INTO `report` VALUES (3, 4, '有人违规', '有人违规', '测试', '2025-09-21 00:00:00', '', '', 2, '谢谢您的举报', 0, '2025-09-21 11:05:28', '2025-09-21 11:08:04');
INSERT INTO `report` VALUES (4, 4, '我的举报10-08', '我的举报10-08', '我的举报10-08', '2025-10-07 00:00:00', '', '', 2, '谢谢', 1, '2025-10-08 15:42:33', '2025-10-08 15:42:53');

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `travel_id` int(11) NOT NULL COMMENT '关联景点ID',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '门票名称',
  `description` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '门票描述',
  `ori_price` decimal(10, 2) NOT NULL COMMENT '原始价格',
  `price` decimal(10, 2) NOT NULL COMMENT '门票价格',
  `inventory` int(11) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `validity_days` int(11) NULL DEFAULT NULL COMMENT '有效期天数',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态（0-下架，1-上架）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '门票表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket
-- ----------------------------
INSERT INTO `ticket` VALUES (1, 10, '测试', '测试', 120.00, 100.00, 100, 30, 1, '2025-09-21 15:30:01', '2025-09-21 15:30:49');
INSERT INTO `ticket` VALUES (2, 10, '门票1', '不错的价格', 120.00, 100.00, 100, 30, 1, '2025-09-21 15:47:09', '2025-09-24 22:36:59');
INSERT INTO `ticket` VALUES (3, 10, '门票2', '很好', 120.00, 100.00, 93, 30, 1, '2025-09-21 15:47:22', '2025-10-08 15:41:53');
INSERT INTO `ticket` VALUES (4, 9, '测试', '不错的', 199.00, 99.00, 999, 1, 1, '2025-09-24 22:47:29', '2025-09-24 22:47:44');
INSERT INTO `ticket` VALUES (5, 9, '门票2', '222', 199.00, 100.00, 99, 30, 1, '2025-09-24 22:51:05', '2025-09-24 22:54:24');
INSERT INTO `ticket` VALUES (6, 8, '文化园门票', '很不错的', 199.00, 100.00, 99, 30, 1, '2025-09-24 22:56:16', '2025-09-24 22:56:16');

-- ----------------------------
-- Table structure for ticket_order
-- ----------------------------
DROP TABLE IF EXISTS `ticket_order`;
CREATE TABLE `ticket_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单号',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `ticket_id` int(11) NOT NULL COMMENT '门票ID',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '订单号',
  `pay_num` int(11) NULL DEFAULT NULL COMMENT '下单数量',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态(1-已付款,2-已完成,3-已取消)',
  `contact_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '联系人手机号',
  `card_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '身份证号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '门票订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket_order
-- ----------------------------
INSERT INTO `ticket_order` VALUES (2, 4, 3, 'ZyLK422935', 1, 100.00, 2, '张安', '19809719801', '122', '2025-09-21 17:40:52', '2025-09-21 17:48:59');
INSERT INTO `ticket_order` VALUES (3, 4, 3, 'iFNI517502', 1, 100.00, 2, '张鹏', '18909710981', '222', '2025-09-21 17:41:34', '2025-10-24 21:00:00');
INSERT INTO `ticket_order` VALUES (4, 4, 3, 'YpoS397178', 1, 100.00, 2, '张安', '19809717622', '555', '2025-09-24 22:26:42', '2025-10-24 23:00:00');
INSERT INTO `ticket_order` VALUES (5, 4, 2, 'HylX751721', 1, 100.00, 3, '战三国', '19878617651', '11', '2025-09-24 22:34:23', '2025-09-24 22:36:59');
INSERT INTO `ticket_order` VALUES (6, 4, 3, 'zyHK678585', 1, 100.00, 2, '张三', '19809817861', '22', '2025-09-24 22:36:52', '2025-09-24 22:37:01');
INSERT INTO `ticket_order` VALUES (7, 4, 3, 'CgmU267358', 1, 100.00, 2, '站桑', '19808767891', '22', '2025-09-24 22:49:19', '2025-09-24 22:49:23');
INSERT INTO `ticket_order` VALUES (8, 4, 3, 'gQkV107904', 1, 100.00, 2, '张三', '19809716543', '222', '2025-09-24 22:55:37', '2025-10-24 23:00:00');
INSERT INTO `ticket_order` VALUES (9, 4, 3, 'mxBD178550', 1, 100.00, 2, '张安', '19879986167', '123', '2025-10-08 15:41:52', '2025-10-08 15:41:58');

-- ----------------------------
-- Table structure for travel
-- ----------------------------
DROP TABLE IF EXISTS `travel`;
CREATE TABLE `travel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '简介',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '描述',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '位置',
  `price` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '价格',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系方式',
  `img_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面图片',
  `rating` decimal(3, 1) NULL DEFAULT 0.0 COMMENT '评分',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '旅游景点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of travel
-- ----------------------------
INSERT INTO `travel` VALUES (2, '鄱阳湖国家湿地公园', '中国最大的淡水湖湿地，观赏胜地', '<h1>鄱阳湖国家湿地公园</h1>\r\n<p>鄱阳湖国家湿地公园位于江西省鄱阳县境内，是中国最大的淡水湖湿地。这里是世界上最重要的候鸟栖息地之一，每年有超过60万只候鸟在此越冬。</p>\r\n<h2>主要特色</h2>\r\n<ul>\r\n<li>白鹤之乡：全球98%以上的白鹤在此越冬</li>\r\n<li>候鸟天堂：300多种动物的栖息地</li>\r\n<li>湿地生态：完整的湿地生态系统</li>\r\n<li>科普教育：丰富的生态科普资源</li>\r\n</ul>\r\n<p><img src=\"/file/files/4b487b4a7c314dfd852cd8f20f4931d3.png\" alt=\"鄱阳湖湿地风光\"/></p>', '江西省上饶市鄱阳县', '免费开放', '0793-6273999', '/file/files/4b487b4a7c314dfd852cd8f20f4931d3.png', 4.8, 1, '2025-09-12 09:00:00', '2025-09-20 13:56:34');
INSERT INTO `travel` VALUES (3, '都昌候鸟省级自然保护区', '白鹤的重要栖息地，观赏摄影圣地', '<h1>都昌候鸟省级自然保护区</h1>\r\n<p>都昌候鸟省级自然保护区是鄱阳湖区域最重要的候鸟保护区之一，以保护白鹤等珍稀候鸟为主要目标。</p>\r\n<h2>观赏亮点</h2>\r\n<ul>\r\n<li>白鹤聚集地：每年11月至次年3月，数千只白鹤在此栖息</li>\r\n<li>多样动物：灰鹤、丹顶鹤、东方白鹳等珍稀动物</li>\r\n<li>最佳观赏时间：清晨6-8点，傍晚4-6点</li>\r\n<li>专业设施：观赏台、望远镜租赁服务</li>\r\n</ul>\r\n<p><img src=\"/file/files/3ed8cb7c7f2f4304bf081cdcd61bb8c3.png\" alt=\"都昌观赏区\"/></p>', '江西省九江市都昌县', '门票30元/人', '0792-5222888', '/file/files/3ed8cb7c7f2f4304bf081cdcd61bb8c3.png', 4.7, 1, '2025-09-12 15:30:00', '2025-09-20 14:04:40');
INSERT INTO `travel` VALUES (4, '永修吴城候鸟小镇', '候鸟观察的最佳据点，生态旅游示范区', '<h1>永修吴城候鸟小镇</h1>\r\n<p>吴城候鸟小镇位于鄱阳湖核心区域，是观察候鸟的最佳据点。小镇融合了观赏、科普、休闲于一体。</p>\r\n<h2>小镇特色</h2>\r\n<ul>\r\n<li>观赏塔：360度观赏视野，配备专业望远镜</li>\r\n<li>科普馆：候鸟知识互动展示</li>\r\n<li>生态步道：2公里木栈道深入湿地</li>\r\n<li>民宿体验：候鸟主题民宿住宿</li>\r\n</ul>\r\n<p><img src=\"/file/files/29d82f59b4f141b98f87cbd94c0860a1.png\" alt=\"吴城候鸟小镇\"/></p>', '江西省九江市永修县吴城镇', '门票50元/人', '0792-3051999', '/file/files/29d82f59b4f141b98f87cbd94c0860a1.png', 4.9, 0, '2025-09-13 10:15:00', '2025-09-13 10:15:00');
INSERT INTO `travel` VALUES (5, '南矶湿地国家级自然保护区', '水鸟的天堂，湿地生态的典型代表', '<h1>南矶湿地国家级自然保护区</h1>\r\n<p>南矶湿地是鄱阳湖区域重要的湿地保护区，以保护水鸟及其栖息环境为主要目标，是湿地生态系统的典型代表。</p>\r\n<h2>生态特色</h2>\r\n<ul>\r\n<li>水鸟乐园：小天鹅、豆雁、白额雁等大型水鸟聚集地</li>\r\n<li>湿地植被：丰富的湿地植物群落</li>\r\n<li>生态完整：保持原始的湿地生态环境</li>\r\n<li>科研价值：重要的湿地生态研究基地</li>\r\n</ul>\r\n<p><img src=\"/file/files/49f23cc9309c49c3bc956c72d83424b0.png\" alt=\"南矶湿地\"/></p>', '江西省南昌市新建区南矶乡', '门票40元/人', '0791-3678999', '/file/files/49f23cc9309c49c3bc956c72d83424b0.png', 4.6, 2, '2025-09-13 14:45:00', '2025-09-27 16:02:19');
INSERT INTO `travel` VALUES (6, '鄱阳湖湿地科学园', '集科普教育与观赏体验于一体', '<h1>鄱阳湖湿地科学园</h1>\r\n<p>鄱阳湖湿地科学园是一个集科普教育、观赏体验、生态展示于一体的综合性湿地公园。</p>\r\n<h2>园区设施</h2>\r\n<ul>\r\n<li>科普展馆：湿地生态系统全景展示</li>\r\n<li>观赏廊道：500米玻璃廊道，不惊扰动物</li>\r\n<li>互动体验：VR观赏、动物声音识别</li>\r\n<li>教育基地：中小学生生态教育实践基地</li>\r\n</ul>\r\n<p><img src=\"/file/files/eafec69dd7e14652801767c8c73fc0fe.png\" alt=\"湿地科学园\"/></p>', '江西省九江市湖口县', '门票60元/人，学生半价', '0792-6789123', '/file/files/eafec69dd7e14652801767c8c73fc0fe.png', 4.5, 0, '2025-09-14 11:20:00', '2025-09-14 11:20:00');
INSERT INTO `travel` VALUES (7, '余干康山观赏大堤', '视野开阔的观赏胜地，摄影师最爱', '<h1>余干康山观赏大堤</h1>\r\n<p>康山大堤是鄱阳湖区域视野最开阔的观赏点之一，特别适合观察和拍摄大型水鸟群体。</p>\r\n<h2>观赏优势</h2>\r\n<ul>\r\n<li>视野开阔：可观察到数公里范围内的动物活动</li>\r\n<li>摄影圣地：绝佳的动物摄影机位</li>\r\n<li>交通便利：可驾车直达观赏点</li>\r\n<li>全天开放：24小时开放，适合不同时段观赏</li>\r\n</ul>\r\n<p><img src=\"/file/files/bfc655eb31634eb3aa3ba2ff51c7f501.png\" alt=\"康山大堤\"/></p>', '江西省上饶市余干县康山乡', '免费开放', '0793-3456789', '/file/files/bfc655eb31634eb3aa3ba2ff51c7f501.png', 4.4, 0, '2025-09-14 16:00:00', '2025-09-14 16:00:00');
INSERT INTO `travel` VALUES (8, '湖口石钟山观赏文化园', '历史文化与观赏体验的完美结合', '<h1>湖口石钟山观赏文化园</h1>\r\n<p>石钟山观赏文化园将历史文化与现代观赏体验完美结合，是文化旅游与生态旅游的典型代表。</p>\r\n<h2>文化特色</h2>\r\n<ul>\r\n<li>历史底蕴：千年古迹与现代观赏的结合</li>\r\n<li>文化展示：鄱阳湖动物文化历史展览</li>\r\n<li>观景平台：登高望远，俯瞰鄱阳湖全景</li>\r\n<li>夜间观赏：特色夜鹭观察项目</li>\r\n</ul>\r\n<p><img src=\"/file/files/d126e52db2004c2d942619adeb224247.png\" alt=\"石钟山文化园\"/></p>', '江西省九江市湖口县石钟山', '门票80元/人', '0792-6543210', '/file/files/d126e52db2004c2d942619adeb224247.png', 4.3, 4, '2025-09-15 09:30:00', '2025-09-24 22:56:31');
INSERT INTO `travel` VALUES (9, '进贤青岚湖生态观赏区', '鄱阳湖支流的隐秘观赏天堂', '<h1>进贤青岚湖生态观赏区</h1>\r\n<p>青岚湖是鄱阳湖的重要支流湿地，相对较少游客，是观赏爱好者的隐秘天堂。</p>\r\n<h2>生态亮点</h2>\r\n<ul>\r\n<li>原生态环境：保持原始的湿地生态</li>\r\n<li>鹬类天堂：各种鹬类、鸻类的重要栖息地</li>\r\n<li>安静环境：游客较少，动物不易受惊扰</li>\r\n<li>专业向导：当地专业观赏向导服务</li>\r\n</ul>\r\n<p><img src=\"/file/files/8e55288d3d0648fd8842734fe1663797.png\" alt=\"青岚湖生态区\"/></p>', '江西省南昌市进贤县青岚湖', '门票35元/人', '0791-5678901', '/file/files/8e55288d3d0648fd8842734fe1663797.png', 4.7, 83, '2025-09-15 13:15:00', '2025-09-24 22:56:17');
INSERT INTO `travel` VALUES (10, '鄱阳湖候鸟医院观光区', '候鸟救护与科普教育基地', '<h1>鄱阳湖候鸟医院观光区</h1>\r\n<p>候鸟医院是专门救护受伤候鸟的医疗机构，同时也是重要的科普教育基地。</p>\r\n<h2>教育意义</h2>\r\n<ul>\r\n<li>救护展示：观看候鸟救护过程</li>\r\n<li>康复中心：康复期候鸟近距离观察</li>\r\n<li>科普讲座：专业兽医科普讲解</li>\r\n<li>志愿服务：参与候鸟保护志愿活动</li>\r\n</ul>\r\n<p><img src=\"/file/files/f8c713ef42f646f7bc9acbd84001b375.png\" alt=\"候鸟医院\"/></p>', '江西省九江市永修县', '门票20元/人，含科普讲座', '0792-3456123', '/file/files/f8c713ef42f646f7bc9acbd84001b375.png', 4.8, 300, '2025-09-16 10:45:00', '2025-10-08 15:41:27');

-- ----------------------------
-- Table structure for travel_guide
-- ----------------------------
DROP TABLE IF EXISTS `travel_guide`;
CREATE TABLE `travel_guide`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `travel_id` int(11) NOT NULL COMMENT '景点ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `img_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面图片',
  `rating` decimal(3, 1) NULL DEFAULT 0.0 COMMENT '评分',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '旅游攻略表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of travel_guide
-- ----------------------------
INSERT INTO `travel_guide` VALUES (1, 2, 1, '进贤青岚湖旅游攻略', '<h1 style=\"text-align: start;\">进贤青岚湖</h1><p>很好看，快来打卡，哈哈哈哈哈</p><p><img src=\"http://localhost:8080/api/file/files/6523761c88444774b70c3ab658375aff.png\" alt=\"\" data-href=\"\" style=\"\"/></p>', '/file/files/7a6b85ada5fd49c0badb699c092214c8.png', 5.0, 12, '2025-09-20 10:34:00', '2025-09-20 12:20:05');
INSERT INTO `travel_guide` VALUES (2, 4, 10, '我的攻略', '<h1>我的攻略</h1><p>这是我的攻略</p><p><img src=\"http://localhost:8080/api/file/files/0aafe7eb9c2d4accb5ba8976d3b56010.png\" alt=\"\" data-href=\"\" style=\"\"/></p>', '/file/files/bb4ec1d2af374c6c9d1795926d128f29.png', 0.0, 1, '2025-09-20 12:58:14', '2025-09-20 16:13:06');
INSERT INTO `travel_guide` VALUES (3, 8, 2, '鄱阳湖国家湿地公园完全观赏攻略', '<h1>鄱阳湖国家湿地公园完全观赏攻略</h1>\r\n<h2>最佳观赏时间</h2>\r\n<p>每年10月至次年3月是最佳观赏季节，其中12月至1月动物数量最多。建议清晨6:00-8:00和傍晚16:00-18:00前往，此时动物最为活跃。</p>\r\n<h2>必备装备</h2>\r\n<ul>\r\n<li>双筒望远镜（8x42或10x42规格最佳）</li>\r\n<li>动物图鉴或识鸟APP</li>\r\n<li>保暖衣物（冬季湖边风大）</li>\r\n<li>防水鞋套</li>\r\n<li>相机长焦镜头（400mm以上）</li>\r\n</ul>\r\n<h2>观赏路线推荐</h2>\r\n<p>1. 游客中心 → 观赏台1号 → 湿地栈道 → 观赏台2号 → 白鹤观察点</p>\r\n<p>2. 全程约3-4小时，建议携带干粮和热水</p>\r\n<h2>常见动物及观察要点</h2>\r\n<p><strong>白鹤</strong>：主要在浅水区觅食，成群出现，建议在观赏台2号观察</p>\r\n<p><strong>小天鹅</strong>：数量最多，在深水区游泳，清晨最为活跃</p>\r\n<p><strong>灰鹤</strong>：警觉性高，需保持距离观察</p>\r\n<p><img src=\"/file/files/4b487b4a7c314dfd852cd8f20f4931d3.png\" alt=\"湿地公园观赏\"/></p>', '/file/files/4b487b4a7c314dfd852cd8f20f4931d3.png', 4.9, 0, '2025-09-12 20:30:00', '2025-09-12 20:30:00');
INSERT INTO `travel_guide` VALUES (4, 9, 3, '都昌白鹤观察完全指南', '<h1>都昌白鹤观察完全指南</h1>\r\n<h2>白鹤观察最佳时机</h2>\r\n<p>都昌是全球白鹤最重要的越冬地，每年11月中旬至次年2月底是观察白鹤的黄金时期。</p>\r\n<h2>观察点选择</h2>\r\n<p><strong>主观赏台</strong>：视野最佳，可观察到大群白鹤</p>\r\n<p><strong>湖边栈道</strong>：距离较近，适合拍摄个体特写</p>\r\n<p><strong>农田观察点</strong>：可观察白鹤觅食行为</p>\r\n<h2>白鹤行为观察要点</h2>\r\n<ul>\r\n<li>觅食时间：清晨和傍晚最为活跃</li>\r\n<li>觅食方式：用嘴在泥中挖掘植物根茎</li>\r\n<li>群体行为：通常20-50只成群活动</li>\r\n<li>警戒行为：有专门的哨兵负责警戒</li>\r\n</ul>\r\n<h2>摄影技巧</h2>\r\n<p>1. 使用600mm以上长焦镜头</p>\r\n<p>2. 选择逆光或侧逆光拍摄，突出羽毛质感</p>\r\n<p>3. 连拍模式捕捉飞行瞬间</p>\r\n<p>4. 注意构图，利用倒影增加画面层次</p>\r\n<p><img src=\"/file/files/3ed8cb7c7f2f4304bf081cdcd61bb8c3.png\" alt=\"都昌白鹤\"/></p>', '/file/files/3ed8cb7c7f2f4304bf081cdcd61bb8c3.png', 4.8, 0, '2025-09-13 18:45:00', '2025-09-13 18:45:00');
INSERT INTO `travel_guide` VALUES (5, 10, 4, '吴城候鸟小镇深度体验攻略', '<h1>吴城候鸟小镇深度体验攻略</h1>\r\n<h2>住宿推荐</h2>\r\n<p>建议选择候鸟主题民宿，可以体验当地渔民生活，清晨还能听到各种动物的叫声作为天然闹钟。</p>\r\n<h2>观赏塔使用指南</h2>\r\n<p>观赏塔分为三层：</p>\r\n<ul>\r\n<li>一层：科普展示区，了解候鸟基础知识</li>\r\n<li>二层：观赏区，配备专业望远镜</li>\r\n<li>三层：360度观景平台，俯瞰整个湿地</li>\r\n</ul>\r\n<h2>生态步道徒步攻略</h2>\r\n<p>2公里木栈道分为4个观察点：</p>\r\n<p>1. 水鸟观察点：观察鸭类、鹅类</p>\r\n<p>2. 鹤类观察点：白鹤、灰鹤聚集地</p>\r\n<p>3. 鹭类观察点：各种鹭类栖息地</p>\r\n<p>4. 综合观察点：多种动物混合区域</p>\r\n<h2>当地美食推荐</h2>\r\n<p>鄱阳湖鱼宴、莲子汤、湖区野菜等特色美食，体验当地渔民文化。</p>\r\n<p><img src=\"/file/files/29d82f59b4f141b98f87cbd94c0860a1.png\" alt=\"吴城小镇\"/></p>', '/file/files/29d82f59b4f141b98f87cbd94c0860a1.png', 4.7, 0, '2025-09-14 12:20:00', '2025-09-14 12:20:00');
INSERT INTO `travel_guide` VALUES (6, 11, 5, '南矶湿地水鸟观察专业指南', '<h1>南矶湿地水鸟观察专业指南</h1>\r\n<h2>水鸟种类及分布</h2>\r\n<p><strong>小天鹅</strong>：主要分布在深水区，数量可达2-3万只</p>\r\n<p><strong>豆雁</strong>：喜欢在浅水区和农田活动</p>\r\n<p><strong>白额雁</strong>：通常在草地觅食</p>\r\n<p><strong>赤麻鸭</strong>：适应性强，各种水域都有分布</p>\r\n<h2>观察技巧</h2>\r\n<ul>\r\n<li>使用单筒望远镜进行远距离观察</li>\r\n<li>学会通过飞行姿态识别不同动物</li>\r\n<li>注意观察动物的觅食行为差异</li>\r\n<li>记录观察时间、天气、动物数量</li>\r\n</ul>\r\n<h2>科研价值</h2>\r\n<p>南矶湿地是重要的科研基地，游客观察数据可为科研提供支持。建议下载eBird等观赏记录APP，为全球动物保护贡献数据。</p>\r\n<h2>保护意识</h2>\r\n<p>观赏时请保持安静，不要大声喧哗，不要投喂动物，不要使用闪光灯拍照。</p>\r\n<p><img src=\"/file/files/49f23cc9309c49c3bc956c72d83424b0.png\" alt=\"南矶湿地\"/></p>', '/file/files/49f23cc9309c49c3bc956c72d83424b0.png', 4.6, 0, '2025-09-15 14:30:00', '2025-09-15 14:30:00');
INSERT INTO `travel_guide` VALUES (7, 12, 6, '湿地科学园亲子观赏攻略', '<h1>湿地科学园亲子观赏攻略</h1>\r\n<h2>适合年龄</h2>\r\n<p>建议6岁以上儿童参与，配备儿童专用望远镜和动物图鉴。</p>\r\n<h2>互动体验项目</h2>\r\n<ul>\r\n<li>VR观赏：身临其境体验动物视角</li>\r\n<li>动物声音识别：学习不同动物的叫声</li>\r\n<li>湿地生态模拟：了解湿地食物链</li>\r\n<li>动物标本观察：近距离观察动物结构</li>\r\n</ul>\r\n<h2>教育活动安排</h2>\r\n<p>每天上午10:00和下午15:00有专业讲解员带领的科普活动，包括：</p>\r\n<p>1. 湿地生态系统讲解（30分钟）</p>\r\n<p>2. 动物识别技巧教学（20分钟）</p>\r\n<p>3. 实地观赏体验（60分钟）</p>\r\n<p>4. 观赏日记制作（30分钟）</p>\r\n<h2>安全注意事项</h2>\r\n<p>观赏廊道有玻璃护栏，但仍需注意儿童安全。建议携带防蚊虫用品和饮用水。</p>\r\n<p><img src=\"/file/files/eafec69dd7e14652801767c8c73fc0fe.png\" alt=\"科学园\"/></p>', '/file/files/eafec69dd7e14652801767c8c73fc0fe.png', 4.5, 0, '2025-09-16 11:15:00', '2025-09-16 11:15:00');
INSERT INTO `travel_guide` VALUES (8, 13, 7, '康山大堤摄影观赏攻略', '<h1>康山大堤摄影观赏攻略</h1>\r\n<h2>摄影最佳时间</h2>\r\n<p><strong>日出时分（6:00-7:30）</strong>：金色光线，适合拍摄剪影和逆光作品</p>\r\n<p><strong>上午时段（8:00-10:00）</strong>：光线柔和，适合拍摄动物细节</p>\r\n<p><strong>傍晚时分（16:00-18:00）</strong>：暖色调光线，适合拍摄群鸟归巢</p>\r\n<h2>摄影器材建议</h2>\r\n<ul>\r\n<li>相机：全画幅单反或微单</li>\r\n<li>镜头：400-600mm长焦镜头</li>\r\n<li>三脚架：稳定支撑，减少抖动</li>\r\n<li>偏振镜：减少水面反光</li>\r\n</ul>\r\n<h2>构图技巧</h2>\r\n<p>1. 利用大堤的线条作为引导线</p>\r\n<p>2. 水面倒影增加画面层次</p>\r\n<p>3. 群鸟飞行时使用连拍模式</p>\r\n<p>4. 注意前景、中景、背景的层次关系</p>\r\n<h2>观赏摄影伦理</h2>\r\n<p>保持适当距离，不要为了拍照而惊扰动物。使用长焦镜头远距离拍摄，避免使用闪光灯。</p>\r\n<p><img src=\"/file/files/bfc655eb31634eb3aa3ba2ff51c7f501.png\" alt=\"康山大堤\"/></p>', '/file/files/bfc655eb31634eb3aa3ba2ff51c7f501.png', 4.8, 0, '2025-09-17 15:45:00', '2025-09-17 15:45:00');
INSERT INTO `travel_guide` VALUES (9, 14, 8, '石钟山夜间观赏特色体验', '<h1>石钟山夜间观赏特色体验</h1>\r\n<h2>夜鹭观察项目</h2>\r\n<p>石钟山是观察夜鹭的最佳地点之一，每晚18:00-21:00是夜鹭最活跃的时间。</p>\r\n<h2>夜间观赏装备</h2>\r\n<ul>\r\n<li>红光手电筒（不惊扰动物）</li>\r\n<li>夜视望远镜或红外观赏设备</li>\r\n<li>保暖衣物（夜间温度较低）</li>\r\n<li>录音设备（记录动物叫声）</li>\r\n</ul>\r\n<h2>观察要点</h2>\r\n<p><strong>夜鹭行为</strong>：白天栖息在树上，傍晚开始活跃觅食</p>\r\n<p><strong>叫声识别</strong>：粗哑的\"呱呱\"声，与白天的动物叫声明显不同</p>\r\n<p><strong>觅食方式</strong>：在水边静静等待，突然出击捕鱼</p>\r\n<h2>文化体验</h2>\r\n<p>结合石钟山的历史文化，了解古人对动物的观察和记录，体验传统文化与现代观赏的结合。</p>\r\n<h2>安全提醒</h2>\r\n<p>夜间观赏需注意安全，建议结伴而行，携带通讯设备，告知家人行程安排。</p>\r\n<p><img src=\"/file/files/d126e52db2004c2d942619adeb224247.png\" alt=\"石钟山夜观\"/></p>', '/file/files/d126e52db2004c2d942619adeb224247.png', 4.4, 0, '2025-09-18 19:20:00', '2025-09-18 19:20:00');
INSERT INTO `travel_guide` VALUES (10, 15, 9, '青岚湖鹬类观察专业攻略', '<h1>青岚湖鹬类观察专业攻略</h1>\r\n<h2>鹬类观察季节</h2>\r\n<p>春季（3-5月）和秋季（8-10月）是观察鹬类的最佳时期，此时正值迁徙季节，种类最为丰富。</p>\r\n<h2>常见鹬类识别</h2>\r\n<p><strong>黑翅长脚鹬</strong>：腿部极长，黑白分明</p>\r\n<p><strong>反嘴鹬</strong>：嘴部向上弯曲，独特易识别</p>\r\n<p><strong>金眶鸻</strong>：眼部金色环，体型较小</p>\r\n<p><strong>环颈鸻</strong>：颈部有明显环带</p>\r\n<h2>观察技巧</h2>\r\n<ul>\r\n<li>选择退潮时观察，泥滩暴露面积大</li>\r\n<li>使用单筒望远镜，倍数20-60倍</li>\r\n<li>注意观察不同鹬类的觅食方式差异</li>\r\n<li>记录动物的腿色、嘴型、体型特征</li>\r\n</ul>\r\n<h2>生态价值</h2>\r\n<p>鹬类是湿地生态系统的重要指示物种，它们的数量和种类反映了湿地环境的健康状况。</p>\r\n<h2>保护行动</h2>\r\n<p>参与当地的动物环志活动，为鹬类迁徙研究贡献数据。</p>\r\n<p><img src=\"/file/files/8e55288d3d0648fd8842734fe1663797.png\" alt=\"青岚湖鹬类\"/></p>', '/file/files/8e55288d3d0648fd8842734fe1663797.png', 4.7, 1, '2025-09-19 16:30:00', '2025-09-20 13:56:08');
INSERT INTO `travel_guide` VALUES (11, 16, 10, '候鸟医院科普教育深度体验', '<h1>候鸟医院科普教育深度体验</h1>\r\n<h2>参观流程</h2>\r\n<p>1. 接待大厅：了解候鸟医院历史和使命</p>\r\n<p>2. 急救室：观看候鸟救护过程（如有）</p>\r\n<p>3. 康复中心：近距离观察康复期候鸟</p>\r\n<p>4. 放飞区：参与候鸟放飞仪式（周末）</p>\r\n<h2>科普讲座内容</h2>\r\n<ul>\r\n<li>候鸟常见疾病和受伤原因</li>\r\n<li>候鸟救护技术和康复过程</li>\r\n<li>人类活动对候鸟的影响</li>\r\n<li>如何参与候鸟保护行动</li>\r\n</ul>\r\n<h2>志愿服务机会</h2>\r\n<p>可申请成为候鸟医院志愿者，参与：</p>\r\n<p>- 候鸟日常护理</p>\r\n<p>- 科普宣传活动</p>\r\n<p>- 野外救护行动</p>\r\n<p>- 数据记录整理</p>\r\n<h2>教育意义</h2>\r\n<p>通过实地参观和互动体验，深入了解候鸟保护的重要性和紧迫性，培养生态保护意识。</p>\r\n<p><img src=\"/file/files/f8c713ef42f646f7bc9acbd84001b375.png\" alt=\"候鸟医院\"/></p>', '/file/files/f8c713ef42f646f7bc9acbd84001b375.png', 4.9, 3, '2025-09-20 14:15:00', '2025-09-21 11:04:13');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `role` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色',
  `age` int(11) NULL DEFAULT 18 COMMENT '年龄',
  `gender` int(11) NULL DEFAULT 2 COMMENT '性别（0-男，1-女，2-保密）',
  `avatar` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `profile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人介绍',
  `status` int(11) NULL DEFAULT 1 COMMENT '账号状态（0-封禁，1-正常）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123', '管理员', 'admin', 18, 2, '/file/files/eb9db3a4019e48f4b8597e4089eb94ab.jpg', '简介很简洁', 1, '2025-09-01 13:52:30', '2025-09-12 23:47:02');
INSERT INTO `user` VALUES (2, 'caiya', '123', '管理员', 'admin', 18, 2, '/file/files/e829b0cfc24d4a109189612a6e9ff24c.png', '2222', 1, '2025-09-01 13:52:30', '2025-10-05 08:28:57');
INSERT INTO `user` VALUES (4, 'user', '123', '张三', 'user', 25, 0, '/file/files/31ce7826131a426a8a79738ab3446025.jpg', '这是一段个人介绍', 1, '2025-09-12 23:48:32', '2025-10-05 08:33:03');
INSERT INTO `user` VALUES (5, 'zhangsan', '123', '张三', 'user', 25, 0, '/file/files/4738603f598a494dac889b3921dbd5ca.jpg', '这是一段个人介绍', 1, '2025-09-12 23:59:48', '2025-09-13 00:00:28');
INSERT INTO `user` VALUES (6, 'ccc', '123', 'ccc', 'user', 18, 2, NULL, '这个人很懒，还没有简介', 1, '2025-09-20 00:24:44', '2025-09-20 00:24:44');
INSERT INTO `user` VALUES (7, 'aaa', '123', 'aaa', 'user', 18, 2, NULL, '这个人很懒，还没有简介', 1, '2025-09-20 00:26:36', '2025-09-20 00:26:36');
INSERT INTO `user` VALUES (8, 'animallover01', '123', '观赏达人小李', 'user', 28, 0, '/file/files/72c7b1ed0abb41a79cd3d9f6fa0fa658.jpg', '热爱观赏摄影，专注鄱阳湖候鸟拍摄5年', 1, '2025-09-12 08:30:00', '2025-09-12 08:30:00');
INSERT INTO `user` VALUES (9, 'naturelover', '123', '自然之友小王', 'user', 32, 1, '/file/files/6c6c56f709df4b99be83eb746144b604.jpg', '生态保护志愿者，喜欢户外观赏和自然摄影', 1, '2025-09-12 14:20:00', '2025-09-12 14:20:00');
INSERT INTO `user` VALUES (10, 'photographer88', '123', '摄影师老张', 'user', 45, 0, '/file/files/29a652512a974d2ea31f28629db550cf.jpg', '专业野生动物摄影师，鄱阳湖观赏摄影专家', 1, '2025-09-13 09:15:00', '2025-09-13 09:15:00');
INSERT INTO `user` VALUES (11, 'ecotourist', '123', '生态旅行者小陈', 'user', 26, 1, NULL, '生态旅游爱好者，致力于推广可持续旅游', 1, '2025-09-13 16:45:00', '2025-09-13 16:45:00');
INSERT INTO `user` VALUES (12, 'animalwatcher99', '123', '观赏新手小刘', 'user', 23, 0, NULL, '刚入门的观赏爱好者，希望学习更多观赏知识', 1, '2025-09-14 10:30:00', '2025-09-14 10:30:00');
INSERT INTO `user` VALUES (13, 'wetlandguard', '123', '湿地守护者', 'user', 38, 1, NULL, '湿地保护工作者，关注鄱阳湖生态环境', 1, '2025-09-14 13:20:00', '2025-09-14 13:20:00');
INSERT INTO `user` VALUES (14, 'student_bio', '123', '生物系学生小赵', 'user', 21, 1, NULL, '生物学专业学生，研究候鸟迁徙规律', 1, '2025-09-15 11:00:00', '2025-09-15 11:00:00');
INSERT INTO `user` VALUES (15, 'familytrip', '123', '亲子游爱好者', 'user', 35, 0, NULL, '喜欢带孩子观赏，培养下一代的自然意识', 1, '2025-09-15 15:30:00', '2025-09-15 15:30:00');
INSERT INTO `user` VALUES (16, 'retired_teacher', '123', '退休教师老李', 'user', 62, 0, NULL, '退休生物教师，热衷于科普教育和观赏活动', 1, '2025-09-16 08:45:00', '2025-09-16 08:45:00');
INSERT INTO `user` VALUES (17, 'travel_blogger', '123', '旅行博主小美', 'user', 29, 1, NULL, '旅行博主，专注生态旅游内容创作', 1, '2025-09-16 12:15:00', '2025-09-16 12:15:00');
INSERT INTO `user` VALUES (18, 'conservation_vol', '123', '保护志愿者小周', 'user', 31, 0, NULL, '野生动物保护志愿者，参与多项保护行动', 1, '2025-09-17 09:30:00', '2025-09-17 09:30:00');
INSERT INTO `user` VALUES (19, 'animal_guide', '123', '观赏向导老王', 'user', 48, 0, NULL, '专业观赏向导，熟悉鄱阳湖各个观赏点', 1, '2025-09-17 14:00:00', '2025-09-17 14:00:00');
INSERT INTO `user` VALUES (20, 'nature_kid', '123', '自然小达人', 'user', 16, 1, NULL, '高中生，从小热爱自然和动物观察', 1, '2025-09-18 10:20:00', '2025-09-18 10:20:00');
INSERT INTO `user` VALUES (21, 'eco_researcher', '123', '生态研究员', 'user', 34, 1, NULL, '生态学研究员，专注湿地生态系统研究', 1, '2025-09-18 16:45:00', '2025-09-18 16:45:00');
INSERT INTO `user` VALUES (22, 'weekend_animaler', '123', '周末观赏客', 'user', 27, 0, NULL, '上班族，周末喜欢到鄱阳湖观赏放松', 1, '2025-09-19 11:30:00', '2025-09-19 11:30:00');
INSERT INTO `user` VALUES (23, 'photo_enthusiast', '123', '摄影爱好者小林', 'user', 30, 1, NULL, '业余摄影爱好者，喜欢拍摄动物和自然风光', 1, '2025-09-19 15:15:00', '2025-09-19 15:15:00');
INSERT INTO `user` VALUES (24, 'local_guide', '123', '本地向导老刘', 'user', 52, 0, '/file/files/201886932c1b43b7bb42f72d530f80b9.jpg', '鄱阳湖本地人，熟悉当地动物和最佳观赏时机', 1, '2025-09-20 08:00:00', '2025-09-20 15:37:36');
INSERT INTO `user` VALUES (25, 'animal_artist', '123', '动物画家', 'user', 41, 1, '/file/files/5c1d26c8f93c42b19cb1900f8edcb9e3.jpg', '专业画家，以动物为主题创作艺术作品', 1, '2025-09-20 13:45:00', '2025-09-20 15:37:27');

-- ----------------------------
-- Table structure for weight
-- ----------------------------
DROP TABLE IF EXISTS `weight`;
CREATE TABLE `weight`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `travel_id` int(11) NULL DEFAULT NULL COMMENT '景点ID',
  `behavior` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '行为（collect、like、view）',
  `score` int(11) NULL DEFAULT NULL COMMENT '分值（collect=3、like=2，view=1）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '权重表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weight
-- ----------------------------
INSERT INTO `weight` VALUES (1, 4, 1, 'view', 1, '2025-09-20 13:14:24', '2025-09-20 13:14:24');
INSERT INTO `weight` VALUES (29, 4, 8, 'view', 1, '2025-09-24 22:55:49', '2025-09-24 22:55:49');
INSERT INTO `weight` VALUES (26, 4, 5, 'view', 1, '2025-09-20 16:07:27', '2025-09-20 16:07:27');
INSERT INTO `weight` VALUES (4, 4, 10, 'view', 1, '2025-09-20 13:56:13', '2025-09-20 13:56:13');
INSERT INTO `weight` VALUES (27, 4, 10, 'like', 2, '2025-09-21 11:04:02', '2025-09-21 11:04:02');
INSERT INTO `weight` VALUES (6, 4, 10, 'collect', 3, '2025-09-20 13:56:16', '2025-09-20 13:56:16');
INSERT INTO `weight` VALUES (7, 4, 1, 'view', 1, '2025-09-20 13:56:21', '2025-09-20 13:56:21');
INSERT INTO `weight` VALUES (28, 4, 9, 'view', 1, '2025-09-24 22:29:22', '2025-09-24 22:29:22');
INSERT INTO `weight` VALUES (9, 4, 2, 'view', 1, '2025-09-20 13:56:34', '2025-09-20 13:56:34');
INSERT INTO `weight` VALUES (10, 4, 2, 'like', 2, '2025-09-20 13:56:36', '2025-09-20 13:56:36');
INSERT INTO `weight` VALUES (11, 4, 2, 'collect', 3, '2025-09-20 13:56:37', '2025-09-20 13:56:37');
INSERT INTO `weight` VALUES (12, 4, 3, 'view', 1, '2025-09-20 14:04:40', '2025-09-20 14:04:40');
INSERT INTO `weight` VALUES (13, 4, 3, 'like', 2, '2025-09-20 14:04:42', '2025-09-20 14:04:42');
INSERT INTO `weight` VALUES (14, 4, 3, 'collect', 3, '2025-09-20 14:04:43', '2025-09-20 14:04:43');
INSERT INTO `weight` VALUES (15, 24, 1, 'view', 1, '2025-09-20 14:05:20', '2025-09-20 14:05:20');
INSERT INTO `weight` VALUES (16, 24, 1, 'like', 2, '2025-09-20 14:05:21', '2025-09-20 14:05:21');
INSERT INTO `weight` VALUES (17, 24, 1, 'collect', 3, '2025-09-20 14:05:22', '2025-09-20 14:05:22');
INSERT INTO `weight` VALUES (18, 24, 10, 'view', 1, '2025-09-20 14:07:13', '2025-09-20 14:07:13');
INSERT INTO `weight` VALUES (19, 24, 10, 'like', 2, '2025-09-20 14:07:14', '2025-09-20 14:07:14');
INSERT INTO `weight` VALUES (20, 24, 10, 'collect', 3, '2025-09-20 14:07:15', '2025-09-20 14:07:15');
INSERT INTO `weight` VALUES (21, 25, 1, 'view', 1, '2025-09-20 14:07:30', '2025-09-20 14:07:30');
INSERT INTO `weight` VALUES (22, 25, 1, 'like', 2, '2025-09-20 14:07:31', '2025-09-20 14:07:31');
INSERT INTO `weight` VALUES (23, 25, 1, 'collect', 3, '2025-09-20 14:07:32', '2025-09-20 14:07:32');
INSERT INTO `weight` VALUES (24, 4, 11, 'view', 1, '2025-09-20 15:51:19', '2025-09-20 15:51:19');
INSERT INTO `weight` VALUES (25, 4, 10, 'view', 1, '2025-09-20 15:58:42', '2025-09-20 15:58:42');
INSERT INTO `weight` VALUES (30, 7, 10, 'view', 1, '2025-09-27 16:37:38', '2025-09-27 16:37:38');
INSERT INTO `weight` VALUES (31, 7, 10, 'like', 2, '2025-09-27 16:37:39', '2025-09-27 16:37:39');
INSERT INTO `weight` VALUES (32, 7, 10, 'collect', 3, '2025-09-27 16:37:40', '2025-09-27 16:37:40');

SET FOREIGN_KEY_CHECKS = 1;
