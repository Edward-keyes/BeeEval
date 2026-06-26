'use client'

import { Card, CardContent } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { cn } from '@/lib/utils'
import { ModelCapabilityAssessment } from './model-capability-assessment'
import { X, Play, Info, ChevronLeft, ChevronRight } from 'lucide-react'
import { useState, useRef, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { VideoPlayerModal } from '@/components/video-player-modal'
import Hls from 'hls.js'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'


interface MainContentProps {
    showIndustryAverage: boolean
    category: string
    subcategory: string
    item: string
    currentVehicle: {
        id: string
        name: string
        brandModel: string
    } | null
    videoIntro: {
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
    } | null
    isLoading?: boolean
}

export function MainContent({
    showIndustryAverage,
    category,
    subcategory,
    item,
    currentVehicle,
    videoIntro,
    isLoading
}: MainContentProps) {
    const [isVideoModalOpen, setIsVideoModalOpen] = useState(false)
    const videoRef = useRef<HTMLVideoElement>(null)
    const hlsRef = useRef<Hls | null>(null)
    const { language } = useLanguage()
    // 多视频切换逻辑
    const [currentCaseIndex, setCurrentCaseIndex] = useState(0);
    useEffect(() => {
        setCurrentCaseIndex(0);
    }, [videoIntro]);
    const videoList = Array.isArray((videoIntro as any)?.caseDataList) && (videoIntro as any).caseDataList.length > 0
        ? (videoIntro as any).caseDataList
        : videoIntro?.videoNumber
            ? [{ videoNumber: videoIntro.videoNumber, materialType: 1 }] // 默认为视频类型
            : [];
    const handlePrev = () => setCurrentCaseIndex(i => Math.max(0, i - 1));
    const handleNext = () => setCurrentCaseIndex(i => Math.min(videoList.length - 1, i + 1));
    // 当前案例的功能标签（新接口将functionLabel下放到caseDataList中）
    const currentCaseFunctionLabels: string[] = Array.isArray((videoIntro as any)?.caseDataList)
        ? (((videoIntro as any).caseDataList?.[currentCaseIndex]?.functionLabel) || [])
        : (videoIntro?.functionLabel || []);

    useEffect(() => {
        const cleanup = () => {
            if (hlsRef.current) {
                hlsRef.current.destroy()
                hlsRef.current = null
            }
            if (videoRef.current) {
                videoRef.current.src = ''
                videoRef.current.load()
            }
        }

        const currentVideoUrl = videoList[currentCaseIndex]?.videoNumber
        const currentMaterialType = videoList[currentCaseIndex]?.materialType

        if (videoRef.current && currentVideoUrl && currentMaterialType === 1) {
            cleanup()

            const hls = new Hls()
            hlsRef.current = hls

            if (Hls.isSupported()) {
                hls.loadSource(currentVideoUrl)
                hls.attachMedia(videoRef.current)
                hls.on(Hls.Events.MANIFEST_PARSED, () => {
                    // videoRef.current?.play() // 可选：自动播放
                })
            } else if (videoRef.current.canPlayType('application/vnd.apple.mpegurl')) {
                videoRef.current.src = currentVideoUrl
            }
        }

        return cleanup
    }, [videoList, currentCaseIndex])

    // 如果没有数据，显示提示信息
    if (!videoIntro) {
        return (
            <Card className="bg-[#1C2028] border-gray-800">
                <CardContent className="p-6">
                    <div className="flex flex-col items-center justify-center py-12 text-center">
                        <Info className="h-12 w-12 text-gray-500 mb-4" />
                        <p className="text-gray-400 text-lg">{translations[language].noFunctionConfiguration}</p>
                        <p className="text-gray-500 text-sm mt-2">{translations[language].noFunctionConfiguration}</p>
                    </div>
                </CardContent>
            </Card>
        )
    }

    return (
        <div className="space-y-6">
            {isLoading ? (
                <div className="flex items-center justify-center h-[300px]">
                    <div className="flex items-center space-x-2">
                        <div className="w-3 h-3 bg-amber-500 rounded-full animate-pulse"></div>
                        <div className="w-3 h-3 bg-amber-500 rounded-full animate-pulse delay-150"></div>
                        <div className="w-3 h-3 bg-amber-500 rounded-full animate-pulse delay-300"></div>
                    </div>
                </div>
            ) : (
                <Card className="bg-[#1C2028] border-gray-800">
                    {videoList.length > 0 ? (
                        <>
                            <div className="relative aspect-video">
                                {videoList[currentCaseIndex].materialType === 1 ? (
                                    <video
                                        ref={videoRef}
                                        src={videoList[currentCaseIndex].videoNumber}
                                        className="w-full h-full object-cover rounded-t-lg"
                                        muted
                                        playsInline
                                        controls
                                    />
                                ) : (
                                    <img
                                        src={videoList[currentCaseIndex].videoNumber}
                                        alt="功能展示"
                                        className="w-full h-full object-cover rounded-t-lg"
                                    />
                                )}
                                {videoList.length > 1 && (
                                    <>
                                        <div className="absolute bottom-3 left-1/2 -translate-x-1/2 z-10 select-none">
                                            <div className="px-3 py-1 rounded-full bg-black/60 text-white text-xs font-medium">
                                                {`${currentCaseIndex + 1}/${videoList.length}`}
                                            </div>
                                        </div>
                                        <button
                                            onClick={handlePrev}
                                            disabled={currentCaseIndex === 0}
                                            className="absolute left-3 top-1/2 -translate-y-1/2 z-10 h-10 w-10 rounded-full bg-white/90 text-black shadow hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
                                            aria-label="Previous"
                                        >
                                            <ChevronLeft className="h-6 w-6" />
                                        </button>
                                        <button
                                            onClick={handleNext}
                                            disabled={currentCaseIndex === videoList.length - 1}
                                            className="absolute right-3 top-1/2 -translate-y-1/2 z-10 h-10 w-10 rounded-full bg-white/90 text-black shadow hover:bg-white disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
                                            aria-label="Next"
                                        >
                                            <ChevronRight className="h-6 w-6" />
                                        </button>
                                    </>
                                )}
                                {videoList[currentCaseIndex].materialType === 1 && (
                                <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
                                    <Button
                                        variant="ghost"
                                        size="icon"
                                        className="h-16 w-16 rounded-full bg-amber-500/90 hover:bg-amber-500 text-black"
                                        onClick={() => setIsVideoModalOpen(true)}
                                    >
                                        <Play className="h-8 w-8" />
                                    </Button>
                                </div>
                                )}
                            </div>
                            <CardContent className="p-6">
                                <div className="space-y-6">
                                    <div className="space-y-4">
                                        <div className="flex justify-between items-start">
                                            <div className="flex-1">
                                                <h2 className="text-lg font-semibold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent mb-2">
                                                    {item}
                                                </h2>
                                                <p className="text-gray-400">{videoIntro.description}</p>
                                            </div>
                                            <div className="flex items-center gap-2 text-gray-400">
                                                <Info className="h-4 w-4" />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="border-t border-gray-800 pt-6">
                                        <h3 className="text-lg font-semibold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent mb-4">
                                            {translations[language].coreAbility}
                                        </h3>
                                        <ul className="list-disc pl-5 space-y-2">
                                            {currentCaseFunctionLabels.map((capability: string, index: number) => (
                                                <li key={index} className="text-sm text-gray-400">
                                                    {capability}
                                                </li>
                                            ))}
                                        </ul>
                                    </div>
                                </div>
                            </CardContent>
                        </>
                    ) : (
                        <CardContent className="p-6">
                            <div className="flex flex-col items-center justify-center py-12 text-center">
                                <Info className="h-12 w-12 text-gray-500 mb-4" />
                                <p className="text-gray-400 text-lg">{translations[language].noFunctionConfiguration}</p>
                                <p className="text-gray-500 text-sm mt-2">{translations[language].noFunctionConfiguration}</p>
                            </div>
                            <div className="mt-8 pt-8 border-t border-gray-800">
                                <h2 className="text-lg font-semibold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent mb-4">
                                    {item}
                                </h2>
                                <p className="text-gray-400">{videoIntro.description}</p>
                            </div>
                        </CardContent>
                    )}
                </Card>
            )}

            <ModelCapabilityAssessment showIndustryAverage={showIndustryAverage} />

            {videoList.length > 0 && (
                <VideoPlayerModal
                    isOpen={isVideoModalOpen}
                    onClose={() => setIsVideoModalOpen(false)}
                    video={{
                        videoId: videoIntro.videoId,
                        title: item,
                        category: subcategory,
                        brandName: currentVehicle?.brandModel || '',
                        videoUrl: videoList[currentCaseIndex].videoNumber,
                        subtitleUrl: videoIntro.videoStr || undefined
                    }}
                />
            )}
        </div>
    )
}
