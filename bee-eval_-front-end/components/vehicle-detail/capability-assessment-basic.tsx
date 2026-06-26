'use client'

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { TabsContent } from "@/components/ui/tabs"
import { ResponsiveContainer, RadarChart, PolarGrid, PolarAngleAxis, Radar, Tooltip, PolarRadiusAxis } from 'recharts'
import React, { useMemo, useState, useEffect, useRef, useCallback } from 'react'
import { CapabilityAssessmentBasicType } from './capability-assessment-data'
import { Badge } from "@/components/ui/badge"
import { HoverCard, HoverCardContent, HoverCardTrigger } from "@/components/ui/hover-card"
import { Portal } from "@radix-ui/react-portal"
import http from '@/utils/request'
import { handleTokenExpired, handleTokenReplaced } from '@/utils/auth'
import { Lancelot } from "next/font/google"
import { useLanguage } from "@/constants/language"
import { translations } from "@/language"

interface PerformanceData {
    [key: string]: number;
}

// 指标详情接口
interface IndexDetail {
    indexName: string;
    detail: string;
}

interface IndexDetailData {
    cognitiveAbility: IndexDetail[];
    actionAbility: IndexDetail[];
}

// 感知能力数据接口
interface PerceptionAbilityResponse {
    code: number;
    msg: string;
    data: PerceptionCategory[];
}

interface PerceptionVehicle {
    vehicleId: string;
    vehicleName: string;
    isHave: string; // "0"表示不支持，"2"表示支持
}

interface PerceptionItem {
    threeTagName: string;
    vehicleList: PerceptionVehicle[];
}

interface PerceptionCategory {
    perceptionAbilityName: string;
    perceptionAbilitySmallList: PerceptionItem[];
}

// 功能描述映射
const featureDescriptions: Record<string, Record<string, string>> = {
    "听觉感知": {
        "多音区识别": "可以识别车内不同区域的声音，提供更精确的语音交互体验。",
        "全车免唤醒": "车内任何位置均可无需唤醒词直接发起交互。",
        "车外语音": "支持通过车外麦克风进行语音交互，无需进入车内。",
        "多人同时说话": "系统能同时处理多人的语音指令，不会混淆或冲突。",
        "声纹记忆": "系统可以记住用户的声音特征，实现个性化识别。",
        "随时打断": "用户可以随时打断AI的回答，实现更自然的对话体验。"
    },
    "多模态感知": {
        "注视": "系统能检测到用户眼睛注视的方向和物体。",
        "手势控制": "支持通过手势进行车机系统控制，无需触摸屏幕。",
        "可见即可说": "用户可以直接指着屏幕上看到的内容发出指令。",
        "人脸记忆": "系统能记住用户的面部特征，实现个性化识别。",
        "视线追踪": "精确追踪驾驶员视线方向，提供更智能的交互体验。"
    }
}

export const CapabilityAssessmentBasic: React.FC<CapabilityAssessmentBasicType> = ({
    showIndustryAverage,
    selectedBrands,
    getBrandColor,
    assessmentData,
    currentVehicle = { brandModel: "", id: "" },
    vehicles,
    indexDetails,
}) => {
    // 当前车辆ID，通过URL参数获取
    const [vehicleId, setVehicleId] = useState<string>("");
    const [perceptionData, setPerceptionData] = useState<PerceptionCategory[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const { language } = useLanguage()
    // 添加选中指标状态 - 设置默认值确保界面始终有内容显示
    const [selectedIndex, setSelectedIndex] = useState<string>("信息提取能力");
    const [selectedIndexDetail, setSelectedIndexDetail] = useState<string>("评估大模型从各源数据提取有价值信息的能力");
    // 添加一个标记，记录是否已经选择了默认指标
    const hasSetDefaultIndex = useRef(false);
    // 添加一个初始化标志，确保组件只初始化一次
    const isInitialized = useRef(false);

    // 从URL中获取车辆ID
    useEffect(() => {
        if (typeof window !== 'undefined') {
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            if (id) {
                setVehicleId(id);
            }
        }
    }, []);

    // 获取所有选中车型的ID数组
    const getSelectedVehicleIds = () => {
        // 创建一个集合来存储唯一的ID
        const idSet = new Set<string>();

        // 添加currentVehicle的id（如果存在）
        if (currentVehicle && 'id' in currentVehicle && currentVehicle.id) {
            idSet.add(currentVehicle.id);
        }

        // 从vehicles中获取选中车型的ID
        if (Array.isArray(vehicles) && Array.isArray(selectedBrands)) {
            const selectedVehicles = vehicles.filter((item: any) =>
                selectedBrands.includes(item.brandModel) &&
                item.brandModel !== currentVehicle.brandModel // 排除当前车型，避免重复
            );

            // 添加所有选中车型的ID
            selectedVehicles.forEach((vehicle: any) => {
                if (vehicle.id) {
                    idSet.add(vehicle.id);
                }
            });
        }

        // 转换为数组并返回
        return Array.from(idSet).filter(id => id);
    };

    // 对感知能力数据进行处理，合并相同功能标签
    const processPerceptionData = (data: PerceptionCategory[]): PerceptionCategory[] => {
        if (!Array.isArray(data) || data.length === 0) return [];

        // 由于新的API响应已按功能项分组，可能不需要之前的去重逻辑
        // 如果仍有其他处理需求，可在此添加
        return data;
    };

    // 获取感知能力数据
    const fetchPerceptionData = async (ids: string[]) => {
        if (!ids || ids.length === 0) return;

        try {
            setLoading(true);

            const response = await http.post<any>('/ware/functiontree/queryPerceptionAbilityByVehicleId', {
                ids: ids,
                language: language
            });

            // 检查 token 是否失效
            if (response.code === 11012) {
                handleTokenExpired()
                return
            }
            if (response.code === 11014) {
                handleTokenReplaced()
                return
            }

            // 检查response的结构
            if (response && response.data) {

                // 处理数据，合并重复功能项
                if (Array.isArray(response.data)) {
                    if (response.data.length > 0) {
                    }

                    // 对数据进行处理后再设置状态
                    const processedData = processPerceptionData(response.data);
                    setPerceptionData(processedData);
                } else {
                    setPerceptionData([]);
                }
            } else {
                setPerceptionData([]);
            }
        } catch (error) {
            setPerceptionData([]);
        } finally {
            setLoading(false);
        }
    };

    // 当车型ID或选定的品牌发生变化时获取数据
    useEffect(() => {
        const ids = getSelectedVehicleIds();
        if (ids.length > 0) {
            fetchPerceptionData(ids);
        }
    }, [vehicleId, selectedBrands, vehicles]);

    // 从列表数据中提取 performanceMetrics
    const performanceMetrics = useMemo(() => {
        const allAbilities: any[] = assessmentData.flatMap((vehicle: any) => vehicle.actionAbilities);
        const uniqueMetrics = Array.from(new Set(allAbilities.map(ability => ability.actionDescription)));
        return uniqueMetrics.map(label => {
            const exampleAbility = allAbilities.find(ability => ability.actionDescription === label);
            return {
                label,
                unit: exampleAbility?.unit || '',
            };
        });
    }, [assessmentData]);

    const getPerformanceData = (metric: any): PerformanceData => {
        const data: PerformanceData = {
            [currentVehicle.brandModel]: metric.actionNumber,
        };

        // 添加行业均值数据
        if (showIndustryAverage) {
            const industryData = assessmentData.find((vehicle: any) =>
                vehicle.vehicleName === "行业均值" || vehicle.vehicleId === "111"
            )?.actionAbilities.find(
                (ability: any) => ability.actionDescription === metric.actionDescription
            );

            if (industryData) {
                data[translations[language].hangyejunzhi] = industryData.actionNumber;
            } else {
                // 如果找不到行业均值数据，设置默认值或留空
                data[translations[language].hangyejunzhi] = 0;
            }
        }

        // 添加其他选中的车型数据
        selectedBrands.forEach(brand => {
            if (brand !== currentVehicle.brandModel) {
                const brandData = assessmentData.find((vehicle: any) => vehicle.vehicleName === brand)?.actionAbilities.find(
                    (ability: any) => ability.actionDescription === metric.actionDescription
                );
                if (brandData) {
                    data[brand] = brandData.actionNumber;
                }
            }
        });

        return data;
    };

    // 选择默认指标的函数 - 优化选择逻辑
    const selectDefaultIndex = useCallback(() => {
        // 如果已经有选中的指标，且不是默认的"信息提取能力"，则不需要再选择
        if (selectedIndex !== "信息提取能力" && selectedIndexDetail !== "评估大模型从各源数据提取有价值信息的能力") {
            return;
        }

        if (assessmentData && assessmentData.length > 0) {
            // 尝试从认知能力数据中获取
            const cognitiveAbilities = assessmentData[0]?.cognitiveAbilities || [];
            if (cognitiveAbilities.length > 0) {
                const defaultIndex = cognitiveAbilities[0]?.cognitiveDescription;
                if (defaultIndex) {
                    setSelectedIndex(defaultIndex);
                    setSelectedIndexDetail(getIndexDetail(defaultIndex, 'cognitive') || "");
                    hasSetDefaultIndex.current = true;
                    return;
                }
            }

            // 尝试从转换后的数据中获取
            const transformedData = transformData(assessmentData);
            if (transformedData.length > 0) {
                const defaultSubject = transformedData[0]?.subject;
                if (defaultSubject) {
                    setSelectedIndex(defaultSubject);
                    setSelectedIndexDetail(getIndexDetail(defaultSubject, 'cognitive') || "");
                    hasSetDefaultIndex.current = true;
                    return;
                }
            }
        }
    }, [assessmentData, indexDetails, selectedIndex, selectedIndexDetail]);

    // 修改transformData函数，确保不会因为状态更新导致循环渲染
    function transformData(list: any[]): { subject: string;[key: string]: string | number }[] {
        if (!list || list.length === 0) {
            return [];
        }

        // 获取所有车辆的名字
        const vehicleNames = list.map(vehicle => vehicle.vehicleName);

        // 获取所有的能力标签（generalAbilityTag）
        const subjects = [...new Set(list.flatMap(vehicle => vehicle.generalAbilities?.map((item: any) => item.generalAbilityTag) || []))];

        // 只在组件初始化时自动选择第一个指标，而不是每次transformData被调用
        if (subjects.length > 0 && !isInitialized.current) {
            // 使用setTimeout确保状态更新在当前渲染周期之后
            setTimeout(() => {
                selectDefaultIndex();
                isInitialized.current = true;
            }, 0);
        }

        return subjects.map(subject => {
            const result: { [key: string]: string | number, subject: string } = { subject };

            // 对每个车辆，查找其对应的能力值
            vehicleNames.forEach(vehicleName => {
                const vehicle = list.find(v => v.vehicleName === vehicleName);
                const ability = vehicle?.generalAbilities?.find((item: any) => item.generalAbilityTag === subject);
                if (ability) {
                    result[vehicleName] = ability.generalAbilityValue;
                }
            });

            return result;
        });
    }

    // 判断某个特性是否被某车型支持
    const isFeatureSupported = (vehicle: PerceptionVehicle) => {
        return vehicle.isHave === "2";
    };

    // 获取特性描述
    const getFeatureDescription = (category: string, feature: string) => {
        return featureDescriptions[category]?.[feature] || "暂无描述";
    };

    // 获取指标详情的函数
    const getIndexDetail = (indexName: string, type: 'cognitive' | 'action'): string => {
        if (!indexDetails) return '';

        const detailsList = type === 'cognitive' ? indexDetails.cognitiveAbility : indexDetails.actionAbility;
        const detail = detailsList.find(item => item.indexName === indexName);
        return detail?.detail || '';
    };

    // 点击雷达图数据点时更新选中的指标
    const handleRadarClick = (payload: any) => {
        if (payload && payload.activeLabel) {
            const indexName = payload.activeLabel;
            setSelectedIndex(indexName);
            setSelectedIndexDetail(getIndexDetail(indexName, 'cognitive'));
        }
    };

    // 组件加载时设置默认选中的指标 - 优化useEffect依赖
    useEffect(() => {
        if (assessmentData && assessmentData.length > 0 && !isInitialized.current) {
            selectDefaultIndex();
            isInitialized.current = true;
        }
    }, [assessmentData, selectDefaultIndex]);

    // 清理原来不需要的useEffect，避免重复设置指标
    // 删除原来的单独useEffect

    // 在状态更新后添加一个useEffect来检查状态
    useEffect(() => {
    }, [perceptionData]);

    return (
        <TabsContent value="basic" className="space-y-8">
            <div className="grid grid-cols-2 gap-8">
                {/* 通识能力雷达图 */}
                <Card className="bg-[#1C2028] border-gray-800 relative">
                    <CardHeader className="pb-4">
                        <CardTitle className="text-lg font-medium bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">{translations[language].generalAbility}</CardTitle>
                    </CardHeader>
                    <CardContent className="flex flex-col h-[500px]">
                        {/* 雷达图区域 - 使用固定高度 */}
                        <div className="h-[380px]">
                            <ResponsiveContainer width="100%" height="100%">
                                <RadarChart
                                    data={transformData(assessmentData)}
                                    margin={{ top: 20, right: 40, bottom: 20, left: 40 }}
                                    cx="50%"
                                    cy="50%"
                                    outerRadius={140}
                                    onClick={handleRadarClick}
                                >
                                    <PolarGrid stroke="rgba(255,255,255,0.1)" gridType="circle" />
                                    <PolarAngleAxis
                                        dataKey="subject"
                                        tick={(props) => {
                                            const { x, y, payload, textAnchor, ...rest } = props;
                                            // 判断是否为当前选中的指标
                                            const isSelected = payload.value === selectedIndex;

                                            return (
                                                <g transform={`translate(${x},${y})`}>
                                                    <text
                                                        x={0}
                                                        y={0}
                                                        textAnchor={textAnchor}
                                                        fill={isSelected ? "#FF6B00" : "#9CA3AF"}
                                                        fontWeight={isSelected ? "600" : "400"}
                                                        fontSize={12}
                                                    >
                                                        {payload.value}
                                                    </text>
                                                </g>
                                            );
                                        }}
                                    />

                                    {/* 当前车型 */}
                                    <Radar
                                        name={currentVehicle.brandModel}
                                        dataKey={currentVehicle.brandModel}
                                        stroke="#FF6B00"
                                        fill="#FF6B00"
                                        fillOpacity={0.1}
                                        dot={{
                                            r: 4,
                                            fill: "#FF6B00",
                                            strokeWidth: 2,
                                            cursor: "pointer"
                                        }}
                                    />

                                    {/* 对比车型 */}
                                    {selectedBrands
                                        .filter(brand => brand !== currentVehicle.brandModel)
                                        .map((brand, index) => (
                                            <Radar
                                                key={brand}
                                                name={brand}
                                                dataKey={brand}
                                                stroke={getBrandColor(brand)}
                                                fill={getBrandColor(brand)}
                                                fillOpacity={0.1}
                                                dot={{
                                                    r: 4,
                                                    fill: getBrandColor(brand),
                                                    strokeWidth: 2,
                                                    cursor: "pointer"
                                                }}
                                                activeDot={{
                                                    r: 6,
                                                    fill: getBrandColor(brand),
                                                    strokeWidth: 2,
                                                    cursor: "pointer"
                                                }}
                                            />
                                        ))}

                                    {/* 行业均值 */}
                                    {showIndustryAverage && (
                                        <Radar
                                            name={translations[language].hangyejunzhi}
                                            dataKey={translations[language].hangyejunzhi}
                                            stroke="#6B7280"
                                            strokeWidth={1}
                                            fill="#6B7280"
                                            fillOpacity={0.1}
                                            dot={{
                                                r: 3,
                                                fill: "#6B7280",
                                                fillOpacity: 0.5,
                                                stroke: "#6B7280",
                                                strokeWidth: 1
                                            }}
                                        />
                                    )}
                                    <PolarRadiusAxis
                                        domain={[0, 5]}
                                        tick={false}
                                        axisLine={false}
                                        tickCount={6}
                                    />
                                    <Tooltip
                                        contentStyle={{
                                            backgroundColor: '#1C2028',
                                            border: '1px solid rgba(255,255,255,0.1)',
                                            borderRadius: '8px',
                                            padding: '10px',
                                            maxWidth: '300px'
                                        }}
                                        formatter={(value: number, name: string, props: any) => {
                                            return [
                                                <div key={`${name}-value`}>
                                                    <span style={{
                                                        color: name === currentVehicle.brandModel ? '#FF6B00' :
                                                            (name === translations[language].hangyejunzhi ? '#6B7280' : getBrandColor(name))
                                                    }}>
                                                        {value.toFixed(1)} {translations[language].points}
                                                    </span>
                                                </div> as React.ReactNode,
                                                name
                                            ];
                                        }}
                                        labelStyle={{
                                            color: '#FF6B00',
                                            fontWeight: 'bold'
                                        }}
                                    />
                                </RadarChart>
                            </ResponsiveContainer>
                        </div>

                        {/* 指标详情展示区域 - 固定位置并始终显示 */}
                        <div className="w-full mt-4 h-[120px]">
                            <div className="bg-[#14161c] text-[#9ca3af] rounded-md p-3 h-full">
                                <div className="flex flex-col h-full">
                                    {/* 指标名称和描述 */}
                                    <div className="mb-2">
                                        <div className="text-md font-medium">{selectedIndex || "..."}</div>
                                        <div className="text-xs text-[#9ca3af]/80 mt-1">
                                            {selectedIndexDetail || translations[language].noDataAvailable}
                                        </div>
                                    </div>

                                    {/* 车辆得分行 */}
                                    <div className="flex flex-wrap gap-2 mt-auto">
                                        {/* 当前车型得分 */}
                                        <div className="flex items-center bg-black/30 rounded-md py-1.5 px-3">
                                            <span className="text-xs text-gray-400 mr-2">{currentVehicle.brandModel}:</span>
                                            <span className="text-sm font-medium text-amber-500">
                                                {(() => {
                                                    const value = transformData(assessmentData).find(item => item.subject === selectedIndex)?.[currentVehicle.brandModel];
                                                    return typeof value === 'number' ? `${value.toFixed(1)}分` : '-';
                                                })()}
                                            </span>
                                        </div>

                                        {/* 对比车型得分 */}
                                        {selectedBrands
                                            .filter(brand => brand !== currentVehicle.brandModel)
                                            .map(brand => (
                                                <div key={brand} className="flex items-center bg-black/30 rounded-md py-1.5 px-3">
                                                    <span className="text-xs text-gray-400 mr-2">{brand}:</span>
                                                    <span className="text-sm font-medium" style={{ color: getBrandColor(brand) }}>
                                                        {(() => {
                                                            const value = transformData(assessmentData).find(item => item.subject === selectedIndex)?.[brand];
                                                            return typeof value === 'number' ? `${value.toFixed(1)}分` : '-';
                                                        })()}
                                                    </span>
                                                </div>
                                            ))}

                                        {/* 行业均值得分 */}
                                        {showIndustryAverage && (
                                            <div className="flex items-center bg-black/30 rounded-md py-1.5 px-3">
                                                <span className="text-xs text-gray-400 mr-2">{translations[language].hangyejunzhi}:</span>
                                                <span className="text-sm font-medium text-gray-400">
                                                    {(() => {
                                                        const value = transformData(assessmentData).find(item => item.subject === selectedIndex)?.[translations[language].hangyejunzhi];
                                                        return typeof value === 'number' ? `${value.toFixed(1)}分` : '-';
                                                    })()}
                                                </span>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </CardContent>
                </Card>

                {/* 行动能力 */}
                <Card className="bg-[#1C2028] border-gray-800">
                    <CardHeader className="pb-4">
                        <CardTitle className="text-lg font-medium bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">{translations[language].actionAbility}</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <div className="grid grid-cols-2 gap-4">
                            {performanceMetrics.map((metric) => {
                                const data = getPerformanceData(
                                    assessmentData.find((vehicle: any) => vehicle.vehicleName === currentVehicle.brandModel)?.actionAbilities.find(
                                        (ability: any) => ability.actionDescription === metric.label
                                    ) || { actionNumber: 0 }
                                );
                                const hasComparison = selectedBrands.filter(brand => brand !== currentVehicle.brandModel).length > 0;
                                const detail = getIndexDetail(metric.label, 'action');

                                return (
                                    <div key={metric.label} className="bg-black/20 rounded-lg p-4 hover:bg-black/30 transition-all border border-gray-800/50">
                                        <div className="flex flex-col">
                                            <div className="flex items-center gap-1">
                                                <div className="text-sm font-medium text-gray-400 mb-4">{metric.label}</div>
                                                {detail && (
                                                    <HoverCard>
                                                        <HoverCardTrigger asChild>
                                                            <div className="cursor-help opacity-60 hover:opacity-100">
                                                                <svg style={{ marginTop: '-17px' }} xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-gray-400">
                                                                    <circle cx="12" cy="12" r="10"></circle>
                                                                    <path d="M12 16v-4"></path>
                                                                    <path d="M12 8h.01"></path>
                                                                </svg>
                                                            </div>
                                                        </HoverCardTrigger>
                                                        <HoverCardContent className="w-80 bg-[#1C2028] border border-gray-800 rounded-md shadow-lg p-4">
                                                            <p className="text-sm text-gray-300">{detail}</p>
                                                        </HoverCardContent>
                                                    </HoverCard>
                                                )}
                                            </div>
                                            <div className="flex items-center gap-6">
                                                {/* 主车型数据 */}
                                                <div className="flex flex-col min-w-[80px]">
                                                    <div className="flex items-baseline gap-1">
                                                        <span className={`font-bold tracking-tight bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent ${hasComparison ? 'text-3xl' : 'text-4xl'}`}>
                                                            {data[currentVehicle.brandModel]}
                                                        </span>
                                                        <span className="text-[10px] text-gray-400 font-medium">{metric.unit}</span>
                                                    </div>
                                                    {hasComparison && (
                                                        <span className="text-[10px] text-gray-400 mt-1">{currentVehicle.brandModel}</span>
                                                    )}
                                                    {showIndustryAverage && (
                                                        <div className="text-[10px] text-gray-500 mt-1.5 flex items-center">
                                                            <span>{translations[language].hangyejunzhi}</span>
                                                            <span className="font-medium">{data[`${translations[language].hangyejunzhi}`] || data[`${translations[language].emptyData}`]}{metric.unit}</span>
                                                        </div>
                                                    )}
                                                </div>

                                                {/* 对比车型数据 */}
                                                {Object.entries(data)
                                                    .filter(([brand]) => brand !== currentVehicle.brandModel && brand !== translations[language].hangyejunzhi && brand !== "行业平均")
                                                    .map(([brand, value]) => (
                                                        <div key={brand} className="flex flex-col min-w-[80px]">
                                                            <div className="flex items-baseline gap-1">
                                                                <span className="text-xl font-bold tracking-tight" style={{ color: getBrandColor(brand) }}>
                                                                    {value}
                                                                </span>
                                                                <span className="text-[10px] text-gray-400 font-medium">{metric.unit}</span>
                                                            </div>
                                                            <span className="text-[10px] text-gray-400 mt-1">{brand}</span>
                                                            {showIndustryAverage && (
                                                                <div className="text-[10px] text-gray-500 mt-1 invisible">占位</div>
                                                            )}
                                                        </div>
                                                    ))}
                                            </div>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    </CardContent>
                </Card>

                {/* 感知能力 */}
                <Card className="bg-[#1C2028] border-gray-800 col-span-2">
                    <CardHeader className="pb-4">
                        <CardTitle className="text-lg font-medium bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">{translations[language].perceptionAbility}</CardTitle>
                    </CardHeader>
                    <CardContent>
                        {loading ? (
                            <div className="text-center py-10 text-gray-400">正在加载感知能力数据...</div>
                        ) : (
                            <div className="grid grid-cols-2 gap-8">
                                {Array.isArray(perceptionData) && perceptionData.length > 0 ? (
                                    perceptionData.map((category, idx) => (
                                        <Card key={`${category.perceptionAbilityName}-${idx}`} className="bg-[#1C2028] border-gray-800">
                                            <CardHeader className="pb-2">
                                                <CardTitle className="text-lg font-medium bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">
                                                    {category.perceptionAbilityName}
                                                </CardTitle>
                                            </CardHeader>
                                            <CardContent className="space-y-3">
                                                {Array.isArray(category.perceptionAbilitySmallList) &&
                                                    category.perceptionAbilitySmallList.map((item, itemIdx) => (
                                                        <div key={`${item.threeTagName}-${itemIdx}`} className="flex items-center justify-between">
                                                            <span className="text-sm text-gray-400">{item.threeTagName}</span>
                                                            <div className="flex gap-2">
                                                                {Array.isArray(item.vehicleList) && item.vehicleList.length > 0 ? (
                                                                    item.vehicleList.map((vehicle, vehicleIdx) => (
                                                                        <HoverCard key={`${vehicle.vehicleId}-${vehicleIdx}`}>
                                                                            <HoverCardTrigger asChild>
                                                                                <div className="relative">
                                                                                    <Badge
                                                                                        variant="outline"
                                                                                        className={`${isFeatureSupported(vehicle) ? 'bg-amber-500/10 text-amber-500 border-amber-500/20' : 'bg-gray-500/10 text-gray-500 border-gray-500/20'} cursor-pointer relative`}
                                                                                    >
                                                                                        {vehicle.vehicleName}
                                                                                    </Badge>
                                                                                </div>
                                                                            </HoverCardTrigger>
                                                                            <Portal>
                                                                                <HoverCardContent
                                                                                    className="w-80 bg-[#1C2028] border border-gray-800 rounded-md shadow-lg p-4 z-[100]"
                                                                                    side="right"
                                                                                    align="start"
                                                                                    sideOffset={5}
                                                                                >
                                                                                    <div className="flex flex-col gap-2">
                                                                                        <h4 className="text-sm font-semibold text-amber-500">{item.threeTagName}</h4>
                                                                                        <p className="text-sm text-gray-400">
                                                                                            {isFeatureSupported(vehicle)
                                                                                                ? getFeatureDescription(category.perceptionAbilityName, item.threeTagName)
                                                                                                : "不具备该功能"}
                                                                                        </p>
                                                                                    </div>
                                                                                </HoverCardContent>
                                                                            </Portal>
                                                                        </HoverCard>
                                                                    ))
                                                                ) : (
                                                                    <div className="text-xs text-gray-500">无车型数据</div>
                                                                )}

                                                                {/* 生成未在vehicleList中的已选车型的标签 */}
                                                                {selectedBrands
                                                                    .filter(brand => {
                                                                        // 如果vehicleList不是有效数组，则显示所有选中车型
                                                                        if (!Array.isArray(item.vehicleList)) {
                                                                            return true;
                                                                        }
                                                                        // 否则过滤掉已在vehicleList中的车型
                                                                        return !item.vehicleList.some(v => v.vehicleName === brand);
                                                                    })
                                                                    .map((brand, brandIdx) => (
                                                                        <HoverCard key={`${brand}-${brandIdx}`}>
                                                                            <HoverCardTrigger asChild>
                                                                                <div className="relative">
                                                                                    <Badge
                                                                                        variant="outline"
                                                                                        className="bg-gray-500/10 text-gray-500 border-gray-500/20 cursor-pointer relative"
                                                                                    >
                                                                                        {brand}
                                                                                    </Badge>
                                                                                </div>
                                                                            </HoverCardTrigger>
                                                                            <Portal>
                                                                                <HoverCardContent
                                                                                    className="w-80 bg-[#1C2028] border border-gray-800 rounded-md shadow-lg p-4 z-[100]"
                                                                                    side="right"
                                                                                    align="start"
                                                                                    sideOffset={5}
                                                                                >
                                                                                    <div className="flex flex-col gap-2">
                                                                                        <h4 className="text-sm font-semibold text-amber-500">{item.threeTagName}</h4>
                                                                                        <p className="text-sm text-gray-400">无对比数据</p>
                                                                                    </div>
                                                                                </HoverCardContent>
                                                                            </Portal>
                                                                        </HoverCard>
                                                                    ))}
                                                            </div>
                                                        </div>
                                                    ))}
                                            </CardContent>
                                        </Card>
                                    ))
                                ) : (
                                    <div className="col-span-3 text-center py-10 text-gray-400">
                                        暂无感知能力数据
                                        {JSON.stringify(perceptionData) !== '[]' && <div>数据结构异常: {JSON.stringify(perceptionData).substring(0, 100)}...</div>}
                                    </div>
                                )}
                            </div>
                        )}
                    </CardContent>
                </Card>

            </div>
        </TabsContent>
    )
}
