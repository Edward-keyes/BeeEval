'use client'

import { useEffect, useState } from 'react'
import { CapabilitiesSidebar } from './sidebar'
import { MainContent } from './main-content'
import { ComparisonSidebar } from './comparison-sidebar'
import { ComparisonModal } from './comparison-modal'
import { BackButton } from './back-button'
import { FeatureRichness } from './feature-richness'
import http from '@/utils/request'
import { handleTokenExpired, handleTokenReplaced } from '@/utils/auth'
import { useLanguage } from '@/constants/language'

interface VideoIntroResponse {
    videoId: string
    description: string
    functionLabel: string[]
    videoNumber: string | null
    videoStr?: string | null
    caseDataList?: {
        videoNumber: string
        functionLabel?: string[]
        materialType: number // 0: 图片, 1: 视频
    }[]
}

interface OtherVideoResponse {
    description: string
    functionLabel: string[]
    videoNumber: string | null
    brandModel: string
    videoStr?: string | null
}

interface SkillModule {
    functionName: string
    molecule: string
    denominator: string
    ratio: string
}

export function LLMCapabilitiesLayout() {
    const [selectedCategory, setSelectedCategory] = useState('用车助手')
    const [selectedSubcategory, setSelectedSubcategory] = useState('日程驾驶场景')
    const [selectedItem, setSelectedItem] = useState('车书展示')
    const [selectedCompetitor, setSelectedCompetitor] = useState<number | null>(null)
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [currentVehicle, setCurrentVehicle] = useState<any>({})
    const [videoIntro, setVideoIntro] = useState<VideoIntroResponse | null>(null)
    const [currentThreeTagId, setCurrentThreeTagId] = useState<string>('')
    const [otherVehicles, setOtherVehicles] = useState<OtherVideoResponse[]>([])
    const [skillModules, setSkillModules] = useState<SkillModule[]>([])
    const { language } = useLanguage()

    // 添加加载状态
    const [isVideoIntroLoading, setIsVideoIntroLoading] = useState(false)
    const [isOtherVehiclesLoading, setIsOtherVehiclesLoading] = useState(false)

    // 根据三级id与车辆id查询此车的视频与介绍
    const queryVideoByThreeTagIdAndVehicleId = async () => {
        if (!currentVehicle?.id || !currentThreeTagId) {
            setVideoIntro(null)
            return
        }

        setIsVideoIntroLoading(true)
        try {
            const res = await http.post<VideoIntroResponse>('/ware/functiononetag/queryVideoByThreeTagIdAndVehicleId', {
                vehicleId: currentVehicle.id + '',
                threeTagId: currentThreeTagId + '',
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

            // 确保即使返回空数据也会更新状态
            setVideoIntro(res.data || null)
        } catch (error) {
            // console.error('获取视频介绍失败:', error)
            setVideoIntro(null)
        } finally {
            setIsVideoIntroLoading(false)
        }
    }

    // 根据三级id查询其他车是否有此视频
    const queryOtherVideoByThreeTagId = async () => {
        if (!currentVehicle?.id || !currentThreeTagId) {
            setOtherVehicles([])
            return
        }

        setIsOtherVehiclesLoading(true)
        try {
            const res = await http.post<OtherVideoResponse[]>('/ware/functiononetag/queryOtherVideoByThreeTagId', {
                vehicleId: currentVehicle.id + '',
                threeTagId: currentThreeTagId + '',
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

            setOtherVehicles(res.data || [])
        } catch (error) {
            // console.error('获取其他车辆视频失败:', error)
            setOtherVehicles([])
        } finally {
            setIsOtherVehiclesLoading(false)
        }
    }

    // 获取功能丰富度数据
    const fetchFunctionRichnessRatio = async () => {
        if (!currentVehicle?.id) {
            setSkillModules([])
            return
        }

        try {
            const res = await http.post<SkillModule[]>('/ware/functiontree/queryFunctionRichnessRatioByVehicleId', {
                id: currentVehicle.id,
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

            setSkillModules(res.data || [])
        } catch (error) {
            // console.error('获取功能丰富度失败:', error)
            setSkillModules([])
        }
    }

    // 监听两个依赖值的变化
    useEffect(() => {
        queryVideoByThreeTagIdAndVehicleId()
        queryOtherVideoByThreeTagId()
        fetchFunctionRichnessRatio()
    }, [currentVehicle?.id, currentThreeTagId])

    // 监听语言变化，重新获取数据
    useEffect(() => {
        queryVideoByThreeTagIdAndVehicleId()
        queryOtherVideoByThreeTagId()
        fetchFunctionRichnessRatio()
    }, [language])

    const handleVehicleChange = (vehicle: any) => {
        setCurrentVehicle(vehicle)
        setSelectedCompetitor(null)
        setIsModalOpen(false)
    }

    const handleItemSelect = (item: string, threeTagId: string) => {
        // 清空之前的数据，避免显示旧数据
        setVideoIntro(null)
        setOtherVehicles([])

        // 即使是同一个标签，也强制刷新数据
        if (threeTagId === currentThreeTagId) {
            queryVideoByThreeTagIdAndVehicleId()
            queryOtherVideoByThreeTagId()
        }

        setSelectedItem(item)
        setCurrentThreeTagId(threeTagId)
    }

    const handleCompetitorSelect = (index: number) => {
        setSelectedCompetitor(index)
        setIsModalOpen(true)
    }

    const handleCloseModal = () => {
        setIsModalOpen(false)
        setSelectedCompetitor(null)
    }

    return (
        <div className="container mx-auto px-4 py-3 space-y-3">
            <BackButton
                defaultVehicle={currentVehicle}
                onVehicleChange={handleVehicleChange}
            />
            {skillModules.length > 0 && (
                <FeatureRichness skillModules={skillModules} />
            )}
            <div className="bg-[#1C2028] shadow-lg rounded-lg overflow-hidden">
                <div className="flex">
                    <div className="w-72 flex-shrink-0 bg-[#1C2028] border-r border-gray-800 shadow-inner">
                        <CapabilitiesSidebar
                            selectedCategory={selectedCategory}
                            selectedSubcategory={selectedSubcategory}
                            selectedItem={selectedItem}
                            onCategorySelect={setSelectedCategory}
                            onSubcategorySelect={setSelectedSubcategory}
                            onItemSelect={handleItemSelect}
                        />
                    </div>
                    <div className="flex-grow overflow-y-auto p-3 bg-[#1C2028] max-w-[calc(100vw-38rem)]">
                        <MainContent
                            category={selectedCategory}
                            subcategory={selectedSubcategory}
                            item={selectedItem}
                            showIndustryAverage={true}
                            currentVehicle={currentVehicle}
                            videoIntro={videoIntro}
                            isLoading={isVideoIntroLoading}
                        />
                    </div>
                    <div className="w-80 flex-shrink-0 border-l border-gray-800 bg-[#1C2028] p-3 overflow-y-auto">
                        <ComparisonSidebar
                            selectedCompetitor={selectedCompetitor}
                            onCompetitorSelect={handleCompetitorSelect}
                            selectedCategory={selectedCategory}
                            selectedSubcategory={selectedSubcategory}
                            selectedItem={selectedItem}
                            currentVehicle={currentVehicle}
                            otherVehicles={otherVehicles}
                            isLoading={isOtherVehiclesLoading}
                        />
                    </div>
                </div>
            </div>
            <ComparisonModal
                isOpen={isModalOpen}
                onClose={handleCloseModal}
                currentVehicle={{
                    brandModel: currentVehicle.brandModel || '',
                    description: videoIntro?.description,
                    functionLabel: videoIntro?.functionLabel,
                    videoNumber: videoIntro?.videoNumber || null,
                    videoStr: videoIntro?.videoStr || null,
                    caseDataList: (videoIntro as any)?.caseDataList || undefined,
                }}
                competitor={otherVehicles[selectedCompetitor || 0]}
                isLoading={isVideoIntroLoading || isOtherVehiclesLoading}
            />
        </div>
    )
}
