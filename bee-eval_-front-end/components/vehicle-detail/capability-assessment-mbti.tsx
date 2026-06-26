'use client'

import { TabsContent } from "@/components/ui/tabs"
import { Card, CardContent, } from "@/components/ui/card"
import React from 'react'
import { CapabilityAssessmentMBTITyoe, personalityTraits, ComparisonDotsProps, vehicleNameMapping } from './capability-assessment-data'
import { cn } from "@/lib/utils"
import { Badge } from "@/components/ui/badge"
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import { Progress } from "@/components/ui/progress"

// 添加 MBTI 维度的中英文说明
const mbtiDimensions = {
    en: {
        E: { title: 'Extroversion', description: 'Tends to focus on the outer world of people and activities' },
        I: { title: 'Introversion', description: 'Tends to focus on the inner world of ideas and impressions' },
        S: { title: 'Sensing', description: 'Tends to focus on basic information, what is real' },
        N: { title: 'Intuition', description: 'Tends to focus on interpreting and adding meaning' },
        T: { title: 'Thinking', description: 'Tends to make decisions based on logic and analysis' },
        F: { title: 'Feeling', description: 'Tends to make decisions based on values and harmony' },
        J: { title: 'Judging', description: 'Tends to prefer a planned, organized approach' },
        P: { title: 'Perceiving', description: 'Tends to prefer a flexible, spontaneous approach' }
    },
    zh: {
        E: { title: '外向', description: '倾向于关注外部世界、人和活动' },
        I: { title: '内向', description: '倾向于关注内心世界、想法和印象' },
        S: { title: '实感', description: '倾向于关注基本信息和现实' },
        N: { title: '直觉', description: '倾向于解释和赋予意义' },
        T: { title: '思考', description: '倾向于基于逻辑和分析做决定' },
        F: { title: '情感', description: '倾向于基于价值观和和谐做决定' },
        J: { title: '判断', description: '倾向于计划性和有组织的方法' },
        P: { title: '感知', description: '倾向于灵活和自发的方法' }
    }
} as const;

const ComparisonDots: React.FC<ComparisonDotsProps> = ({
    trait,
    currentVehicle,
    selectedBrands,
    getBrandColor,
    personalityTraits,
    isEnglish
}) => {
    const { language } = useLanguage();

    // 根据当前语言获取对应的traits数据
    const getTraits = (brand: string) => {
        const personality = personalityTraits[brand];
        if (!personality) return [];
        return isEnglish && personality.traitsEn ? personality.traitsEn : personality.traits;
    };

    // 获取显示名称函数
    const getDisplayName = (name: string): string => {
        return isEnglish ? (vehicleNameMapping.en[name] || name) : name;
    };

    // 从特征名称中提取MBTI字母
    const extractMbtiLetter = (name: string): string => {
        // 使用正则表达式提取括号中的字母
        const match = name.match(/\(([IESNTFJP])\)/);
        return match ? match[1] : '';
    };

    // MBTI特质的固定位置：E、S、T、J 在左侧，I、N、F、P 在右侧
    const leftSideLetters = ['E', 'S', 'T', 'J'];

    return (
        <div className="relative h-12">
            {/* 背景轴线 */}
            <div className="absolute left-0 right-0 top-1/2 h-0.5 bg-gray-800/50" />

            {/* 渲染所有品牌的点，包括当前车型 */}
            {selectedBrands.map((brand) => {
                const brandTraits = getTraits(brand);
                const matchingTrait = brandTraits.find(t =>
                    t.name === trait.name || t.name === trait.opposite
                );

                if (!matchingTrait) return null;

                // 获取当前特征的MBTI字母
                const traitLetter = extractMbtiLetter(matchingTrait.name);

                // 获取当前特征的得分
                const score = matchingTrait.score;

                // 固定左右位置：E、S、T、J 始终在左侧，I、N、F、P 始终在右侧
                const isLeftSide = leftSideLetters.includes(traitLetter);

                // 计算位置
                let position;
                if (isLeftSide) {
                    // 左侧特质 (E/S/T/J)，得分越高越靠左
                    position = `${100 - score}%`;
                } else {
                    // 右侧特质 (I/N/F/P)，得分越高越靠右
                    position = `${score}%`;
                }

                const isCurrent = brand === currentVehicle.brandModel;

                // 获取显示名称
                const displayBrand = getDisplayName(brand);

                return (
                    <div
                        key={brand}
                        className="absolute top-1/2 -translate-y-1/2 transition-all duration-300 group"
                        style={{ left: position }}
                    >
                        {/* 悬停信息 - 显示在点的上方 */}
                        <div className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 opacity-0 group-hover:opacity-100 transition-opacity duration-200 pointer-events-none">
                            <div className="bg-[#1C2028] border border-gray-800 rounded-md shadow-lg p-2 whitespace-nowrap">
                                <div className="flex flex-col items-center gap-1">
                                    <span className="text-sm font-bold" style={{ color: getBrandColor(brand) }}>
                                        {score}% {matchingTrait.name.split(' ')[0]}
                                    </span>
                                    <span className="text-xs text-gray-400">
                                        {matchingTrait.description}
                                    </span>
                                </div>
                            </div>
                        </div>

                        {/* 点 */}
                        <div
                            className={cn(
                                "w-4 h-4 rounded-full shadow-lg transition-transform duration-300 hover:scale-125",
                                isCurrent ? "z-30" : "z-20"
                            )}
                            style={{ backgroundColor: getBrandColor(brand) }}
                        />

                        {/* 品牌名称 */}
                        <div
                            className="absolute -bottom-6 left-1/2 -translate-x-1/2 whitespace-nowrap text-xs"
                            style={{ color: getBrandColor(brand) }}
                        >
                            {displayBrand}
                        </div>
                    </div>
                );
            })}
        </div>
    );
};

// 修改 PersonalityDescription 组件
const PersonalityDescription: React.FC<{
    brand: string;
    personality: typeof personalityTraits[keyof typeof personalityTraits];
    getBrandColor: (brand: string) => string;
    selectedBrands: string[];
}> = ({ brand, personality, getBrandColor, selectedBrands }) => {
    const { language } = useLanguage();
    const isEnglish = language === 'en';

    // 根据当前语言获取显示的品牌名称
    const displayBrand = isEnglish ? vehicleNameMapping.en[brand] || brand : brand;

    // 获取当前语言对应的特性数据
    const traits = isEnglish && personality.traitsEn ? personality.traitsEn : personality.traits;

    return (
        <div className={cn(
            "bg-black/20 rounded-lg border border-gray-800/50",
            selectedBrands.length === 1 ? "p-8" : "p-6",
            "h-full"
        )}>
            <div className="flex flex-col h-full">
                <div className={cn(
                    "flex items-center gap-3",
                    selectedBrands.length === 1 ? "mb-6" : "mb-4"
                )}>
                    <h3 className={cn(
                        "font-bold",
                        selectedBrands.length === 1 ? "text-2xl" : "text-xl"
                    )} style={{ color: getBrandColor(brand) }}>
                        {personality.type}{isEnglish ? "" : "型人格"}
                    </h3>
                    <Badge
                        variant="outline"
                        className="border-opacity-20"
                        style={{
                            backgroundColor: `${getBrandColor(brand)}1a`,
                            borderColor: `${getBrandColor(brand)}33`,
                            color: getBrandColor(brand)
                        }}
                    >
                        {displayBrand}
                    </Badge>
                </div>

                <div className={cn(
                    selectedBrands.length === 1 ? "mb-8" : "mb-6"
                )}>
                    <p className={cn(
                        "text-gray-400 leading-relaxed",
                        selectedBrands.length === 1 ? "text-base" : "text-sm"
                    )}>
                        {isEnglish && personality.descriptionEn ? personality.descriptionEn : personality.description}
                    </p>
                </div>
            </div>
        </div>
    );
};

// 修改 PersonalityTraitScore 组件以支持英文显示
const PersonalityTraitScore: React.FC<{
    trait: {
        name: string;
        score: number;
        description: string;
        opposite: string;
    };
    getBrandColor: (brand: string) => string;
    brand: string;
    isEnglish?: boolean;
}> = ({ trait, getBrandColor, brand, isEnglish }) => {
    const { language } = useLanguage();
    const currentIsEnglish = isEnglish !== undefined ? isEnglish : language === 'en';
    const mbtiDims = mbtiDimensions[currentIsEnglish ? 'en' : 'zh'];

    // 获取首字母，并确保是有效的MBTI字母
    const traitKey = trait.name.charAt(0) as 'E' | 'I' | 'S' | 'N' | 'T' | 'F' | 'J' | 'P';
    const oppositeKey = trait.opposite.charAt(0) as 'E' | 'I' | 'S' | 'N' | 'T' | 'F' | 'J' | 'P';
    const scoreKey = trait.score > 50 ? traitKey : oppositeKey;

    return (
        <div className="mb-4 last:mb-0">
            <div className="flex justify-between text-sm mb-1">
                <span>{mbtiDims[traitKey]?.title || trait.name}</span>
                <span>{mbtiDims[oppositeKey]?.title || trait.opposite}</span>
            </div>
            <Progress value={trait.score} color={getBrandColor(brand)} />
            <div className="text-xs text-gray-400 mt-1">
                {currentIsEnglish
                    ? mbtiDims[scoreKey]?.description
                    : trait.description}
            </div>
        </div>
    );
};

export const CapabilityAssessmentMBTI: React.FC<CapabilityAssessmentMBTITyoe> = ({
    selectedBrands,
    currentVehicle = { brandModel: "极氪-007" },
}) => {
    const { language } = useLanguage();
    const isEnglish = language === 'en';

    // 获取真实的中文车型名称，用于查找数据
    const getChineseVehicleName = (displayName: string): string => {
        if (isEnglish) {
            // 如果当前是英文，且在反向映射中找到了匹配，则返回对应的中文名
            if (vehicleNameMapping.reverseEn[displayName]) {
                return vehicleNameMapping.reverseEn[displayName];
            }

            // 如果当前是英文，但在中文名映射中找到了匹配，说明这是一个中文名
            for (const [zhName, enName] of Object.entries(vehicleNameMapping.en)) {
                if (displayName === zhName) {
                    return displayName;
                }
            }
        }

        // 如果当前是中文或没有找到匹配，则直接返回
        return displayName;
    };

    // 获取显示名称
    const getDisplayName = (name: string): string => {
        return isEnglish ? (vehicleNameMapping.en[name] || name) : name;
    };

    // 将所有选定的品牌转换为中文名称，以便在personalityTraits中查询
    const chineseSelectedBrands = selectedBrands.map(getChineseVehicleName);

    // 获取当前车型的中文名称
    const currentChineseVehicle = { brandModel: getChineseVehicleName(currentVehicle.brandModel) };

    const currentPersonality = personalityTraits[currentChineseVehicle.brandModel as keyof typeof personalityTraits];

    // 如果没有找到当前车型的MBTI数据，显示空状态
    if (!currentPersonality) {
        return (
            <TabsContent value="mbti" className="space-y-8">
                <Card className="bg-[#1C2028] border-gray-800">
                    <CardContent className="p-6">
                        <div className="flex flex-col items-center justify-center py-12 text-center">
                            <div className="w-16 h-16 rounded-full bg-amber-500/10 flex items-center justify-center mb-4">
                                <svg
                                    className="w-8 h-8 text-amber-500"
                                    fill="none"
                                    stroke="currentColor"
                                    viewBox="0 0 24 24"
                                    xmlns="http://www.w3.org/2000/svg"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth={2}
                                        d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                                    />
                                </svg>
                            </div>
                            <h3 className="text-xl font-semibold text-white mb-2">{translations[language].noMBTIData || "暂无MBTI数据"}</h3>
                            <p className="text-gray-400 text-center max-w-md">
                                {translations[language].mbtiDataComingSoon || "该车型暂未进行MBTI性格特征评估，我们正在努力完善数据，敬请期待。"}
                            </p>
                        </div>
                    </CardContent>
                </Card>
            </TabsContent>
        );
    }

    // 为每个品牌生成不同的颜色
    const getBrandColor = (brand: string) => {
        // 使用中文名称进行比较，因为currentVehicle.brandModel可能是英文的
        const chineseBrand = getChineseVehicleName(brand);
        const currentChineseBrandModel = getChineseVehicleName(currentVehicle.brandModel);

        if (chineseBrand === currentChineseBrandModel) {
            return "#FF6B00";
        }
        const comparisonBrands = chineseSelectedBrands.filter(b => b !== currentChineseBrandModel);
        const index = comparisonBrands.indexOf(chineseBrand);
        return index === 0 ? "#326199" : "#4FB1A1";
    };

    // 根据当前语言获取对应的traits数据
    const getTraits = (personality: typeof personalityTraits[keyof typeof personalityTraits]) => {
        return isEnglish && personality.traitsEn ? personality.traitsEn : personality.traits;
    };

    return (
        <TabsContent value="mbti" className="space-y-8">
            <Card className="bg-[#1C2028] border-gray-800">
                <CardContent className="p-6">
                    <div className="space-y-8">
                        {/* 性格特征描述部分 */}
                        <div className={cn(
                            "grid gap-6",
                            selectedBrands.length === 1 ? "grid-cols-1" :
                                selectedBrands.length === 2 ? "grid-cols-2" :
                                    "grid-cols-3"
                        )}>
                            {/* 当前车型 */}
                            <PersonalityDescription
                                brand={currentVehicle.brandModel}
                                personality={currentPersonality}
                                getBrandColor={getBrandColor}
                                selectedBrands={selectedBrands}
                            />

                            {/* 对比车型 */}
                            {selectedBrands
                                .filter(brand => getChineseVehicleName(brand) !== currentChineseVehicle.brandModel)
                                .map(brand => {
                                    const chineseBrand = getChineseVehicleName(brand);
                                    const brandPersonality = personalityTraits[chineseBrand as keyof typeof personalityTraits];
                                    if (!brandPersonality) return null;

                                    return (
                                        <PersonalityDescription
                                            key={brand}
                                            brand={brand}
                                            personality={brandPersonality}
                                            getBrandColor={getBrandColor}
                                            selectedBrands={selectedBrands}
                                        />
                                    );
                                })}
                        </div>

                        {/* MBTI特征评估 */}
                        <div className="grid grid-cols-2 gap-6">
                            {getTraits(currentPersonality).map((currentTrait) => {
                                // 提取当前特征的MBTI字母
                                const traitLetter = currentTrait.name.match(/\(([IESNTFJP])\)/)?.at(1) || '';
                                const oppositeLetter = currentTrait.opposite.match(/\(([IESNTFJP])\)/)?.at(1) || '';

                                // 确定左右显示顺序：E/S/T/J 始终在左侧，I/N/F/P 始终在右侧
                                const leftSideLetters = ['E', 'S', 'T', 'J'];
                                const shouldFlip = !leftSideLetters.includes(traitLetter) && leftSideLetters.includes(oppositeLetter);

                                // 根据需要翻转特性名称的显示顺序
                                const leftTrait = shouldFlip ? currentTrait.opposite : currentTrait.name;
                                const rightTrait = shouldFlip ? currentTrait.name : currentTrait.opposite;

                                return (
                                    <div key={currentTrait.name} className="bg-black/20 rounded-lg p-6 border border-gray-800/50">
                                        <div className="space-y-4">
                                            {/* 特征名称 - 固定顺序显示 */}
                                            <div className="flex justify-between items-center">
                                                <span className="text-lg font-semibold text-white">{leftTrait}</span>
                                                <span className="text-lg font-semibold text-gray-400">{rightTrait}</span>
                                            </div>

                                            {/* 评分轴 */}
                                            <div className="relative h-12">
                                                {/* 对比车型点 */}
                                                <ComparisonDots
                                                    trait={shouldFlip ?
                                                        { ...currentTrait, name: currentTrait.opposite, opposite: currentTrait.name } :
                                                        currentTrait}
                                                    currentVehicle={currentChineseVehicle}
                                                    selectedBrands={chineseSelectedBrands}
                                                    getBrandColor={getBrandColor}
                                                    personalityTraits={personalityTraits}
                                                    isEnglish={isEnglish}
                                                />
                                            </div>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    </div>
                </CardContent>
            </Card>
        </TabsContent>
    )
}
