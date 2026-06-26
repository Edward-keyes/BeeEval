'use client'

import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Card, CardContent } from '@/components/ui/card'
import Image from 'next/image'
import { Button } from '@/components/ui/button'
import http from '@/utils/request'
import {
    ResponsiveContainer,
    BarChart,
    XAxis,
    YAxis,
    Tooltip,
    CartesianGrid,
    Bar,
    Cell,
    LabelList,
    RadarChart,
    PolarGrid,
    PolarAngleAxis,
    Radar,
    PolarRadiusAxis
} from 'recharts'
import React, { useState, useEffect, useRef } from 'react'
import {
    CapabilityAssessmentDomainType,
    industryAverageData,
    MetricKey,
    basicCapabilityData,
    testCases
} from './capability-assessment-data'
import { Eye, X, MousePointerClick, ZoomIn, Bot, Info, ChevronLeft, ChevronRight, FileX } from 'lucide-react'
import { handleTokenExpired, handleTokenReplaced } from '@/utils/auth'
import { cn } from '@/lib/utils'
import { useAuthStore } from '@/store/useAuthStore'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'

// 定义测试案例接口
interface TestCaseItem {
    score: number;
    testInstruct: string;
    url: string;
    interpretationOfResult: string;
    indexName: string;
    vehicleName?: string; // 添加车型名称，用于区分不同车型的测试案例
    detail?: string; // 添加指标详情说明字段
}

interface ProblemData {
    vehicleName: string;
    vehicleId: string;
    problemList: Array<{
        score: number;
        question: string;
    }>;
}

interface Vehicle {
    id: string;
    name: string;
    status?: number;
    // ... other existing fields ...
}

export const CapabilityAssessmentDomain: React.FC<CapabilityAssessmentDomainType> = ({
    showIndustryAverage,
    selectedBrands,
    getBrandColor,
    vehicles,
    currentVehicle = { brandModel: '' }
}) => {
    const [selectedMetric, setSelectedMetric] = useState<MetricKey>('复杂指令识别率')
    const [indexDetail, setIndexDetail] = useState<any>()
    const [selectedDomain, setSelectedDomain] = useState<any>()
    const [isImageModalOpen, setIsImageModalOpen] = useState(false)
    const [listData, setListData] = useState<any[]>([])
    const [queryIndexScore, setQueryIndexScore] = useState<any[]>([])
    const [selectedDomainIndexId, setSelectedDomainIndexId] = useState<string>('')
    const [isLoadingTestCase, setIsLoadingTestCase] = useState(false)
    const [selectedVehicleForCase, setSelectedVehicleForCase] = useState<string>('') // 当前选择查看的车型
    const DEFAULT_METRIC_NAME = '直接指令识别率'; // 定义默认指标名称常量
    const [activeTab, setActiveTab] = useState('tab1')
    const [problemData, setProblemData] = useState<ProblemData[]>([])
    const { token, accountStatus } = useAuthStore()
    const { language } = useLanguage()
    const fetchData = async (ids: Array<any>) => {
        try {
            const res = await http.post<any[]>('/vehicle/domaintree/queryScoreByBrandInfo', {
                brandBaseInfoId: ids,
                language: language
            })

            // 检查 token 是否失效
            if (res.code === 11012) {
                handleTokenExpired()
                return
            }
            if (res.code === 11014) {
                handleTokenReplaced()
                return
            }

            setListData(res.data)
            setSelectedDomain(res.data[0].functionDomainId)
            queryIndexScoreChange(res.data[0].functionDomainId)
        } catch (error) {
        }
    }

    function change() {
        const targetModels = selectedBrands
        const result = vehicles.filter((item: any) => targetModels.includes(item.brandModel));
        fetchData(result.map((item: any) => item.id))
    }

    const fetchDataQueryIndexScore = async (functionalDomain: any, ids: Array<any>) => {
        try {
            const res = await http.post<any[]>('/vehicle/domaintree/queryIndexScore', {
                functionalDomain: functionalDomain,
                brandBaseInfoId: ids,
                language: language
            })

            // 检查 token 是否失效
            if (res.code === 11012) {
                handleTokenExpired()
                return
            }
            if (res.code === 11014) {
                handleTokenReplaced()
                return
            }

            // 过滤掉 vehicleIndexScore 为空数组的指标
            const filteredData = (res.data || []).filter(item => Array.isArray(item.vehicleIndexScore) && item.vehicleIndexScore.length > 0);
            setQueryIndexScore(filteredData);

            // 新增：切换域后，自动切换 selectedMetric 和 indexDetail
            if (filteredData && filteredData.length > 0) {
                setSelectedMetric(filteredData[0].domainIndexName as MetricKey);
                setIndexDetail(filteredData[0].indexDetail);
                setSelectedDomainIndexId(filteredData[0].domainIndexId);
                fetchTestCaseData(filteredData[0].domainIndexId);
            } else {
                setSelectedMetric('' as MetricKey);
                setIndexDetail('');
                setSelectedDomainIndexId('');
                setProblemData([]);
            }
        } catch (error) {
            // console.error('获取车辆列表失败:', error)
        }
    }
    function queryIndexScoreChange(functionalDomain: any) {
        const targetModels = selectedBrands
        const result = vehicles.filter((item: any) => targetModels.includes(item.brandModel));
        fetchDataQueryIndexScore(functionalDomain, result.map((item: any) => item.id));
    }
    useEffect(() => {
        change()
    }, [selectedBrands])

    function generateList(list: Array<any>) {
        let list2: any = [];
        let vehicleNames = new Set();

        list.forEach(item => {
            item.vehicleIndexScore.forEach((vehicle: any) => {
                vehicleNames.add(vehicle.vehicleInfoName);
            });
        });

        const vehicleNamesArray = Array.from(vehicleNames);

        list.forEach(item => {
            let subject = item.domainIndexName;
            let industryAvgScore = item.indexAvgScore;
            let vehicleScores: { [key: string]: number } = {};

            item.vehicleIndexScore.forEach((vehicle: any) => {
                vehicleScores[vehicle.vehicleInfoName] = vehicle.vehicleInfoScore;
            });

            vehicleNamesArray.forEach((name: any) => {
                if (!(name in vehicleScores)) {
                    vehicleScores[name] = industryAvgScore;
                }
            });

            let entry: { [key: string]: any } = { subject, "行业均值": industryAvgScore };
            vehicleNames.forEach((name: any) => {
                entry[name] = vehicleScores[name];
            });

            list2.push(entry);
        });

        return list2;
    }

    // 获取问题列表数据
    const fetchProblemList = async (domainIndexId: string) => {
        if (!domainIndexId) return;

        try {
            // 获取所有选中车型的ID
            const selectedVehicleIds: string[] = [];

            // 提取选中车型的ID
            vehicles.forEach((item: any) => {
                if (item.id && item.brandModel && selectedBrands.includes(item.brandModel)) {
                    selectedVehicleIds.push(String(item.id));
                }
            });

            if (selectedVehicleIds.length === 0) {
                setProblemData([]);
                return;
            }

            // 调用API获取问题列表数据
            const response = await http.post<{ data: ProblemData[] }>('/vehicletestresult/list/multiple', {
                domainIndexId: domainIndexId,
                vehicleId: selectedVehicleIds
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

            if (response && Array.isArray(response.data)) {
                setProblemData(response.data);
            } else {
                setProblemData([]);
            }
        } catch (error) {
            // console.error('获取问题列表数据失败:', error);
            setProblemData([]);
        }
    };

    // 修改 fetchTestCaseData 函数，只获取 problemList 数据
    const fetchTestCaseData = async (domainIndexId: string) => {
        if (!domainIndexId) return;

        setIsLoadingTestCase(true);
        try {
            const selectedVehicleIds = vehicles
                .filter((item: any) => selectedBrands.includes(item.brandModel))
                .map((item: any) => String(item.id));

            if (selectedVehicleIds.length === 0) {
                setProblemData([]);
                return;
            }

            const response = await http.post<{ data: ProblemData[] }>('/vehicletestresult/list/multiple', {
                    functionIndexId: domainIndexId,
                    vehicleId: selectedVehicleIds,
                    language: language
            });

            if (response.code === 11012) {
                handleTokenExpired();
                return;
            }
            if (response.code === 11014) {
                handleTokenReplaced();
                return;
            }

            if (response && Array.isArray(response.data)) {
                setProblemData(response.data);
                // 设置默认选中的车型
                if (response.data.length > 0) {
                    const defaultVehicle = response.data.find(item => item.vehicleName === currentVehicle.brandModel)?.vehicleName
                        || response.data[0].vehicleName;
                    setSelectedVehicleForCase(defaultVehicle);
                }
            } else {
                setProblemData([]);
            }
        } catch (error) {
            setProblemData([]);
        } finally {
            setIsLoadingTestCase(false);
        }
    };

    // 获取当前选中车型的问题列表
    const getCurrentVehicleProblems = () => {
        const currentVehicleData = problemData.find(item =>
            item.vehicleName === selectedVehicleForCase ||
            (!selectedVehicleForCase && item.vehicleName === currentVehicle.brandModel)
        );
        return currentVehicleData?.problemList || [];
    };

    // 当选择车型时执行的函数
    const handleVehicleSelect = (brand: string) => {
        setSelectedVehicleForCase(brand);
    };

    // 获取当前选中车型的所有测试案例
    const getCurrentVehicleCases = () => {
        // 过滤出当前选中车型的测试案例，如果没有明确的车型信息，返回所有案例
        return problemData.filter(item =>
            !item.vehicleName || item.vehicleName === selectedVehicleForCase
        );
    };

    // 检查是否是试用车型
    const isTrialVehicle = (vehicle: any) => {
        return vehicle?.status === 3
    }

    // 检查是否应该限制访问
    const shouldRestrictAccess = (vehicle: any) => {
        return (token && accountStatus === '1' && !isTrialVehicle(vehicle))
    }

    // 获取当前雷达图点击指标的得分（支持多车型切换）
    const getSelectedMetricScore = () => {
        const metric = queryIndexScore.find(item => item.domainIndexName === selectedMetric);
        const brand = selectedVehicleForCase || currentVehicle.brandModel;
        if (metric && metric.vehicleIndexScore) {
            const vehicleScore = metric.vehicleIndexScore.find((v: any) => v.vehicleInfoName === brand);
            if (vehicleScore) {
                return Number(vehicleScore.vehicleInfoScore).toFixed(1);
            }
        }
        return "N/A";
    };

    return (
        <TabsContent value="domain" className="space-y-6">
            <Card className="bg-[#1C2028] border-gray-800">
                <CardContent className="p-6">
                    <div className="space-y-6">
                        <Tabs
                            value={selectedDomain}
                            onValueChange={(value) => {
                                setSelectedDomain(value)
                                queryIndexScoreChange(value)
                            }}
                        >
                            <TabsList className="grid grid-cols-6 gap-0 mb-4 bg-transparent px-[30px]">
                                {listData.map((item) => (
                                    <TabsTrigger
                                        key={item.functionDomainId}
                                        value={item.functionDomainId}
                                        className="text-base font-medium px-1 transition-all
                    data-[state=active]:bg-amber-500/10 data-[state=active]:text-amber-500 
                    data-[state=active]:text-lg data-[state=active]:font-bold 
                    data-[state=active]:shadow-sm data-[state=active]:border-[1.5px] 
                    data-[state=active]:border-amber-500/20 data-[state=active]:rounded-md"
                                    >
                                        {item.functionDomainName}
                                    </TabsTrigger>
                                ))}
                            </TabsList>
                        </Tabs>

                        <div className="h-[300px] w-full">
                            <ResponsiveContainer width="100%" height="100%">
                                <BarChart
                                    data={listData.map(domain => {
                                        // Initialize the new object for list1
                                        const result: any = {
                                            name: domain.functionDomainName,
                                            industryAverage: Number(domain.industryAvgScore)
                                        };
                                        // For each selected brand, get its score and add it to the result
                                        selectedBrands.forEach(brand => {
                                            const brandScore = domain.vehicleScore.find((vehicle: any) => vehicle.vehicleInfoName === brand);
                                            result[brand] = brandScore ? Number(brandScore.vehicleInfoScore) : 0; // Convert to number
                                        });
                                        // If the brand model is selected, add a "score" field with the current vehicle's score
                                        if (selectedBrands.includes(currentVehicle.brandModel)) {
                                            const currentBrandScore = domain.vehicleScore.find((vehicle: any) => vehicle.vehicleInfoName === currentVehicle.brandModel);
                                            result.score = currentBrandScore ? Number(currentBrandScore.vehicleInfoScore) : 0; // Convert to number
                                        }
                                        return result;
                                    })}
                                    layout="horizontal"
                                    margin={{ top: 0, right: 50, left: -30, bottom: 0 }}
                                    barSize={32}
                                    barGap={5}
                                >
                                    <CartesianGrid
                                        strokeDasharray="3 3"
                                        stroke="rgba(255,255,255,0.1)"
                                        opacity={0.05}
                                        horizontal={true}
                                        vertical={false}
                                    />
                                    <XAxis dataKey="name" type="category" tickLine={false} axisLine={false} tick={false} />
                                    <YAxis type="number" domain={[0, 5]} tickLine={false} axisLine={false} tick={false} />
                                    <Tooltip
                                        contentStyle={{
                                            backgroundColor: '#1C2028',
                                            border: '1px solid rgba(255,255,255,0.1)',
                                            borderRadius: '8px'
                                        }}
                                        labelStyle={{
                                            color: '#FF6B00',
                                            fontWeight: 'bold'
                                        }}
                                        itemStyle={{
                                            fontWeight: 'bold'
                                        }}
                                        formatter={(value: number, name: string) => [
                                            `${(Number(value)).toFixed(1)}分`,
                                            name
                                        ]}
                                    />
                                    <Bar dataKey={currentVehicle.brandModel} name={currentVehicle.brandModel} radius={[4, 4, 0, 0]} fill="#FF6B00">
                                        <LabelList
                                            dataKey={currentVehicle.brandModel}
                                            position="top"
                                            offset={10}
                                            formatter={(value: number) => (value).toFixed(1)}
                                            style={{
                                                fontSize: '14px',
                                                fontWeight: '600',
                                                fill: '#FF6B00'
                                            }}
                                        />
                                    </Bar>
                                    {selectedBrands
                                        .filter((brand) => brand !== currentVehicle.brandModel)
                                        .map((brand) => (
                                            <Bar key={brand} dataKey={brand} name={brand} radius={[4, 4, 0, 0]} fill={getBrandColor(brand)}>
                                                <LabelList
                                                    dataKey={brand}
                                                    position="top"
                                                    offset={10}
                                                    formatter={(value: number) => (value).toFixed(1)}
                                                    style={{
                                                        fontSize: '14px',
                                                        fontWeight: '600',
                                                        fill: getBrandColor(brand)
                                                    }}
                                                />
                                            </Bar>
                                        ))}
                                    {showIndustryAverage && (
                                        <Bar dataKey="industryAverage" name={language === 'en' ? 'Industry Average' : '行业均值'} radius={[4, 4, 0, 0]} fill="#6B7280">
                                            <LabelList
                                                dataKey="industryAverage"
                                                position="top"
                                                offset={10}
                                                formatter={(value: number) => (value).toFixed(1)}
                                                style={{
                                                    fontSize: '14px',
                                                    fontWeight: '500',
                                                    fill: '#9CA3AF'
                                                }}
                                            />
                                        </Bar>
                                    )}
                                </BarChart>
                            </ResponsiveContainer>
                        </div>

                        {/* 详细数据分析部分 */}
                        {selectedDomain && (
                            <div className="mt-16">
                                <div className="grid grid-cols-2 gap-8">
                                    {/* 雷达图部分 */}
                                    <div className="min-h-[430px]">
                                        <ResponsiveContainer width="100%" height={430}>
                                            <RadarChart
                                                data={generateList(queryIndexScore)}
                                                margin={{ top: 20, right: 30, bottom: 20, left: 30 }}
                                                onClick={(data) => {
                                                    if (data && data.activePayload && data.activePayload[0]) {
                                                        const subject = data.activePayload[0].payload.subject;
                                                        setSelectedMetric(subject as MetricKey);

                                                        // 查找点击指标对应的domainIndexId并输出到控制台
                                                        const clickedMetric = queryIndexScore.find(item => item.domainIndexName === subject);
                                                        if (clickedMetric) {
                                                            const domainIndexId = clickedMetric.domainIndexId;
                                                            setIndexDetail(clickedMetric.indexDetail); // 新增：同步设置指标详情
                                                            setSelectedDomainIndexId(domainIndexId);
                                                            fetchTestCaseData(domainIndexId);
                                                        }
                                                    }
                                                }}
                                            >
                                                <PolarGrid stroke="rgba(255,255,255,0.1)" />
                                                <PolarAngleAxis
                                                    dataKey="subject"
                                                    tick={{
                                                        fill: '#9CA3AF',
                                                        fontSize: 12
                                                    }}
                                                />
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
                                                        borderRadius: '8px'
                                                    }}
                                                    formatter={(value: number, name: string) => [
                                                        `${(Number(value)).toFixed(1)}${translations[language].points}`,
                                                        name
                                                    ]}
                                                    labelStyle={{
                                                        color: '#FF6B00',
                                                        fontWeight: 'bold'
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
                                                        fill: '#FF6B00',
                                                        strokeWidth: 2,
                                                        cursor: 'pointer'
                                                    }}
                                                    activeDot={{
                                                        r: 6,
                                                        fill: '#FF6B00',
                                                        strokeWidth: 2,
                                                        cursor: 'pointer'
                                                    }}
                                                />

                                                {/* 对比车型 */}
                                                {selectedBrands
                                                    .filter((brand) => brand !== currentVehicle.brandModel)
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
                                                                cursor: 'pointer'
                                                            }}
                                                            activeDot={{
                                                                r: 6,
                                                                fill: getBrandColor(brand),
                                                                strokeWidth: 2,
                                                                cursor: 'pointer'
                                                            }}
                                                        />
                                                    ))}

                                                {/* 行业均值 */}
                                                {showIndustryAverage && (
                                                    <Radar
                                                        name={language === 'en' ? 'Industry Average' : '行业均值'}
                                                        dataKey='行业均值'
                                                        stroke="#6B7280"
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
                                            </RadarChart>
                                        </ResponsiveContainer>
                                    </div>

                                    {/* 测试案例展示 */}
                                    <div className="bg-[#1C2028] border border-gray-800 rounded-lg overflow-hidden">
                                        {isLoadingTestCase ? (
                                            <div className="flex flex-col items-center justify-center h-[430px] text-gray-400 space-y-4">
                                                <div className="animate-spin w-8 h-8 border-2 border-amber-500 border-opacity-50 rounded-full border-t-transparent"></div>
                                                <p>正在加载数据...</p>
                                            </div>
                                        ) : problemData.length > 0 ? (
                                            <div className="flex flex-col h-full">
                                                        {/* 头部信息 */}
                                                        <div className="px-6 pt-6 pb-6 border-b border-gray-800">
                                                            <div className="flex items-center justify-between">
                                                                <div>
                                                                    <h3 className="text-lg font-semibold text-white">
                                                                {selectedMetric}
                                                                    </h3>
                                                            <p className="text-sm text-gray-400">
                                                                {indexDetail && (
                                                                    <div className="text-sm text-gray-400 mt-2 whitespace-pre-line leading-relaxed w-[380px]">
                                                                        {indexDetail}
                                                                    </div>
                                                                )}
                                                                    </p>
                                                                </div>
                                                                    <div className="flex flex-col items-end gap-1">
                                                                        <div className="flex items-center gap-3">
                                                                            <div className="text-sm text-gray-400">{translations[language].score}</div>
                                                                            <div className="relative">
                                                                                <div className="w-16 h-16 rounded-xl bg-gradient-to-br from-amber-400/20 to-amber-600/20 border border-amber-500/20 flex items-center justify-center">
                                                                                    <span className="text-3xl font-bold bg-gradient-to-br from-amber-200 to-amber-500 bg-clip-text text-transparent">
                                                                            {getSelectedMetricScore()}
                                                                                    </span>
                                                                                </div>
                                                                                <div className="absolute -top-1 -right-1 w-4 h-4 rounded-full bg-amber-500/10 border border-amber-500/20 flex items-center justify-center">
                                                                                    <span className="text-[10px] text-amber-500 font-medium">{translations[language].points}</span>
                                                                                </div>
                                                                            </div>
                                                                        </div>

                                                                    </div>
                                                            </div>

                                                            {/* 添加车型选择器 - 当有多个车型时显示 */}
                                                            {selectedBrands.length > 1 && (
                                                                <div className="mt-4 pt-2 border-t border-gray-800/50 flex items-center">
                                                                    <div className="flex items-center gap-2 flex-wrap">
                                                                        <span className="text-xs text-gray-500">{translations[language].selectModel}:</span>
                                                                        {selectedBrands.map(brand => (
                                                                            <Button
                                                                                key={brand}
                                                                                variant="outline"
                                                                                size="sm"
                                                                                className={`h-7 px-3 text-xs rounded-full 
                                        ${selectedVehicleForCase === brand
                                                                                        ? 'bg-amber-500/10 text-amber-500 border-amber-500/20'
                                                                                : 'bg-black/20 text-gray-400 border-gray-800 hover:bg-black/30'
                                                                            }`}
                                                                                onClick={() => handleVehicleSelect(brand)}
                                                                            >
                                                                                {brand}
                                                                            </Button>
                                                                        ))}
                                                                    </div>
                                                                </div>
                                                            )}
                                                        </div>

                                                                    {/* 问题列表 */}
                                                <div className="p-6">
                                                                    <div className="space-y-3 max-h-[360px] overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-amber-500/20 scrollbar-track-black/20 hover:scrollbar-thumb-amber-500/30">
                                                                        {getCurrentVehicleProblems().length > 0 ? (
                                                                            getCurrentVehicleProblems().map((problem, index) => (
                                                                                <div
                                                                                    key={index}
                                                                                    className="flex items-center justify-between p-4 rounded-lg bg-black/20 border border-gray-800"
                                                                                >
                                                                    <div className="text-gray-200 max-w-[80%] whitespace-pre-line leading-loose">
                                                                                        {problem.question}
                                                                                    </div>
                                                                                    <div className="flex items-center gap-2">
                                                                                        <span className="text-sm text-gray-400">{translations[language].score}</span>
                                                                                        <div className="w-12 h-12 rounded-lg bg-gradient-to-br from-amber-400/20 to-amber-600/20 border border-amber-500/20 flex items-center justify-center">
                                                                                            <span className="text-xl font-bold bg-gradient-to-br from-amber-200 to-amber-500 bg-clip-text text-transparent">
                                                                                                {problem.score.toFixed(1)}
                                                                                            </span>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            ))
                                                                        ) : (
                                                                            <div className="flex flex-col items-center justify-center h-[200px] text-gray-400 space-y-4">
                                                                                <FileX className="w-8 h-8 text-gray-500" />
                                                                                <p>{translations[language].noDataAvailable}</p>
                                                                            </div>
                                                                        )}
                                                                    </div>
                                                                </div>
                                            </div>
                                        ) : (
                                            <div className="flex flex-col items-center justify-center h-[430px] text-gray-400 space-y-4">
                                                <MousePointerClick className="w-8 h-8 text-gray-500" />
                                                <p>{translations[language].clickRadarChartToViewTestCaseDetails}</p>
                                            </div>
                                        )}
                                    </div>

                                </div>
                            </div>
                        )}
                    </div>
                </CardContent>
            </Card>
            {/* Image Preview Modal */}
            {isImageModalOpen && problemData.length > 0 && (
                <div
                    className="fixed inset-0 bg-black/75 flex items-center justify-center z-[100]"
                    onClick={() => setIsImageModalOpen(false)}
                >
                    <div
                        className="relative max-w-[90%] max-h-[85vh] bg-[#1C2028] rounded-lg p-4 flex items-center justify-center m-auto"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <Button
                            variant="ghost"
                            size="icon"
                            onClick={() => setIsImageModalOpen(false)}
                            className="absolute top-2 right-2 z-50 bg-black/50 hover:bg-black/70 text-white"
                        >
                            <X className="h-5 w-5" />
                        </Button>
                        <div className="flex items-center justify-center w-full h-full min-h-[200px]">
                            <img
                                src={problemData[0]?.vehicleName || ""}
                                alt="测试案例截图"
                                className="object-contain rounded-lg max-w-full max-h-[70vh]"
                                onError={(e) => {
                                    // console.error("模态框图片加载失败:", problemData[0]?.vehicleName);
                                    // 不要自动关闭模态框，而是显示错误信息
                                    e.currentTarget.style.display = "none";
                                    const parent = e.currentTarget.parentElement;
                                    if (parent) {
                                        parent.innerHTML += '<div class="absolute inset-0 flex items-center justify-center text-gray-400">图片加载失败</div>';
                                    }
                                }}
                            />
                        </div>
                    </div>
                </div>
            )}
        </TabsContent >
    )
}
