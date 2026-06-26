'use client';

import { motion } from 'framer-motion';
import Image from 'next/image';
import { cn } from "@/lib/utils";
import { Header } from "@/components/header";
import { Footer } from "@/components/footer";
import { useLanguage } from "@/constants/language";
import { translations } from "@/language";

interface FeatureSection {
    title: string;
    description: string;
    features: {
        title: string;
        description: string;
        gif?: string;
        reverse?: boolean;
    }[];
}

export default function Page() {
    const { language } = useLanguage();
    const trans = translations[language] as any;

    // 根据当前语言动态构建features数组
    const features: FeatureSection[] = [
        {
            title: language === 'en' ? "Comprehensive Vehicle Database" : "全景车型数据库",
            description: language === 'en'
                ? "Comprehensive coverage of mainstream intelligent vehicle brands, providing in-depth analysis and measured data"
                : "全面覆盖主流智能汽车品牌，提供深度分析和实测数据",
            features: [
                {
                    title: language === 'en' ? "Complete Data Coverage" : "全面数据覆盖",
                    description: language === 'en'
                        ? "_Coverage of mainstream intelligent vehicle brands, providing comprehensive and reliable evaluation data_\n\n1. Vehicle Coverage: Covering 20+ mainstream intelligent vehicle models, regularly updated dynamic data tracking\n\n2. Evaluation Dimensions: Evaluation from three dimensions of parameter configuration, function implementation, and measured performance to ensure data reliability\n\n3. Data Updates: Regular updates of vehicle model data, timely iteration follow-up, dynamic display of intelligent development trends"
                        : "_覆盖主流智能汽车品牌，提供全面可靠的评测数据_\n\n1. 车型覆盖：涵盖20+主流品牌智能车型，定期更新动态数据追踪\n\n2. 评测维度：从参数配置、功能实现、实测表现三个维度进行评估，确保数据可靠性\n\n3. 数据更新：定期更新车型数据，及时跟进迭代，动态展现智能化发展趋势",
                    gif: "/images/faq/车型库.gif"
                }
            ]
        },
        {
            title: language === 'en' ? "Intelligent Function Structure Tree" : "智能功能结构树",
            description: language === 'en'
                ? "Systematically display the functional architecture of intelligent vehicles, intuitively compare the characteristics of various brands"
                : "系统化展示智能汽车功能架构，直观对比各品牌特点",
            features: [
                {
                    title: language === 'en' ? "Hierarchical Breakdown" : "层级拆解",
                    description: language === 'en'
                        ? "_Systematic presentation of intelligent functions, three-level architecture for precise positioning_\n\n1. Primary Functions: Covering general chat, car assistants, audio-visual entertainment and other functions, comprehensively displaying the scope of the intelligent cockpit system\n\n2. Secondary Classification: Scene-based subdivision of each core function, such as general chat subdivided into dialogue personification, emotionalization, content richness, etc.\n\n3. Tertiary Function Points: Display specific function implementation, including function description, usage scenarios, operation guidance and other detailed information"
                        : "_智能功能体系化呈现，三级架构精准定位_\n\n1. 一级功能：涵盖通识闲聊、用车助手、影音娱乐等功能，全面展示智能座舱系统范畴\n\n2. 二级分类：对每个核心功能进行场景化细分，如通识闲聊细分为对话拟人化、情感化、内容丰富度等\n\n3. 三级功能点：展示具体功能实现，包含功能说明、使用场景、操作指引等详细信息",
                    gif: "/images/faq/层级拆解.gif"
                },
                {
                    title: language === 'en' ? "Video Comparison" : "视频对比",
                    description: language === 'en'
                        ? "_Multi-dimensional function video comparison, intuitive display of product differences_\n\n1. Function Implementation: Automatically correlate different brands horizontally, showing the implementation methods and completion degrees of different vehicle models on the same function\n\n2. Interactive Experience: Compare operation process, response speed, interaction design and other user experience elements\n\n3. Performance: Actual scenario demonstration, comparing the performance of various brands in terms of accuracy, stability, intelligence level, etc."
                        : "_多维度功能视频对比，直观展示产品差异_\n\n1. 功能实现：自动横向关联不同品牌，展示不同车型在相同功能上的实现方式和完成度\n\n2. 交互体验：对比操作流程、响应速度、交互设计等用户体验要素\n\n3. 性能表现：实际场景演示，对比各品牌在准确性、稳定性、智能化水平等方面的表现",
                    gif: "/images/faq/视频对比.gif",
                    reverse: true
                },
                {
                    title: language === 'en' ? "Trend Tracking" : "趋势追踪",
                    description: language === 'en'
                        ? "_Function penetration rate, brand heat map, function comparison details, making trends at a glance_\n\n1. Function Penetration Rate: Comparison of the current status and trends of different brand vehicle models in the application of large models\n\n2. Brand Heat Map: Intuitive presentation of the focus of different brands on different functions\n\n3. Detailed Function Comparison: Breakdown by function level (1/2/3 levels), providing function rating (good/avg/poor/n.a.) and video interpretation"
                        : "_功能渗透率、品牌热力图、功能对比详解，让趋势一目了然_\n\n1. 功能渗透率：不同品牌车型在大模型应用上的配置现状对比及趋势展现\n\n2. 品牌热力图：直观呈现各品牌不同功能上的侧重点\n\n3. 功能对比详细：按功能层级（1/2/3级）拆解，提供功能评级（good/avg/poor/n.a.）及视频解读",
                    gif: "/images/faq/趋势追踪.gif"
                },
                {
                    title: language === 'en' ? "Highlight Recommendations" : "亮点推荐",
                    description: language === 'en'
                        ? "_Quick display of highlight functions, focusing on the direction of industry development_\n\n1. Selected Scenarios: Screen the most representative functional scenarios from massive test cases to showcase the innovative highlights of various brands\n\n2. Trend Insights: Keep up with industry development trends, timely discover and display emerging intelligent functions and application scenarios"
                        : "_亮点功能快速展现，聚焦行业发展方向_\n\n1. 精选场景：从海量测试用例中筛选最具代表性的功能场景，展示各品牌创新亮点\n\n2. 趋势洞察：紧跟行业发展动向，及时发现和展示新兴的智能化功能与应用场景",
                    gif: "/images/faq/亮点推荐.gif",
                    reverse: true
                }
            ]
        },
        {
            title: language === 'en' ? "Model Capability Perspective" : "模型能力透视镜",
            description: language === 'en'
                ? "In-depth analysis of the core capabilities of intelligent systems, comprehensive assessment of performance"
                : "深入分析智能系统的核心能力，全方位评估性能表现",
            features: [
                {
                    title: language === 'en' ? "Three Dimensions of Basic Capabilities" : "基础能力三维度",
                    description: language === 'en'
                        ? "_General knowledge, action, perception capability multi-vehicle comparative evaluation_\n\n1. General Cognition: Evaluate the comprehensive performance of vehicle models in knowledge understanding, language processing, logical reasoning, etc.\n\n2. Action Capability: Analyze the processing ability of vehicle models in task execution, response speed, completion rate and other scenarios\n\n3. Perception Capability: Test the recognition and processing level of vehicle models in voice, vision, multimodal interaction, etc."
                        : "_通识，行动，感知能力多车型对比评估_\n\n1. 通识认知：评估车型在知识理解、语言处理、逻辑推理等方面的综合表现\n\n2. 行动能力：分析车型在任务执行、响应速度、完成率等场景的处理能力\n\n3. 感知能力：测试车型在语音、视觉、多模态等交互方面的识别和处理水平",
                    gif: "/images/faq/基础能力三维度.gif"
                },
                {
                    title: language === 'en' ? "In-depth Analysis of Six Scenarios" : "六大场景深度解析",
                    description: language === 'en'
                        ? "_Six functional domain performance scores, multi-dimensional visualization of evaluation results_\n\n1. Data Visualization: Bar charts and radar charts intuitively present the performance scores of six functional domains, making the data at a glance\n\n2. Standard Scenario Library: Built-in 200+ standard test scenarios, providing professional case test scores and in-depth analysis\n\n3. Capability Assessment: High-score performance long-range dialogue transcripts, high-frequency problem collections reflecting the direction for model capability improvement, video presentation of large model answer highlights and shortcomings"
                        : "_六大功能域性能评分，多维度可视化展示评测结果_\n\n1. 数据可视化：柱状图和雷达图直观呈现六大功能域的性能评分，让数据一目了然\n\n2. 标准场景库：内置200+标准测试场景，提供专业案例实测评分及深度解析\n\n3. 能力评估：高分表现长程对话实录展示，高频问题集锦反映模型能力待改进方向，视频呈现大模型回答亮点及不足",
                    gif: "/images/faq/六大场景深度解析.gif",
                    reverse: true
                },
                {
                    title: language === 'en' ? "Exclusive Cockpit MBTI" : "独家座舱MBTI",
                    description: language === 'en'
                        ? "_Build innovative vehicle personality models, analyze intelligent cockpit personality characteristics_\n\n1. Personality Dimensions: Evaluate vehicle model characteristics from four dimensions: extroversion/introversion, sensing/intuition, thinking/feeling, judging/perceiving\n\n2. Personality Portraits: Construct 16 personality types such as ESTJ/INFJ, showing different interaction styles and service characteristics of different vehicle models\n\n3. Competitive Comparison: Multi-vehicle personality characteristic comparative analysis, discovering brand differentiation advantages"
                        : "_构建创新车格模型，解析智能座舱个性特征_\n\n1. 性格维度：从外向/内向、感知/直觉、思考/感受、判断/感知四个维度评估车型特征\n\n2. 个性画像：构建ESTJ/INFJ等16种性格类型，展现不同车型的交互风格和服务特点\n\n3. 竞品对比：多车型个性特征对比分析，发掘品牌差异化优势",
                    gif: "/images/faq/独家座舱MBTI.gif"
                }
            ]
        },
        {
            title: language === 'en' ? "Multidimensional Capability Ranking" : "多维能力排行榜",
            description: language === 'en'
                ? "Comprehensive scoring system, helping you understand the intelligence ranking of various brands"
                : "全面的评分体系，助您了解各品牌智能化排行",
            features: [
                {
                    title: language === 'en' ? "Basic Capability + Scenario-specific Rankings" : "基础能力+场景专项榜",
                    description: language === 'en'
                        ? "_Multi-dimensional evaluation system, precisely positioning brand advantages_\n\nFrom basic capability dimensions to travel, vehicle knowledge, vehicle control, casual chat, entertainment, creation six functional domains, comprehensive evaluation of vehicle intelligence level, helping users understand the different capability rankings of various brands"
                        : "_多维度评估体系，精准定位品牌优势_\n\n从基础能力维度到出行、车书、车控、闲聊、娱乐、创作六大功能域，全方位评测车型智能化水平，帮助用户了解各品牌不同能力排行",
                    gif: "/images/faq/基础能力+场景专项榜.gif",
                    reverse: true
                }
            ]
        }
    ];

    return (
        <>
            <Header />
            <main className="min-h-screen bg-[#0D1117] pt-20">
                {/* Hero Section */}
                <section className="relative py-16 overflow-hidden">
                    <div className="container mx-auto px-4">
                        <div className="text-center max-w-4xl mx-auto">
                            <motion.h1
                                className="text-4xl sm:text-5xl font-bold bg-gradient-to-r from-amber-200 to-amber-500 text-transparent bg-clip-text mb-6"
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.5 }}
                            >
                                {language === 'en' ? 'Learn about BeeEval Features' : '了解BeeEval的功能特性'}
                            </motion.h1>
                            <motion.p
                                className="text-lg text-gray-300 mb-8"
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.5, delay: 0.1 }}
                            >
                                {language === 'en'
                                    ? 'Explore our platform\'s core functions and learn how to better use BeeEval'
                                    : '探索我们平台的核心功能，了解如何更好地使用BeeEval'}
                            </motion.p>
                        </div>
                    </div>
                </section>

                {/* Features Section */}
                <section className="py-16">
                    <div className="container mx-auto px-4">
                        <div className="grid gap-32">
                            {features.map((section, index) => (
                                <motion.div
                                    key={section.title}
                                    className="bg-[#1C2028]/80 rounded-2xl overflow-hidden border border-gray-800/50"
                                    initial={{ opacity: 0, y: 40 }}
                                    whileInView={{ opacity: 1, y: 0 }}
                                    viewport={{ once: true }}
                                    transition={{ duration: 0.6, delay: index * 0.1 }}
                                >
                                    {/* 主标题和描述 */}
                                    <div className="bg-[#151922]/80 p-8 border-b border-gray-800/50">
                                        <div className="text-center max-w-4xl mx-auto">
                                            <motion.h2
                                                className="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-amber-200 to-amber-500 text-transparent bg-clip-text mb-4 tracking-tight"
                                                initial={{ opacity: 0, y: 20 }}
                                                whileInView={{ opacity: 1, y: 0 }}
                                                viewport={{ once: true }}
                                            >
                                                {section.title}
                                            </motion.h2>
                                            <motion.p
                                                className="text-lg text-gray-300 max-w-2xl mx-auto"
                                                initial={{ opacity: 0, y: 20 }}
                                                whileInView={{ opacity: 1, y: 0 }}
                                                viewport={{ once: true }}
                                                transition={{ delay: 0.1 }}
                                            >
                                                {section.description}
                                            </motion.p>
                                        </div>
                                    </div>

                                    {/* 功能特性内容 */}
                                    <div className="p-8">
                                        <div className="grid gap-12">
                                            {section.features.map((feature, featureIndex) => (
                                                <motion.div
                                                    key={feature.title}
                                                    className={cn(
                                                        "grid grid-cols-1 lg:grid-cols-2 gap-12 items-center bg-[#151922]/30 rounded-xl p-8",
                                                        feature.reverse && "lg:[&>*:first-child]:order-2 lg:[&>*:last-child]:order-1"
                                                    )}
                                                    initial={{ opacity: 0, y: 20 }}
                                                    whileInView={{ opacity: 1, y: 0 }}
                                                    viewport={{ once: true }}
                                                    transition={{ duration: 0.4, delay: featureIndex * 0.1 }}
                                                >
                                                    {/* 文字部分 */}
                                                    <div className="flex flex-col justify-center">
                                                        <div className="bg-[#1C2028]/60 rounded-lg p-6 backdrop-blur-sm border border-gray-800/50">
                                                            <h3 className="text-xl font-bold text-amber-400 mb-3">{feature.title}</h3>
                                                            {feature.description.startsWith('_') ? (
                                                                <>
                                                                    <p className="text-base text-gray-400 italic mb-4">
                                                                        {feature.description.split('\n\n')[0].replace(/_/g, '')}
                                                                    </p>
                                                                    {feature.description.split('\n\n').slice(1).map((paragraph, index) => (
                                                                        <p key={index} className="text-base text-gray-300 leading-relaxed mb-3">
                                                                            {paragraph}
                                                                        </p>
                                                                    ))}
                                                                </>
                                                            ) : (
                                                                <p className="text-base text-gray-300 leading-relaxed">
                                                                    {feature.description}
                                                                </p>
                                                            )}
                                                        </div>
                                                    </div>

                                                    {/* 图片/GIF部分 */}
                                                    {feature.gif && (
                                                        <div className="flex items-center justify-center">
                                                            <div className="relative aspect-[16/10] w-full rounded-xl overflow-hidden border border-amber-500/20 shadow-lg shadow-amber-500/10">
                                                                <Image
                                                                    src={feature.gif}
                                                                    alt={`${feature.title} ${language === 'en' ? 'Demo' : '演示'}`}
                                                                    fill
                                                                    className="object-cover"
                                                                />
                                                            </div>
                                                        </div>
                                                    )}
                                                </motion.div>
                                            ))}
                                        </div>
                                    </div>
                                </motion.div>
                            ))}
                        </div>
                    </div>
                </section>
            </main>
            <Footer />
        </>
    );
}