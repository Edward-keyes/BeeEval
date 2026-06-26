'use client'

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs'
import React, { useState, useRef, useEffect } from 'react'
import { Eye, EyeOff } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { cn } from '@/lib/utils'
import { CapabilityAssessmentProps, vehicleNameMapping } from './capability-assessment-data'
import { CapabilityAssessmentBasic } from './capability-assessment-basic'
import { CapabilityAssessmentDomain } from './capability-assessment-domain'
import { CapabilityAssessmentMBTI } from './capability-assessment-mbti'
import { Checkbox } from '@/components/ui/checkbox'
import http from '@/utils/request'
import { handleTokenExpired, handleTokenReplaced } from '@/utils/auth'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'

// 添加指标详情接口的类型
interface IndexDetail {
    indexName: string;
    detail: string;
}

interface IndexDetailData {
    cognitiveAbility: IndexDetail[];
    actionAbility: IndexDetail[];
}

export const CapabilityAssessment: React.FC<CapabilityAssessmentProps> = ({
    showIndustryAverage,
    selectedBrands,
    vehicles,
    currentVehicle = { brandModel: '' },
    onBrandsChange
}) => {
    const [showIndustryAverageState, setShowIndustryAverageState] = useState(showIndustryAverage)
    const [brandSelectOpen, setBrandSelectOpen] = useState(false)
    const brandSelectRef = useRef<HTMLDivElement>(null)
    const [assessmentData, setAssessmentData] = useState<any[]>([])
    const [indexDetails, setIndexDetails] = useState<IndexDetailData>({ cognitiveAbility: [], actionAbility: [] })
    const { language } = useLanguage()
    const isEnglish = language === 'en';

    // 获取显示名称函数
    const getDisplayName = (name: string): string => {
        return isEnglish ? (vehicleNameMapping.en[name] || name) : name;
    };

    const fetchData = async () => {
        try {
            const res = await http.post<any[]>('/vehicle/domainindex/bigModelCapabilityAssessmentBase', {
                ids: vehicles.map((item: any) => item.id),
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

            setAssessmentData(res.data)
        } catch (error) {
            // console.error('获取车辆列表失败:', error)
        }
    }

    // 添加获取指标详情的函数
    const fetchIndexDetails = async () => {
        try {
            const res = await http.post<IndexDetailData>('/vehicle/domainindex/bigModelIndexDetail', {
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

            setIndexDetails(res.data)
        } catch (error) {
            // console.error('获取指标详情失败:', error)
        }
    }

    // 添加点击外部关闭的逻辑
    useEffect(() => {
        fetchData()
        fetchIndexDetails()
        const handleClickOutside = (event: MouseEvent) => {
            if (brandSelectRef.current && !brandSelectRef.current.contains(event.target as Node)) {
                setBrandSelectOpen(false)
            }
        }
        document.addEventListener('mousedown', handleClickOutside)
        return () => {
            document.removeEventListener('mousedown', handleClickOutside)
        }
    }, [])

    // 监听语言变化，重新获取数据
    useEffect(() => {
        fetchData()
        fetchIndexDetails()
    }, [language])

    // 处理品牌选择
    const handleBrandToggle = (item: any) => {
        if (item.brandModel === currentVehicle.brandModel) {
            return // 当前车型不允许取消选择
        }

        if (selectedBrands.includes(item.brandModel)) {
            onBrandsChange(selectedBrands.filter((b) => b !== item.brandModel))
        } else {
            if (selectedBrands.length >= 3) {
                return // 最多选择3个品牌
            }
            onBrandsChange([...selectedBrands, item.brandModel])
        }
    }

    // 为每个品牌生成不同的颜色
    const getBrandColor = (brand: string) => {
        if (brand === currentVehicle.brandModel) {
            return '#FF6B00'
        }
        const comparisonBrands = selectedBrands.filter((b) => b !== currentVehicle.brandModel)
        const index = comparisonBrands.indexOf(brand)
        return index === 0 ? '#326199' : '#4FB1A1'
    }
    return (
        <Card className="overflow-hidden border-none shadow-xl bg-gradient-to-br from-background via-background to-muted/20">
            <CardHeader className="bg-gradient-to-b from-black to-[#151922] pb-4">
                <div className="flex items-center justify-between">
                    <CardTitle className="text-2xl font-bold text-primary">{translations[language].bigModelAbilityEvaluation}</CardTitle>
                    <div className="flex items-center gap-3">
                        <Button
                            variant="outline"
                            size="sm"
                            onClick={() => setShowIndustryAverageState(!showIndustryAverageState)}
                            className="h-10 px-6 text-sm font-medium rounded-md bg-black/20 text-gray-400 hover:bg-black/30 border-0 transition-colors"
                        >
                            {showIndustryAverageState ? (
                                <>
                                    <Eye className="w-4 h-4 mr-2" />
                                    {translations[language].hideIndustryAverage}
                                </>
                            ) : (
                                <>
                                    <EyeOff className="w-4 h-4 mr-2" />
                                    {translations[language].showIndustryAverage}
                                </>
                            )}
                        </Button>
                        <div className="relative" ref={brandSelectRef}>
                            <Button
                                variant="outline"
                                size="sm"
                                onClick={() => setBrandSelectOpen(!brandSelectOpen)}
                                className="h-10 px-6 text-sm font-medium rounded-md bg-black/20 text-gray-400 hover:bg-black/30 border-0 transition-colors"
                            >
                                {selectedBrands.length > 1 ? (
                                    <div className="flex items-center gap-2">
                                        <span>{getDisplayName(currentVehicle.brandModel)}</span>
                                        <span className="px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-500 text-xs">
                                            +{selectedBrands.length - 1}
                                        </span>
                                    </div>
                                ) : (
                                    translations[language].addComparisonModel
                                )}
                            </Button>
                            {brandSelectOpen && (
                                <div className="absolute right-0 mt-2 w-72 max-h-96 overflow-y-auto bg-[#1C2028] border border-gray-800 rounded-lg shadow-lg z-50">
                                    <div className="p-4">
                                        <div className="text-sm font-medium text-gray-400 mb-2">{translations[language].selectModelsToCompare}</div>
                                        <div className="space-y-2">
                                            {vehicles.map((item: any) => {
                                                const isSelected = selectedBrands.includes(item.brandModel)
                                                const isCurrentVehicle = item.brandModel === currentVehicle.brandModel
                                                const isDisabled = !isSelected && selectedBrands.length >= 3

                                                return (
                                                    <div
                                                        key={item.id}
                                                        className={cn(
                                                            'flex items-center gap-2 p-2 rounded-md cursor-pointer',
                                                            isSelected ? 'bg-amber-500/10' : 'hover:bg-gray-800/50',
                                                            isDisabled && 'opacity-50 cursor-not-allowed'
                                                        )}
                                                        onClick={() => {
                                                            if (isCurrentVehicle || isDisabled) return
                                                            handleBrandToggle(item)
                                                        }}
                                                    >
                                                        <Checkbox
                                                            checked={isSelected}
                                                            disabled={isDisabled || isCurrentVehicle}
                                                            className={cn(
                                                                'data-[state=checked]:bg-amber-500 data-[state=checked]:border-amber-500',
                                                                isCurrentVehicle && 'opacity-50'
                                                            )}
                                                        />
                                                        <span className="text-sm text-gray-300">
                                                            {getDisplayName(item.brandModel)}
                                                            {isCurrentVehicle && <span className="ml-2 text-xs text-amber-500">({translations[language].currentModel})</span>}
                                                        </span>
                                                    </div>
                                                )
                                            })}
                                        </div>
                                        <div className="mt-4 pt-4 border-t border-gray-800">
                                            <div className="text-xs text-gray-400">{translations[language].selected} {selectedBrands.length}/{translations[language].threeBrands}</div>
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
                <div className="flex items-center gap-2 mt-4">
                    <div className="flex items-center gap-2">
                        <span className="inline-block w-3 h-3 rounded-full" style={{ backgroundColor: '#FF6B00' }} />
                        <span className="text-sm text-muted-foreground">{getDisplayName(currentVehicle.brandModel)}</span>
                    </div>
                    {selectedBrands
                        .filter((brand) => brand !== currentVehicle.brandModel)
                        .map((brand, index) => (
                            <div key={brand} className="flex items-center gap-2">
                                <span className="inline-block w-3 h-3 rounded-full" style={{ backgroundColor: getBrandColor(brand) }} />
                                <span className="text-sm text-muted-foreground">{getDisplayName(brand)}</span>
                            </div>
                        ))}
                </div>
            </CardHeader>

            <CardContent className="p-8">
                <Tabs defaultValue="basic">
                    <TabsList className="flex w-full h-10 items-center gap-2 mb-8 bg-transparent">
                        <TabsTrigger
                            value="basic"
                            className="h-10 px-6 text-sm font-medium rounded-md bg-amber-500 text-black data-[state=active]:bg-amber-500 data-[state=active]:text-black data-[state=inactive]:bg-black/20 data-[state=inactive]:text-gray-400 transition-colors"
                        >
                            {translations[language].basicAbilities}
                        </TabsTrigger>
                        <TabsTrigger
                            value="domain"
                            className="h-10 px-6 text-sm font-medium rounded-md bg-black/20 text-gray-400 data-[state=active]:bg-amber-500 data-[state=active]:text-black data-[state=inactive]:bg-black/20 data-[state=inactive]:text-gray-400 transition-colors"
                        >
                            {translations[language].featureDomainPerformance}
                        </TabsTrigger>
                        <TabsTrigger
                            value="mbti"
                            className="h-10 px-6 text-sm font-medium rounded-md bg-black/20 text-gray-400 data-[state=active]:bg-amber-500 data-[state=active]:text-black data-[state=inactive]:bg-black/20 data-[state=inactive]:text-gray-400 transition-colors"
                        >
                            {translations[language].personalityTraits}
                        </TabsTrigger>
                    </TabsList>

                    <CapabilityAssessmentBasic
                        showIndustryAverage={showIndustryAverageState}
                        selectedBrands={selectedBrands}
                        currentVehicle={currentVehicle}
                        getBrandColor={getBrandColor}
                        assessmentData={assessmentData}
                        vehicles={vehicles}
                        indexDetails={indexDetails}
                    />

                    <CapabilityAssessmentDomain
                        showIndustryAverage={showIndustryAverageState}
                        selectedBrands={selectedBrands}
                        currentVehicle={currentVehicle}
                        getBrandColor={getBrandColor}
                        vehicles={vehicles}
                    />

                    <CapabilityAssessmentMBTI selectedBrands={selectedBrands} currentVehicle={currentVehicle} />
                </Tabs>
            </CardContent>
        </Card>
    )
}
