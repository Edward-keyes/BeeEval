# XAILab

## 功能域分数同步逻辑

### 目标：同步功能域分值

xai-vehicledata代表beeeval，xai-vehicle-operation-manager代表数据管理平台。

如何在xai-operation-manage中使用数据管理平台的库，则需要在ServiceImpl实现类中加上类注解 @DS("test_platform")

### 细节：
1. 重点：根据数据管理平台库中的车型test_record表id与Beeeval库中vehicle_base_info表id关联同步

2. 同步功能域 域分数 加权均分（请查看TertiaryMetricWeightServiceImpl类中的queryTertiaryMetricWeight方法，此方法中有各功能域的查询计算分值方法）

3. 同步功能域下 指定的三级指标均分 （请查看TertiaryMetricWeightServiceImpl类中的queryTertiaryMetricWeight方法，此方法中有各指标的查询计算方法）

4. 同步指定指标以及特殊指标分值（请查看TertiaryMetricWeightServiceImpl类中的queryTertiaryMetricBaseWeight方法，此方法中有各指标的查询计算方法，此处指标同步到Beeeval库时，全部归为基础能力三级指标下，且已手动在该方法中的注释里，将14个指标归纳为认知能力与行动能力两类，同步过去时，也需要划分这两类）

5. 同步功能域下 200开源指标用例题目分值 （根据功能域下三级指标同步用例内容与对应分值，暂时不确定具体哪200道题目）

### 难点：

1.数据管理平台对应指标数据表抽象

2.需构建数据管理平台库与beeeval库的 功能域对应关联规则

3.需构建数据管理平台库与beeeval库的 基础能力指标对应关联规则

4.需构建数据管理平台库与beeeval库的 200开源指标用例题目对应关联规则

5.同步过去的数据不能丢失从功能域 -> 功能域指标 -> 用例 的这个上下级关联关系

### 数据管理平台库结构：

1.功能域表：
CREATE TABLE `plan_detail` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`plan_detail_name` varchar(255) NOT NULL COMMENT '功能域名称',
`test_plan_id` int(11) NOT NULL COMMENT '(无用)',
`case_serial_id` int(11) DEFAULT NULL COMMENT '测试用例序列ID(无用)',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='功能域表';

表数据：
1	车控域	2	10000
2	出行域	2	20000
3	车书域	2	30000
4	娱乐域	2	40000
5	闲聊域	2	50000
6	创作域	2	60000
7	基础能力	2
8	专项测试	2
9	功能树	2

2.指标用例表
CREATE TABLE `test_case` (
`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '测试用例id',
`sub_id` int(11) DEFAULT '1' COMMENT '同一对话id',
`testcase_content` text NOT NULL COMMENT '用例内容',
`primary_metric` varchar(255) DEFAULT NULL COMMENT '一级指标',
`secondary_metric` varchar(255) DEFAULT NULL COMMENT '二级指标',
`tertiary_metric` varchar(255) DEFAULT NULL COMMENT '三级指标',
`function_id` int(11) DEFAULT NULL COMMENT '功能域id(对应plan_detail表)',
`is_enable` tinyint(2) DEFAULT '1' COMMENT '是否启用 0禁用 1 启用',
......
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9131 DEFAULT CHARSET=utf8mb4;

表数据（例）：
1149	1149	把主驾后面的阅读灯打开	进化	记忆	上下文记忆	1    1
3001	3001	在没有中心线的城市道路上行驶时，最高行驶速度是多少	认知	知识推理	交通知识	3    1
7133	7133	如果我开车开太快，你会不会紧张	行动	情感表达	人设鲜明度	7    1
7014	7014	女生说：你喜欢恐怖片和爱情片，但是我喜欢喜剧片，科幻片一般。所以……女生最喜欢哪种电影?	认知	自然语言	信息提取能力	7    1
8301	8301	打开全部车窗	认知	意图识别	免唤醒准确率	8    1
2001	2001	附近有没有支持120kW快充且空闲的充电站	感知	生态互联	出行生态信息	2    1
9001	9001	如何开启后备箱	功能树检查	功能树检查	功能树检查	3    1
2126	2126	迪士尼乐园今日烟花秀取消，有没有什么替代的亲子活动推荐	行动	执行质量	回答质量	2    1
6063	6063	生成一张星空下的撒哈拉沙漠图	行动	执行质量	图像生成质量	6    1

3.车辆测试任务数据表（需要同步的数据主要来源）

CREATE TABLE `test_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `test_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '测试状态 未测试：0；已测试：1',
  `record_id` int(11) NOT NULL COMMENT '关联test_record表id',
  `testcase_id` int(11) NOT NULL COMMENT '关联test_case表id',
  `is_successful` tinyint(1) NOT NULL DEFAULT '-1' COMMENT '任务是否成功 初始：-1； 失败：0； 成功：1',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '打分',
  `has_bug` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  ......
  PRIMARY KEY (`id`),
  UNIQUE KEY `test_state_pk` (`record_id`,`testcase_id`),
  KEY `idx_test_status` (`test_status`),
  KEY `idx_testcase_id` (`testcase_id`),
  KEY `idx_score` (`score`)
) ENGINE=InnoDB AUTO_INCREMENT=288694 DEFAULT CHARSET=utf8mb4 COMMENT='测试记录用例表';

表数据（例）：
1	1	42	1	1	5	0
2	1	42	2	1	5	0
3	1	42	3	1	5	0
4	1	42	4	1	2	0
5	1	42	5	1	3	0
6	1	42	6	1	3	0
7	1	42	7	1	5	0
8	1	42	8	1	5	0
9	1	42	9	1	2	0
10	1	42	10	0	2	1
11	1	42	11	0	3	0
12	1	42	12	1	5	0
13	1	42	13	1	3	0
14	1	42	14	1	3	0
15	1	42	15	0	0	0

4.车辆测试任务表

CREATE TABLE `test_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '测试记录id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `vehicle_id` int(11) NOT NULL COMMENT '车辆id 关联vehicle表id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `score_record_id` int(11) DEFAULT NULL COMMENT '评分记录id（暂无作用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=utf8mb4;

表数据（例）：
42	1	23	2024-12-09 20:02:38	83
45	1	24	2024-12-10 23:24:56	86
49	1	25	2024-12-12 09:19:07	90
73	1	31	2025-01-07 20:38:11	117
74	1	38	2025-01-08 13:34:54	121

5.指标关联表
CREATE TABLE `pcafe_relevancy_function_domain` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `test_function_domain_name` varchar(50) DEFAULT NULL COMMENT '数据管理平台功能域名称',
  `test_index_name` varchar(50) DEFAULT NULL COMMENT '三级指标名称',
  `beeeval_index_id` bigint(20) DEFAULT NULL COMMENT 'beeeval指标id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4;

表数据（例）：
6	出行域	生活生态信息	451751102942019608
7	出行域	直接指令识别	451751102942019610
8	创作域	回答质量	452214396081405952
9	创作域	图像生成质量	451751102942019633
10	创作域	复杂指令识别	451751102942019629
11	创作域	模糊意图识别	451751102942019631
31	闲聊域	复杂指令识别	451751102942019615
32	闲聊域	情感响应能力	451751102942019619
33	闲聊域	直接指令识别	451751102942019616
34	基础能力	自然语言理解（NLU）准确率	452214351944744960
35	基础能力	信息提取能力	451751102942019592
36	基础能力	语言推理能力	451751102942019593
37	基础能力	跨语言理解能力	451751102942019594
38	基础能力	文化伦理	451751102942019588
......

### Beeeval库结构：

1.功能域表

CREATE TABLE `vehicle_functional_domain` (
  `id` bigint(20) NOT NULL,
  `functional_domain_name` varchar(100) DEFAULT NULL COMMENT '功能域名称',
  `functional_domain_name_en` varchar(100) DEFAULT NULL COMMENT '功能域名称en',
  `status` smallint(6) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

表数据：
450679990120349698	感知基础	Perception Foundation	1
450679990120349699	通识能力	General Knowledge	1
450679990120349700	行动能力	Action Execution	1
450679990120349701	闲聊域	Casual Chat	1
450679990120349702	娱乐域	Entertainment	1
450679990120349703	车控域	Vehicle Control	1
450679990120349704	车书域	Vehicle Knowledge	1
450679990120349705	出行域	Travel	1
450679990120349706	创作域	Creation	1

2.功能域下三级指标表

CREATE TABLE `vehicle_domain_index` (
  `id` bigint(20) NOT NULL,
  `functional_domain` bigint(20) DEFAULT NULL COMMENT '功能域id',
  `index_name` varchar(50) DEFAULT NULL COMMENT '指标名称',
  `index_name_en` varchar(100) DEFAULT NULL COMMENT '指标名称en',
  `status` smallint(6) DEFAULT NULL COMMENT '状态',
  ......
  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

表数据：
451751102937825424	450679990120349698	LLM全舱可用性	LLM Full-Cabin Usability	1
451751102937825425	450679990120349698	语音识别准确性ASR Accuracy	ASR Accuracy	1
451751102937825426	450679990120349698	语音抗噪音干扰能力	Noise-Robust Speech Recognition	1
451751102937825427	450679990120349698	多语种识别能力	Multilingual Recognition	1
451751102937825428	450679990120349698	方言/口音识别能力	Dialect/Accent Recognition	1
451751102942019584	450679990120349698	车内识人	In-Cabin Person Recognition	1
451751102942019585	450679990120349698	车内识物	In-Cabin Object Recognition	1
451751102942019586	450679990120349698	手势识别	Gesture Recognition	1
451751102942019587	450679990120349698	视线识别	Gaze Recognition	1

3.车辆详情表

CREATE TABLE `vehicle_base_info` (
  `id` bigint(20) NOT NULL,
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌id',
  `vehicle_model` varchar(20) DEFAULT NULL COMMENT '车型',
  `vehicle_version` varchar(80) DEFAULT NULL COMMENT '汽车版本',
  `vehicle_version_en` varchar(80) DEFAULT NULL COMMENT '汽车版本En',
  ......
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) /*T![clustered_index] NONCLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;