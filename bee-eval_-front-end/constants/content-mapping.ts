interface Content {
  title: string;
  description: string;
  videoUrl: string;
  capabilities: string[];
}

interface SubcategoryMapping {
  [key: string]: {
    [key: string]: Content;
  };
}

interface ContentMapping {
  [key: string]: {
    [key: string]: {
      [key: string]: Content;
    };
  };
}

export const contentMapping: ContentMapping = {
  "用车助手": {
    "日程驾驶场景": {
      "车书展示": {
        title: "车书展示",
        description: "提供车书展示功能，支持用户查阅车辆功能说明和操作指南。",
        videoUrl: "/llm-capabilities videos/车书展示.mp4",
        capabilities: [
          "准确识别用户查阅车辆功能说明的意图",
          "仅依靠大模型整合答案，不能播放用户手册中的视频",
          "仅展示文字内容"
        ]
      },
      "操作直达": {
        title: "操作直达",
        description: "用户通过查询后可直接跳转至功能入口/直接操作，快速完成相关操作。",
        videoUrl: "/llm-capabilities videos/操作直达.mp4",
        capabilities: [
          "推理出用户有后续操作意图，并提供提示",
          "提供完整操作指导，包括界面操作步骤和语音操作话术"
        ]
      },
      "车辆信息查询": {
        title: "车辆信息查询",
        description: "提供车辆状态信息查询功能，包括油量、电池电量、胎压等信息的实时监测。",
        videoUrl: "/llm-capabilities videos/车辆信息查询.mp4",
        capabilities: [
          "准确识别用户需要查询车辆信息的意图",
          "语音播报内容，车机无相应界面展示"
        ]
      }
    },
    "紧急情况处理场景": {
      "故障诊断": {
        title: "智能故障诊断系统",
        description: "通过多传感器数据分析和深度学习模型，提供准确的车辆故障诊断和处理建议。",
        videoUrl: "/videos/diagnosis.mp4",
        capabilities: [
          "实时故障检测",
          "原因分析",
          "处理建议生成",
          "维修指导"
        ]
      },
      "应急指南": {
        title: "紧急情况处理指南",
        description: "为用户提供各类紧急情况下的处理指南，包括事故处理、天气应对等内容。",
        videoUrl: "/videos/emergency.mp4",
        capabilities: [
          "场景识别",
          "应急预案生成",
          "步骤引导",
          "安全提示"
        ]
      },
      "救援服务": {
        title: "一键救援服务",
        description: "提供快速救援服务请求和救援进度追踪功能，支持实时位置共享和救援人员通讯。",
        videoUrl: "/videos/rescue.mp4",
        capabilities: [
          "位置共享",
          "救援调度",
          "进度追踪",
          "实时通讯"
        ]
      }
    },
    "车辆维护与保养场景": {
      "保养提醒": {
        title: "智能保养提醒系统",
        description: "基于车辆使用状况和历史数据，提供个性化的保养提醒和建议服务。",
        videoUrl: "/videos/maintenance.mp4",
        capabilities: [
          "数据分析",
          "预测性维护",
          "保养计划制定",
          "成本估算"
        ]
      },
      "维修指导": {
        title: "智能维修指导",
        description: "提供详细的维修指导和建议，包括视频教程和步骤说明，帮助用户解决简单的维修问题。",
        videoUrl: "/videos/repair.mp4",
        capabilities: [
          "问题诊断",
          "维修方案生成",
          "视频教程",
          "工具推荐"
        ]
      },
      "配件更换": {
        title: "配件更换服务",
        description: "提供配件更换建议和服务预约功能，包括配件选择、价格比较和安装指导。",
        videoUrl: "/videos/parts.mp4",
        capabilities: [
          "配件匹配",
          "价格对比",
          "预约服务",
          "安装指导"
        ]
      }
    }
  },
  "语言及多模态": {
    "交互效果": {
      "连续交互": {
        title: "自然连续对话",
        description: "支持自然、流畅的多轮对话，能够准确理解上下文并保持对话连贯性。",
        videoUrl: "/videos/continuous-interaction.mp4",
        capabilities: [
          "上下文理解",
          "多轮对话",
          "意图识别",
          "情感分析"
        ]
      },
      "多音区识别": {
        title: "多区域语音识别",
        description: "支持车内多个区域的语音识别，可以准确分辨不同座位乘客的语音指令。",
        videoUrl: "/videos/multi-zone.mp4",
        capabilities: [
          "空间音频分析",
          "说话人识别",
          "噪声消除",
          "声源定位"
        ]
      },
      "语音合成": {
        title: "自然语音合成",
        description: "采用先进的语音合成技术，提供自然、流畅的语音反馈，支持多种音色和情感表达。",
        videoUrl: "/videos/synthesis.mp4",
        capabilities: [
          "声音定制",
          "情感表达",
          "语调调整",
          "多语言支持"
        ]
      }
    },
    "智能化": {
      "上下文理解": {
        title: "深度上下文理解",
        description: "能够理解复杂的对话上下文，准确把握用户意图，提供连贯的交互体验。",
        videoUrl: "/videos/context.mp4",
        capabilities: [
          "语境分析",
          "指代消解",
          "意图跟踪",
          "知识图谱"
        ]
      },
      "意图识别": {
        title: "精准意图识别",
        description: "通过深度学习模型，准确识别用户的意图和需求，提供精准的服务响应。",
        videoUrl: "/videos/intent.mp4",
        capabilities: [
          "语义分析",
          "场景理解",
          "行为预测",
          "个性化适配"
        ]
      },
      "知识推理": {
        title: "智能知识推理",
        description: "基于大规模知识图谱，提供智能化的知识推理和问答服务。",
        videoUrl: "/videos/reasoning.mp4",
        capabilities: [
          "知识推理",
          "逻辑分析",
          "关联发现",
          "答案生成"
        ]
      }
    }
  },
  "通识闲聊": {
    "日常对话": {
      "天气查询": {
        title: "智能天气服务",
        description: "提供精准的天气预报和出行建议，支持自然语言查询和智能提醒。",
        videoUrl: "/videos/weather.mp4",
        capabilities: [
          "天气预测",
          "出行建议",
          "实时更新",
          "个性化提醒"
        ]
      },
      "新闻资讯": {
        title: "智能新闻推送",
        description: "根据用户兴趣提供个性化的新闻资讯服务，支持语音播报和内容总结。",
        videoUrl: "/videos/news.mp4",
        capabilities: [
          "内容聚合",
          "个性化推荐",
          "语音播报",
          "热点追踪"
        ]
      },
      "生活百科": {
        title: "智能知识库",
        description: "提供丰富的生活百科知识，支持自然语言查询和知识推荐。",
        videoUrl: "/videos/encyclopedia.mp4",
        capabilities: [
          "知识检索",
          "智能问答",
          "内容推荐",
          "实时更新"
        ]
      }
    },
    "娱乐互动": {
      "笑话段子": {
        title: "趣味内容推送",
        description: "提供有趣的笑话和段子，支持场景化推送和互动分享。",
        videoUrl: "/videos/jokes.mp4",
        capabilities: [
          "内容分类",
          "场景匹配",
          "互动反馈",
          "分享功能"
        ]
      },
      "智力游戏": {
        title: "车载智力游戏",
        description: "提供丰富的车载智力游戏，支持语音交互和多人参与。",
        videoUrl: "/videos/games.mp4",
        capabilities: [
          "游戏互动",
          "多人参与",
          "难度调整",
          "成就系统"
        ]
      },
      "音乐推荐": {
        title: "智能音乐服务",
        description: "基于用户喜好和场景提供个性化的音乐推荐和播放服务。",
        videoUrl: "/videos/music.mp4",
        capabilities: [
          "个性化推荐",
          "场景匹配",
          "智能电台",
          "歌单管理"
        ]
      }
    }
  }
} 