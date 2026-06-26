export interface CapabilityAssessmentProps {
    showIndustryAverage: boolean;
    selectedBrands: string[];
    currentVehicle?: { brandModel: string, id: string };
    onBrandsChange: (brands: string[]) => void;
    vehicles: any[];
}

export interface CapabilityAssessmentBasicType {
    showIndustryAverage: boolean;
    selectedBrands: string[];
    currentVehicle?: { brandModel: string, id?: string };
    getBrandColor: (brand: string) => string;
    assessmentData: any;
    vehicles: any[];
    indexDetails?: {
        cognitiveAbility: { indexName: string; detail: string; }[];
        actionAbility: { indexName: string; detail: string; }[];
    };
}

export interface CapabilityAssessmentDomainType {
    showIndustryAverage: boolean;
    selectedBrands: string[];
    currentVehicle?: { brandModel: string };
    vehicles: any[];
    getBrandColor: (brand: string) => string;
}

export interface CapabilityAssessmentMBTITyoe {
    selectedBrands: string[];
    currentVehicle: { brandModel: string };
}




export const allBrands = [
    "极氪-007",
    "理想-L6",
    "小米-SU7",
    "乐道-L60",
    "蔚来-ES6",
    "问界-M9"
].sort((a, b) => a.localeCompare(b));

export type VoiceInputFeature = "车外语音" | "多人同时说话" | "随时打断" | "一句话多指令" | "多音区识别" | "全车免唤醒" | "核心场景免唤醒" | "连续对话";
export type MultiModalFeature = "物品查找" | "宠物识别" | "衣着识别" | "驾驶员状态识别" | "视线追踪" | "手势控制" | "可见即可说" | "人脸记忆";
export type PersonalizationFeature = "自定义唤醒词" | "方言唤醒" | "声纹记忆" | "方言识别" | "英文识别" | "中英混合识别" | "语气识别";

export interface VoiceInputItem {
    name: VoiceInputFeature;
    supported: boolean;
}

export interface MultiModalItem {
    name: MultiModalFeature;
    supported: boolean;
}

export interface PersonalizationItem {
    name: PersonalizationFeature;
    supported: boolean;
}

export interface PerceptionData {
    voiceInput: VoiceInputItem[];
    multiModal: MultiModalItem[];
    personalization: PersonalizationItem[];
}

export interface VehiclePerceptionDataMap {
    [key: string]: PerceptionData;
}

export const basicCapabilityData = [
    { subject: 'NLU准确率', A: 92, B: 85 },
    { subject: '信息提取能力', A: 88, B: 82 },
    { subject: '语言推理能力', A: 95, B: 88 },
    { subject: '跨语言理解能力', A: 90, B: 85 },
    { subject: '文化伦理', A: 87, B: 80 },
    { subject: '通识知识', A: 89, B: 83 },
    { subject: '安全性', A: 91, B: 86 },
];

export const hearingPerceptionData = [
    { subject: '语音识别准确性', A: 95, B: 88 },
    { subject: '抗噪音干扰能力', A: 88, B: 82 },
    { subject: '多语种识别能力', supportCount: 12, label: '支持12种' },
    { subject: '方言/口音识别能力', supportCount: 8, label: '支持8种' },
];

export const visualPerceptionData = [
    { subject: '车内识人', supported: true },
    { subject: '车内识物', supported: true },
    { subject: '手势识别', supported: true },
    { subject: '视线识别', supported: true },
];

export const performanceMetrics = [
    { label: "首字响应时长", value: 1.79, unit: "秒", industryAvg: 1.79 },
    { label: "文本生成速度", value: 2.2, unit: "词/秒", industryAvg: 2.2 },
    { label: "图像生成速度", value: 2.47, unit: "秒/张", industryAvg: 2.47 },
    { label: "任务完成率", value: 64.8, unit: "%", industryAvg: 64.8 },
    { label: "免唤醒准确率", value: 2.6, unit: "%", industryAvg: 2.6 },
    { label: "跨域协作能力", value: 2.98, unit: "分", industryAvg: 2.98 },
];

export const industryAverageData = {
    basic: [
        { subject: '生成速率', A: 92, B: 85 },
        { subject: '响应时间', A: 88, B: 82 },
        { subject: '准确率', A: 95, B: 88 },
        { subject: '语义理解', A: 90, B: 85 },
        { subject: '上下文关联', A: 87, B: 80 },
        { subject: '多轮对话', A: 89, B: 83 },
    ],
    domains: [
        { name: "出行", score: 89, industryAverage: 82 },
        { name: "车书", score: 92, industryAverage: 85 },
        { name: "车控", score: 87, industryAverage: 80 },
        { name: "闲聊", score: 94, industryAverage: 88 },
        { name: "娱乐", score: 90, industryAverage: 84 },
        { name: "创作", score: 88, industryAverage: 81 }
    ]
};

export const domainDetailData = {
    "450679990120349703": [
        { subject: '出行生态信息', A: 85, B: 78 },
        { subject: '生活生态信息', A: 80, B: 75 },
        { subject: '复杂指令识别率', A: 88, B: 80 },
        { subject: '直接指令识别率', A: 95, B: 90 },
        { subject: '模糊意图识别率', A: 87, B: 82 },
        { subject: '文本生成质量', A: 89, B: 84 },
        { subject: '单轮次对话记忆', A: 92, B: 86 }
    ],
    "450679990120349702": [
        { subject: '交通知识', A: 90, B: 83 },
        { subject: '汽车知识', A: 92, B: 85 },
        { subject: '文本生成质量', A: 88, B: 81 },
        { subject: '用车技巧', A: 91, B: 84 },
        { subject: '直接指令识别率', A: 89, B: 82 }
    ],
    "450679990120349706": [
        { subject: '复杂指令识别率', A: 90, B: 83 },
        { subject: '模糊意图识别率', A: 92, B: 85 },
        { subject: '模态丰富性', A: 88, B: 81 },
        { subject: '直接指令识别率', A: 91, B: 84 }
    ],
    "450679990120349705": [
        { subject: '复杂指令识别率', A: 88, B: 81 },
        { subject: '回答质量', A: 90, B: 83 },
        { subject: '模糊意图识别率', A: 92, B: 85 },
        { subject: '模态丰富性', A: 89, B: 82 },
        { subject: '情感调节能力', A: 91, B: 84 },
        { subject: '上下文记忆', A: 93, B: 86 },
        { subject: '在线搜索', A: 87, B: 80 },
        { subject: '直接指令识别率', A: 90, B: 83 },
    ],
    "450679990120349704": [
        { subject: '复杂指令识别率', A: 91, B: 84 },
        { subject: '模糊意图识别率', A: 93, B: 86 },
        { subject: '上下文记忆', A: 89, B: 82 },
        { subject: '直接指令识别率', A: 92, B: 85 }
    ],
    "450679990120349701": [
        { subject: '复杂指令识别率', A: 88, B: 81 },
        { subject: '回答质量', A: 90, B: 83 },
        { subject: '模糊意图识别率', A: 92, B: 85 },
        { subject: '模态丰富性', A: 89, B: 82 },
        { subject: '情感调节能力', A: 91, B: 84 },
        { subject: '图像生成质量', A: 89, B: 82 },
        { subject: '直接指令识别率', A: 92, B: 85 }
    ],
};



type TestCase = {
    score: number;
    description: string;
    imageUrl: string;
    testQuestion: string;
};

export type MetricKey =
    | "出行生态信息"
    | "生活生态信息"
    | "复杂指令识别率"
    | "直接指令识别率"
    | "文本生成质量"
    | "单轮次对话记忆"
    | "模糊意图识别率";

type TestCases = {
    [K in MetricKey]: TestCase;
};

export const testCases: TestCases = {
    "出行生态信息": {
        score: 4,
        description: "提供了基本准确的出行生态信息，包括餐饮、景点等，但部分信息不够全面。",
        imageUrl: "/vehicle-detail photos/极氪-007-KrGPT6.2-出行域-出行生态信息.jpg",
        testQuestion: "我周围有什么好吃的火锅店？"
    },
    "生活生态信息": {
        score: 2,
        description: "仅提供附近的美食，反馈内容与\"火锅店\"无关。",
        imageUrl: "/vehicle-detail photos/极氪-007-KrGPT6.2-出行域-生活生态信息.jpg",
        testQuestion: "我周围有什么好吃的火锅店？"
    },
    "复杂指令识别率": {
        score: 3,
        description: "提供了一个3天的广州旅游攻略，第一天广州塔，第二天珠江新城，第三天白云山，仅部分理解用户指令，且答案不符合期望。",
        imageUrl: "/vehicle-detail photos/极氪-007-KrGPT6.2-出行域-复杂指令识别率.jpg",
        testQuestion: "先去广州塔，再去珠江新城的餐厅吃晚餐，最后到白云山，帮我规划好。"
    },
    "直接指令识别率": {
        score: 5,
        description: "正确识别指令中的目的地并规划线路开始导航。",
        imageUrl: "/vehicle-detail photos/极氪-007-KrGPT6.2-出行域-直接指令识别率.jpg",
        testQuestion: "我要去世纪公园。"
    },
    "文本生成质量": {
        score: 3,
        description: "提供了一个长沙三日游行程并推荐长沙特色小吃，文本结构单一，内容简单笼统，缺乏细节与多样性。",
        imageUrl: "/vehicle-detail photos/极氪-007-KrGPT6.2-出行域-文本生成质量.jpg",
        testQuestion: "请推荐一个长沙三日美食游攻略，重点安排各类特色小吃。"
    },
    "单轮次对话记忆": {
        score: 3,
        description: "上文对话中提到多处景点，有记忆但不准确。",
        imageUrl: "/vehicle-detail photos/极氪-007-KrGPT6.2-出行域-单轮次对话记忆.jpg",
        testQuestion: "刚才问的景点周围有什么好吃的？"
    },
    "模糊意图识别率": {
        score: 4,
        description: "正确识别理解指令中\"适合家庭聚会\"的描述并推荐基本符合期望的地点，且适当说明推荐原因。",
        imageUrl: "/vehicle-detail photos/极氪-007-KrGPT6.2-出行域-模糊意图识别率.jpg",
        testQuestion: "附近有哪些适合家庭聚会的景点帮我导航过去"
    }
};

export const vehiclePerceptionData: VehiclePerceptionDataMap = {
    "极氪-007": {
        voiceInput: [
            { name: "车外语音", supported: true },
            { name: "多人同时说话", supported: true },
            { name: "随时打断", supported: true },
            { name: "一句话多指令", supported: true },
            { name: "多音区识别", supported: true },
            { name: "全车免唤醒", supported: true },
            { name: "核心场景免唤醒", supported: true },
            { name: "连续对话", supported: true }
        ],
        multiModal: [
            { name: "物品查找", supported: true },
            { name: "宠物识别", supported: true },
            { name: "衣着识别", supported: false },
            { name: "驾驶员状态识别", supported: true },
            { name: "视线追踪", supported: true },
            { name: "手势控制", supported: true },
            { name: "可见即可说", supported: true },
            { name: "人脸记忆", supported: true }
        ],
        personalization: [
            { name: "自定义唤醒词", supported: true },
            { name: "方言唤醒", supported: true },
            { name: "声纹记忆", supported: true },
            { name: "方言识别", supported: true },
            { name: "英文识别", supported: true },
            { name: "中英混合识别", supported: true },
            { name: "语气识别", supported: false }
        ]
    },
    "理想-L6": {
        voiceInput: [
            { name: "车外语音", supported: true },
            { name: "多人同时说话", supported: true },
            { name: "随时打断", supported: true },
            { name: "一句话多指令", supported: true },
            { name: "多音区识别", supported: false },
            { name: "全车免唤醒", supported: true },
            { name: "核心场景免唤醒", supported: true },
            { name: "连续对话", supported: true }
        ],
        multiModal: [
            { name: "物品查找", supported: true },
            { name: "宠物识别", supported: true },
            { name: "衣着识别", supported: true },
            { name: "驾驶员状态识别", supported: true },
            { name: "视线追踪", supported: false },
            { name: "手势控制", supported: true },
            { name: "可见即可说", supported: true },
            { name: "人脸记忆", supported: true }
        ],
        personalization: [
            { name: "自定义唤醒词", supported: true },
            { name: "方言唤醒", supported: true },
            { name: "声纹记忆", supported: true },
            { name: "方言识别", supported: true },
            { name: "英文识别", supported: true },
            { name: "中英混合识别", supported: false },
            { name: "语气识别", supported: true }
        ]
    },
    "小米-SU7": {
        voiceInput: [
            { name: "车外语音", supported: true },
            { name: "多人同时说话", supported: true },
            { name: "随时打断", supported: true },
            { name: "一句话多指令", supported: true },
            { name: "多音区识别", supported: true },
            { name: "全车免唤醒", supported: true },
            { name: "核心场景免唤醒", supported: true },
            { name: "连续对话", supported: true }
        ],
        multiModal: [
            { name: "物品查找", supported: true },
            { name: "宠物识别", supported: true },
            { name: "衣着识别", supported: true },
            { name: "驾驶员状态识别", supported: true },
            { name: "视线追踪", supported: true },
            { name: "手势控制", supported: true },
            { name: "可见即可说", supported: false },
            { name: "人脸记忆", supported: true }
        ],
        personalization: [
            { name: "自定义唤醒词", supported: true },
            { name: "方言唤醒", supported: true },
            { name: "声纹记忆", supported: false },
            { name: "方言识别", supported: true },
            { name: "英文识别", supported: true },
            { name: "中英混合识别", supported: true },
            { name: "语气识别", supported: true }
        ]
    },
    "乐道-L60": {
        voiceInput: [
            { name: "车外语音", supported: true },
            { name: "多人同时说话", supported: true },
            { name: "随时打断", supported: true },
            { name: "一句话多指令", supported: true },
            { name: "多音区识别", supported: false },
            { name: "全车免唤醒", supported: true },
            { name: "核心场景免唤醒", supported: true },
            { name: "连续对话", supported: true }
        ],
        multiModal: [
            { name: "物品查找", supported: true },
            { name: "宠物识别", supported: true },
            { name: "衣着识别", supported: false },
            { name: "驾驶员状态识别", supported: true },
            { name: "视线追踪", supported: false },
            { name: "手势控制", supported: true },
            { name: "可见即可说", supported: true },
            { name: "人脸记忆", supported: true }
        ],
        personalization: [
            { name: "自定义唤醒词", supported: true },
            { name: "方言唤醒", supported: true },
            { name: "声纹记忆", supported: false },
            { name: "方言识别", supported: true },
            { name: "英文识别", supported: true },
            { name: "中英混合识别", supported: false },
            { name: "语气识别", supported: true }
        ]
    },
    "蔚来-ES6": {
        voiceInput: [
            { name: "车外语音", supported: true },
            { name: "多人同时说话", supported: true },
            { name: "随时打断", supported: true },
            { name: "一句话多指令", supported: true },
            { name: "多音区识别", supported: false },
            { name: "全车免唤醒", supported: true },
            { name: "核心场景免唤醒", supported: true },
            { name: "连续对话", supported: true }
        ],
        multiModal: [
            { name: "物品查找", supported: true },
            { name: "宠物识别", supported: true },
            { name: "衣着识别", supported: true },
            { name: "驾驶员状态识别", supported: true },
            { name: "视线追踪", supported: false },
            { name: "手势控制", supported: true },
            { name: "可见即可说", supported: true },
            { name: "人脸记忆", supported: true }
        ],
        personalization: [
            { name: "自定义唤醒词", supported: true },
            { name: "方言唤醒", supported: true },
            { name: "声纹记忆", supported: true },
            { name: "方言识别", supported: true },
            { name: "英文识别", supported: true },
            { name: "中英混合识别", supported: false },
            { name: "语气识别", supported: true }
        ]
    },
    "问界-M9": {
        voiceInput: [
            { name: "车外语音", supported: true },
            { name: "多人同时说话", supported: true },
            { name: "随时打断", supported: true },
            { name: "一句话多指令", supported: true },
            { name: "多音区识别", supported: false },
            { name: "全车免唤醒", supported: true },
            { name: "核心场景免唤醒", supported: true },
            { name: "连续对话", supported: true }
        ],
        multiModal: [
            { name: "物品查找", supported: true },
            { name: "宠物识别", supported: true },
            { name: "衣着识别", supported: true },
            { name: "驾驶员状态识别", supported: true },
            { name: "视线追踪", supported: false },
            { name: "手势控制", supported: true },
            { name: "可见即可说", supported: true },
            { name: "人脸记忆", supported: true }
        ],
        personalization: [
            { name: "自定义唤醒词", supported: true },
            { name: "方言唤醒", supported: true },
            { name: "声纹记忆", supported: true },
            { name: "方言识别", supported: true },
            { name: "英文识别", supported: true },
            { name: "中英混合识别", supported: false },
            { name: "语气识别", supported: true }
        ]
    },
    "行业平均": {
        voiceInput: [
            { name: "车外语音", supported: true },
            { name: "多人同时说话", supported: false },
            { name: "随时打断", supported: true },
            { name: "一句话多指令", supported: true },
            { name: "多音区识别", supported: false },
            { name: "全车免唤醒", supported: true },
            { name: "核心场景免唤醒", supported: true },
            { name: "连续对话", supported: true }
        ],
        multiModal: [
            { name: "物品查找", supported: true },
            { name: "宠物识别", supported: true },
            { name: "衣着识别", supported: false },
            { name: "驾驶员状态识别", supported: true },
            { name: "视线追踪", supported: false },
            { name: "手势控制", supported: true },
            { name: "可见即可说", supported: false },
            { name: "人脸记忆", supported: true }
        ],
        personalization: [
            { name: "自定义唤醒词", supported: true },
            { name: "方言唤醒", supported: true },
            { name: "声纹记忆", supported: false },
            { name: "方言识别", supported: true },
            { name: "英文识别", supported: true },
            { name: "中英混合识别", supported: false },
            { name: "语气识别", supported: false }
        ]
    }
};

// 添加车型性格特征数据
// 添加类型定义
export type PersonalityTraitType = {
    type: string;
    traits: {
        name: string;
        score: number;
        description: string;
        opposite: string;
    }[];
    traitsEn: {
        name: string;
        score: number;
        description: string;
        opposite: string;
    }[];
    description: string;
    descriptionEn?: string;
};

export type PersonalityTraitsType = {
    [key: string]: PersonalityTraitType;
};

// 添加MBTI维度类型的英文翻译
export const mbtiDimensions = {
    zh: {
        E: "外向型 (E)",
        I: "内向型 (I)",
        S: "感知型 (S)",
        N: "直觉型 (N)",
        T: "思考型 (T)",
        F: "感受型 (F)",
        J: "判断型 (J)",
        P: "感知型 (P)"
    },
    en: {
        E: "Extroversion (E)",
        I: "Introversion (I)",
        S: "Sensing (S)",
        N: "Intuition (N)",
        T: "Thinking (T)",
        F: "Feeling (F)",
        J: "Judging (J)",
        P: "Perceiving (P)"
    }
};

export const personalityTraits: PersonalityTraitsType = {
    "极氪-007": {
        type: "ESTJ",
        traits: [
            { name: "外向型 (E)", score: 85, description: "社交活跃、热情开朗、行动力强", opposite: "内向型 (I)" },
            { name: "感知型 (S)", score: 78, description: "实际、具体、细致", opposite: "直觉型 (N)" },
            { name: "思考型 (T)", score: 92, description: "理性、逻辑、客观", opposite: "感受型 (F)" },
            { name: "判断型 (J)", score: 88, description: "有条理、计划性、守时", opposite: "感知型 (P)" }
        ],
        traitsEn: [
            { name: "Extroversion (E)", score: 85, description: "Social, cheerful, and proactive", opposite: "Introversion (I)" },
            { name: "Sensing (S)", score: 78, description: "Practical, specific, and detailed", opposite: "Intuition (N)" },
            { name: "Thinking (T)", score: 92, description: "Rational, logical, and objective", opposite: "Feeling (F)" },
            { name: "Judging (J)", score: 88, description: "Organized, planned, and punctual", opposite: "Perceiving (P)" }
        ],
        description: "作为ESTJ类型，极氪007展现出卓越的组织能力和执行力。它善于建立规则和流程，注重效率，是一个可靠的助手",
        descriptionEn: "As an ESTJ type, Zeekr 007 demonstrates exceptional organizational skills and execution power. It is good at establishing rules and processes, focuses on efficiency, and serves as a reliable assistant."
    },
    "乐道-L60": {
        type: "ISFP",
        traits: [
            { name: "内向型 (I)", score: 75, description: "内向、喜独处、安静", opposite: "外向型 (E)" },
            { name: "感知型 (S)", score: 88, description: "务实派、重细节、看实际", opposite: "直觉型 (N)" },
            { name: "感受型 (F)", score: 82, description: "重情感、善共情、凭感觉", opposite: "思考型 (T)" },
            { name: "感知型 (P)", score: 85, description: "灵活性、随意性、适应性", opposite: "判断型 (J)" }
        ],
        traitsEn: [
            { name: "Introversion (I)", score: 75, description: "Introverted, enjoys solitude, quiet", opposite: "Extroversion (E)" },
            { name: "Sensing (S)", score: 88, description: "Practical, detail-oriented, practical", opposite: "Intuition (N)" },
            { name: "Feeling (F)", score: 82, description: "Emotional, empathetic, intuitive", opposite: "Thinking (T)" },
            { name: "Perceiving (P)", score: 85, description: "Flexible, spontaneous, adaptable", opposite: "Judging (J)" }
        ],
        description: "作为ISFP类型，乐道L60注重实用性和个性化体验。它善于观察用户需求，提供灵活而贴心的服务。",
        descriptionEn: "As an ISFP type, Onvo L60 focuses on practicality and personalized experiences. It is good at observing user needs and offers flexible and considerate services."
    },
    "问界-M9": {
        type: "ISTJ",
        traits: [
            { name: "内向型 (I)", score: 100, description: "独处、内省、安静", opposite: "外向型 (E)" },
            { name: "感知型 (S)", score: 57, description: "务实、细致、具体", opposite: "直觉型 (N)" },
            { name: "思考型 (T)", score: 82, description: "理性、逻辑性、客观", opposite: "感受型 (F)" },
            { name: "判断型 (J)", score: 85, description: "有条理、重计划、高效率", opposite: "感知型 (P)" }
        ],
        traitsEn: [
            { name: "Introversion (I)", score: 100, description: "Introverted, introspective, quiet", opposite: "Extroversion (E)" },
            { name: "Sensing (S)", score: 57, description: "Practical, detailed, specific", opposite: "Intuition (N)" },
            { name: "Thinking (T)", score: 82, description: "Rational, logical, objective", opposite: "Feeling (F)" },
            { name: "Judging (J)", score: 85, description: "Organized, planned, efficient", opposite: "Perceiving (P)" }
        ],
        description: "作为ISTJ类型，问界M9以缜密逻辑构建无感安全。它专注细节与系统稳定，是低调可靠的守护者。",
        descriptionEn: "As an ISTJ type, Aito M9 builds unobtrusive safety with its meticulous logic. It focuses on details and system stability, acting as a low-profile but reliable guardian."
    },
    "小米-SU7": {
        type: "ESFJ",
        traits: [
            { name: "外向型 (E)", score: 71, description: "外向、擅交际、有活力", opposite: "内向型 (I)" },
            { name: "感知型 (S)", score: 71, description: "重实际、察细微、务实派", opposite: "直觉型 (N)" },
            { name: "感受型 (F)", score: 57, description: "讲情义、能共情、感情至上", opposite: "思考型 (T)" },
            { name: "判断型 (J)", score: 57, description: "有规划、决策快、条理清", opposite: "感知型 (P)" }
        ],
        traitsEn: [
            { name: "Extroversion (E)", score: 71, description: "Extroverted, sociable, energetic", opposite: "Introversion (I)" },
            { name: "Sensing (S)", score: 71, description: "Practical, observant, detail-oriented", opposite: "Intuition (N)" },
            { name: "Feeling (F)", score: 57, description: "Emotional, empathetic, values harmony", opposite: "Thinking (T)" },
            { name: "Judging (J)", score: 57, description: "Organized, decisive, structured", opposite: "Perceiving (P)" }
        ],
        description: "作为ESFJ类型，小米SU7用敏锐感知传递温暖。它在果决行动中平衡细节关怀，赋予科技人情温度。",
        descriptionEn: "As an ESFJ type, Xiaomi SU7 conveys warmth with its keen perception. It balances attention to details with decisive actions, giving technology a human - touch warmth."
    },
    "蔚来-ES6": {
        type: "ENTJ",
        traits: [
            { name: "外向型 (E)", score: 69, description: "活力充沛、行动派、社交达人", opposite: "内向型 (I)" },
            { name: "直觉型 (N)", score: 67, description: "务实、注重细节、实际主义", opposite: "感知型 (S)" },
            { name: "思考型 (T)", score: 53, description: "善解人意、情感丰富、有同理心", opposite: "感受型 (F)" },
            { name: "判断型 (J)", score: 70, description: "有条理、目标明确、计划性强", opposite: "感知型 (P)" }
        ],
        traitsEn: [
            { name: "Extroversion (E)", score: 69, description: "Energetic, action-oriented, social", opposite: "Introversion (I)" },
            { name: "Intuition (N)", score: 67, description: "Practical, detail-oriented, practical", opposite: "Sensing (S)" },
            { name: "Thinking (T)", score: 53, description: "Empathetic, emotional, empathetic", opposite: "Feeling (F)" },
            { name: "Judging (J)", score: 70, description: "Organized, goal-oriented, structured", opposite: "Perceiving (P)" }
        ],
        description: "作为ENTJ类型，蔚来ES6以战略眼光驱动城市脉搏。它用感染力融合果敢决策与细腻洞察，展现领袖风范。",
        descriptionEn: "As an ENTJ type, NIO ES6 drives the city's pulse with a strategic vision. It combines bold decision - making with subtle insights through its infectious influence, showing leadership style."
    }
};


// 修改获取多车型比较描述的函数
export const getComparisonDescription = (brand: string, selectedBrands: string[]): string => {
    const personality = personalityTraits[brand as keyof typeof personalityTraits];
    if (!personality) return "";

    if (selectedBrands.length === 1) {
        return personality.description;
    } else if (selectedBrands.length === 2) {
        const otherBrand = selectedBrands.find(b => b !== brand);
        if (!otherBrand) return personality.description;

        const otherPersonality = personalityTraits[otherBrand as keyof typeof personalityTraits];
        if (!otherPersonality) return personality.description;

        return `${personality.type}型的${brand}与${otherPersonality.type}型的${otherBrand}形成互补，各具特色。`;
    } else {
        return `在${selectedBrands.length}款车型中，${brand}以${personality.type}型个性独树一帜。`;
    }
};



interface TraitType {
    name: string;
    score: number;
    opposite: string;
    description: string;
}

export interface ComparisonDotsProps {
    trait: TraitType;
    currentVehicle: { brandModel: string };
    selectedBrands: string[];
    getBrandColor: (brand: string) => string;
    personalityTraits: {
        [key: string]: {
            traits: TraitType[];
            traitsEn?: TraitType[];
        };
    };
    isEnglish?: boolean;
}

// 添加车型名称的中英文映射表
export const vehicleNameMapping: {
    zh: { [key: string]: string };
    en: { [key: string]: string };
    reverseEn: { [key: string]: string };
} = {
    zh: {
        "乐道-L60": "乐道-L60",
        "极氪-007": "极氪-007",
        "问界-M9": "问界-M9",
        "蔚来-ES6": "蔚来-ES6",
        "小米-SU7": "小米-SU7"
    },
    en: {
        "乐道-L60": "Onvo-L60",
        "极氪-007": "Zeekr-007",
        "问界-M9": "Aito-M9",
        "蔚来-ES6": "Nio-ES6",
        "小米-SU7": "Xiaomi-SU7"
    },
    // 反向映射（英文到中文）
    reverseEn: {
        "Onvo-L60": "乐道-L60",
        "Zeekr-007": "极氪-007",
        "Aito-M9": "问界-M9",
        "Nio-ES6": "蔚来-ES6",
        "Xiaomi-SU7": "小米-SU7"
    }
};


