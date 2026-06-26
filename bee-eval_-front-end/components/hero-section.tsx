'use client'

import { useState, useEffect, useMemo } from 'react'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { ChevronRight, Car, ListChecks, BarChart2, FileText, ChevronLeft } from 'lucide-react'
import Link from 'next/link'
import { motion } from 'framer-motion'
import Image from 'next/image'
import { Card, CardContent } from '@/components/ui/card'
import { cn } from '@/lib/utils'
import http from '@/utils/request'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import { toast } from 'sonner'
import { useRouter } from 'next/navigation'
import { useAuthStore } from '@/store/useAuthStore'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"

// 试用账号可查看的车型配置
const TRIAL_VEHICLES = {
    description: '当前账号为试用账号，仅支持查看试用车型'
}

interface CarModel {
    id: number
    brand: string
    name: string
    version: string
    systemVersion: string
    updateDate: string
    launchDate: string
    range: string
    modelFeatures: string
    energyType: string
    chipInformation: string
    conversationDuration: number
    evaluationMetrics: number
    llmSkills: number
    image: string
    status: number  // 1: 正常, 2: 敬请期待, 3: 试用车型
}

type SelectedModel = CarModel

export function HeroSection() {
    const [mounted, setMounted] = useState(false)
    const [selectedModel, setSelectedModel] = useState<SelectedModel>({} as SelectedModel)
    const [startIndex, setStartIndex] = useState(0)
    const [windowWidth, setWindowWidth] = useState(0)
    const [carModels, setCarModels] = useState<CarModel[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const { language } = useLanguage()
    const router = useRouter()
    const { token, accountStatus, logout } = useAuthStore()
    const [isDialogOpen, setIsDialogOpen] = useState(false)

    // 重置状态并重新获取数据的函数
    const resetAndFetch = () => {
        // 清除状态
        setSelectedModel({} as SelectedModel)
        setCarModels([])
        // 重新调用接口刷新数据
        fetchData()
    }

    // 监听语言变化，重新获取数据
    useEffect(() => {
        if (mounted) {
            resetAndFetch()
        }
    }, [language, mounted])

    // Add debug logs
    useEffect(() => { }, [accountStatus, selectedModel, token])

    const fetchData = async () => {
        setIsLoading(true)
        try {
            const response: any = await http.post('/ware/baseinfo/allVehicleInfo', {
                language: language
            })

            if (response.data && response.data.length > 0) {
                response.data.map((item: any) => {
                    item.image = item.vehiclePicture
                    item.name = item.vehicleName
                    item.version = item.vehicleVersion
                    item.systemVersion = item.vehicleSystemVersion
                    item.updateDate = item.testDate
                    item.launchDate = item.timeToMarket
                    item.range = item.enduranceMileage
                    item.modelFeatures = item.modelName
                    item.energyType = item.energyType
                    item.chipInformation = item.chipInformation
                    item.conversationDuration = item.testIndicators
                    item.evaluationMetrics = item.testCases
                    item.status = item.status
                    item.llmSkills = item.bigModelFunctionCount
                    item.brand = ''
                })

                setCarModels(response.data)
                setSelectedModel(response.data[0])
            } else {
                setCarModels([])
                setSelectedModel({} as SelectedModel)
            }
        } catch (error) {
            // console.error('Error fetching data:', error)
            setCarModels([])
            setSelectedModel({} as SelectedModel)
        } finally {
            setIsLoading(false)
        }
    }

    useEffect(() => {
        setMounted(true)
        setWindowWidth(window.innerWidth)
        fetchData()

        const handleResize = () => setWindowWidth(window.innerWidth)
        window.addEventListener('resize', handleResize)
        return () => window.removeEventListener('resize', handleResize)
    }, [])

    const itemsPerView = useMemo(() => {
        if (!windowWidth) return 6
        return windowWidth < 640 ? 1 : windowWidth < 1024 ? 3 : 5
    }, [windowWidth])

    const maxStartIndex = useMemo(() => {
        return Math.max(0, carModels.length - itemsPerView)
    }, [carModels.length, itemsPerView])

    // Calculate visible models only when windowWidth is available
    const visibleModels = useMemo(() => {
        if (!windowWidth) return carModels.slice(0, 6)
        const end = Math.min(startIndex + itemsPerView, carModels.length)
        return carModels.slice(startIndex, end)
    }, [startIndex, windowWidth, carModels, itemsPerView])

    const handlePrevious = () => {
        setStartIndex((prev) => Math.max(0, prev - 1))
    }

    const handleNext = () => {
        if (!windowWidth) return
        setStartIndex((prev) => Math.min(maxStartIndex, prev + 1))
    }

    const handleViewDetails = (modelId: number, isCapabilities: boolean = false) => {
        if (!token) {
            toast(translations[language].pleaseLogin, {
                description: translations[language].loginRequiredToViewDetails,
                duration: 3000,
                style: {
                    background: '#FFC107',
                    color: '#000',
                },
            })
            router.push('/login')
            return
        }

        // 如果是试用账号且不是试用车型
        if (accountStatus === '1' && selectedModel?.status !== 3) {
            toast(translations[language].trialAccountNotSupported, {
                description: translations[language].trialAccountDescription,
                duration: 3000,
                style: {
                    background: '#FFC107',
                    color: '#000',
                },
            })
            return
        }

        // 根据不同的目标页面进行跳转
        const targetPath = isCapabilities ? `/llm-capabilities?id=${modelId}` : `/vehicle-detail?id=${modelId}`
        router.push(targetPath)
    }

    // 监听 token 变化，当 token 变化时重置状态并重新获取数据
    useEffect(() => {
        if (mounted) {
            // token 发生变化时（包括登录和退出登录）重置状态并重新获取数据
            // 退出登录之前先重置状态
            if (!token) {
                logout() // 确保所有存储的状态都被清除
            }
            // 然后重新获取数据
            resetAndFetch()
        }
    }, [token, mounted, logout])

    // Return null on server-side and initial render
    if (!mounted) {
        return null
    }

    return (
        <div className="relative bg-[#0D1117] pt-10 pb-16 mb-12 rounded-3xl shadow-md">
            <div className="container mx-auto px-4">
                <div className="flex flex-col md:flex-row justify-center items-start gap-8">
                    <div className="w-full max-w-7xl mx-auto">
                        <div className="flex-1 py-16">
                            <motion.div
                                className="grid md:grid-cols-1 lg:grid-cols-[1fr,320px] xl:grid-cols-[1fr,380px] 2xl:grid-cols-[1fr,420px] gap-4 sm:gap-6 items-stretch"
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.5 }}
                            >
                                {isLoading ? (
                                    <>
                                        <div className="flex flex-col gap-4">
                                            <div>
                                                <div className="h-10 w-3/4 bg-gray-700/50 rounded animate-pulse mb-2"></div>
                                                <div className="h-6 w-1/2 bg-gray-700/50 rounded animate-pulse"></div>
                                            </div>
                                            <div className="relative h-[290px] sm:h-[330px] md:h-[410px] lg:h-[410px] xl:h-[320px] rounded-lg overflow-hidden -ml-6 bg-gray-700/50 animate-pulse">
                                            </div>
                                        </div>
                                        <div className="flex flex-col justify-between h-full space-y-2 sm:space-y-3">
                                            <div className="bg-[#1C2028]/80 border border-gray-700/30 backdrop-blur-sm h-[280px] rounded-lg p-4">
                                                <div className="h-6 w-1/3 bg-gray-700/50 rounded animate-pulse mb-6"></div>
                                                <div className="grid grid-cols-2 gap-x-6 sm:gap-x-12 gap-y-5 sm:gap-y-4">
                                                    {[...Array(6)].map((_, i) => (
                                                        <div key={i}>
                                                            <div className="h-4 w-20 bg-gray-700/50 rounded animate-pulse mb-2"></div>
                                                            <div className="h-5 w-24 bg-gray-700/50 rounded animate-pulse"></div>
                                                        </div>
                                                    ))}
                                                </div>
                                            </div>
                                            <div className="grid grid-cols-3 gap-4 h-[80px]">
                                                {[...Array(3)].map((_, i) => (
                                                    <div key={i} className="bg-[#1C2028]/80 border border-gray-700/30 backdrop-blur-sm rounded-lg p-3">
                                                        <div className="h-3 w-16 bg-gray-700/50 rounded animate-pulse mb-2"></div>
                                                        <div className="h-6 w-12 bg-gray-700/50 rounded animate-pulse"></div>
                                                    </div>
                                                ))}
                                            </div>
                                            <div className="h-12 w-full bg-gray-700/50 rounded animate-pulse"></div>
                                        </div>
                                    </>
                                ) : (
                                    <>
                                        <div className="flex flex-col gap-4">
                                            <div>
                                                <h1 className="text-3xl sm:text-4xl md:text-5xl font-bold mb-2 sm:mb-0">{selectedModel?.name || translations[language].pleaseSelectModel}</h1>
                                                <p className="text-lg sm:text-xl md:text-2xl text-gray-300">{selectedModel?.version || ''}</p>
                                            </div>
                                            <div className="relative h-[290px] sm:h-[330px] md:h-[410px] lg:h-[410px] xl:h-[320px] rounded-lg overflow-hidden -ml-6">
                                                {selectedModel?.image ? (
                                                    <Image
                                                        src={selectedModel.image}
                                                        alt={`${selectedModel?.brand || ''} ${selectedModel?.name || '车型'}`}
                                                        fill
                                                        className="object-contain"
                                                        priority
                                                    />
                                                ) : (
                                                    <div className="w-full h-full flex items-center justify-center bg-gray-800/30">
                                                        <span className="text-gray-400">{translations[language].noImage}</span>
                                                    </div>
                                                )}
                                                {/* 在主展示区域添加试用车型标签 */}
                                                {token && accountStatus === '1' && selectedModel?.status === 3 && (
                                                    <Badge
                                                        variant="secondary"
                                                        className="text-[11px] px-1.5 py-0.5 absolute top-2 right-2 bg-amber-500/80 text-black z-10"
                                                    >
                                                        {translations[language].trialVehicle}
                                                    </Badge>
                                                )}
                                            </div>
                                        </div>

                                        <div className="flex flex-col justify-between h-full space-y-2 sm:space-y-3">
                                            <Card className="bg-[#1C2028]/80 border border-gray-700/30 backdrop-blur-sm h-[330px]">
                                                <CardContent className="p-4">
                                                    <h2 className="text-lg sm:text-lg font-bold mb-3 sm:mb-6 text-white">{translations[language].basicInformation}</h2>
                                                    <div className="grid grid-cols-2 gap-x-6 sm:gap-x-12 gap-y-5 sm:gap-y-4">
                                                        <div>
                                                            <div className="text-gray-400 text-xs sm:text-sm mb-1">
                                                                {translations[language].systemVersion}
                                                            </div>
                                                            <div className="text-white text-sm sm:text-base font-medium truncate">
                                                                {selectedModel?.systemVersion || translations[language].emptyData}
                                                            </div>
                                                        </div>
                                                        <div>
                                                            <div className="text-gray-400 text-xs sm:text-sm mb-1">{translations[language].testTime}</div>
                                                            <div className="text-white text-sm sm:text-base font-medium truncate">
                                                                {selectedModel?.updateDate || translations[language].emptyData}
                                                            </div>
                                                        </div>
                                                        <div>
                                                            <div className="text-gray-400 text-xs sm:text-sm mb-1">{translations[language].releaseTime}</div>
                                                            <div className="text-white text-sm sm:text-base font-medium truncate">
                                                                {selectedModel?.launchDate || translations[language].emptyData}
                                                            </div>
                                                        </div>
                                                        <div>
                                                            <div className="text-gray-400 text-xs sm:text-sm mb-1">{translations[language].energyType}</div>
                                                            <div className="text-white text-sm sm:text-base font-medium truncate">
                                                                {selectedModel?.energyType || translations[language].emptyData}
                                                            </div>
                                                        </div>
                                                        <div>
                                                            <div className="text-gray-400 text-xs sm:text-sm mb-1">{translations[language].modelName}</div>
                                                            <div className="text-white text-sm sm:text-base font-medium truncate">
                                                                {selectedModel?.modelFeatures || translations[language].emptyData}
                                                            </div>
                                                        </div>
                                                        <div>
                                                            <div className="text-gray-400 text-xs sm:text-sm mb-1">{translations[language].chipInformation}</div>
                                                            <div className="text-white text-sm sm:text-base font-medium truncate">
                                                                {selectedModel?.chipInformation || translations[language].emptyData}
                                                            </div>
                                                        </div>
                                                        <div>
                                                            <div className="text-gray-400 text-xs sm:text-sm mb-1">{translations[language].supplier}</div>
                                                            <div onClick={() => setIsDialogOpen(true)} className="text-amber-500 text-sm sm:text-base font-medium truncate cursor-pointer hover:text-amber-400 transition-colors border-b border-dashed border-gray-600 hover:border-amber-400 inline-block">
                                                                {translations[language].viewDetailsPage}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </CardContent>
                                            </Card>

                                            <div className="grid grid-cols-3 gap-4 h-[80px]">
                                                <Card className="bg-[#1C2028]/80 border border-gray-700/30 backdrop-blur-sm">
                                                    <CardContent className="p-3">
                                                        <div className="text-gray-400 text-xs">{translations[language].testIndicators}</div>
                                                        <div className="text-lg sm:text-xl font-bold text-white mt-1">
                                                            {selectedModel?.conversationDuration || translations[language].emptyData}
                                                        </div>
                                                    </CardContent>
                                                </Card>
                                                <Card className="bg-[#1C2028]/80 border border-gray-700/30 backdrop-blur-sm">
                                                    <CardContent className="p-3">
                                                        <div className="text-gray-400 text-xs">{translations[language].testCases}</div>
                                                        <div className="text-lg sm:text-xl font-bold text-white mt-1">
                                                            {selectedModel?.evaluationMetrics || translations[language].emptyData}
                                                        </div>
                                                    </CardContent>
                                                </Card>
                                                <Card className="bg-[#1C2028]/80 border border-gray-700/30 backdrop-blur-sm">
                                                    <CardContent className="p-3">
                                                        <div className="text-gray-400 text-xs">{translations[language].bigModelFunction}</div>
                                                        <div className="text-lg sm:text-xl font-bold text-white mt-1">{selectedModel?.llmSkills || translations[language].emptyData}</div>
                                                    </CardContent>
                                                </Card>
                                            </div>

                                            <div className="grid grid-cols-2 gap-4">
                                                <Button
                                                    className={cn(
                                                        "w-full font-semibold",
                                                        (selectedModel?.status === 1 || selectedModel?.status === 3)
                                                            ? "bg-amber-500 hover:bg-amber-600 text-black cursor-pointer"
                                                            : "bg-gray-600 text-gray-400 cursor-not-allowed opacity-50"
                                                    )}
                                                    size="lg"
                                                    disabled={selectedModel?.status !== 1 && selectedModel?.status !== 3}
                                                    asChild={selectedModel?.status === 1 || selectedModel?.status === 3}
                                                >
                                                    {(selectedModel?.status === 1 || selectedModel?.status === 3) ? (
                                                        <div
                                                            onClick={() => handleViewDetails(selectedModel?.id || 0)}
                                                            className="flex items-center justify-center cursor-pointer"
                                                        >
                                                            {translations[language].viewModelEvaluation}
                                                            <ChevronRight className="ml-2 h-4 w-4" />
                                                        </div>
                                                    ) : (
                                                        <div className="flex items-center justify-center">
                                                            {translations[language].comingSoon}
                                                            <ChevronRight className="ml-2 h-4 w-4" />
                                                        </div>
                                                    )}
                                                </Button>
                                                <Button
                                                    className={cn(
                                                        "w-full font-semibold",
                                                        (selectedModel?.status === 1 || selectedModel?.status === 3)
                                                            ? "bg-amber-500 hover:bg-amber-600 text-black cursor-pointer"
                                                            : "bg-gray-600 text-gray-400 cursor-not-allowed opacity-50"
                                                    )}
                                                    size="lg"
                                                    disabled={selectedModel?.status !== 1 && selectedModel?.status !== 3}
                                                    asChild={selectedModel?.status === 1 || selectedModel?.status === 3}
                                                >
                                                    {(selectedModel?.status === 1 || selectedModel?.status === 3) ? (
                                                        <div
                                                            onClick={() => handleViewDetails(selectedModel?.id || 0, true)}
                                                            className="flex items-center justify-center cursor-pointer"
                                                        >
                                                            {translations[language].viewModelCapabilities}
                                                            <ChevronRight className="ml-2 h-4 w-4" />
                                                        </div>
                                                    ) : (
                                                        <div className="flex items-center justify-center">
                                                            {translations[language].comingSoon}
                                                            <ChevronRight className="ml-2 h-4 w-4" />
                                                        </div>
                                                    )}
                                                </Button>
                                            </div>
                                        </div>
                                    </>
                                )}
                            </motion.div>

                            {/* Car Model Carousel */}
                            <motion.div
                                className="mt-12 w-full"
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.5, delay: 0.4 }}
                            >
                                <div className="space-y-4">
                                    <div className="flex items-center justify-between">
                                        <h3 className="text-xl font-semibold text-amber-500">
                                            {translations[language].hotModels}
                                        </h3>
                                        <Link
                                            href="/all-models"
                                            className="flex items-center space-x-2 text-base text-gray-400 hover:text-amber-500"
                                        >
                                            <span className="font-medium">{translations[language].allVehicleModels}</span>
                                            <span className="text-amber-500">{carModels.length}</span>
                                            <ChevronRight className="h-4 w-4" />
                                        </Link>
                                    </div>
                                    <div className="relative">
                                        <div className="flex items-center justify-center relative">
                                            <Button
                                                variant="ghost"
                                                size="icon"
                                                onClick={handlePrevious}
                                                disabled={startIndex === 0 || isLoading}
                                                className="absolute -left-4 top-1/2 -translate-y-1/2 z-20 bg-[#0D1117]/90 backdrop-blur-sm text-white hover:text-amber-500 h-8 w-8 rounded-full shadow-lg"
                                            >
                                                <ChevronLeft className="h-4 w-4" />
                                            </Button>
                                            <div
                                                className={cn(
                                                    'absolute left-0 top-0 bottom-0 w-24 z-10 pointer-events-none',
                                                    'bg-gradient-to-r from-[#0D1117] to-transparent',
                                                    (startIndex === 0 || isLoading) && 'hidden'
                                                )}
                                            />
                                            <div
                                                className={cn(
                                                    'absolute right-0 top-0 bottom-0 w-24 z-10 pointer-events-none',
                                                    'bg-gradient-to-l from-[#0D1117] to-transparent',
                                                    (startIndex >= maxStartIndex || isLoading) && 'hidden'
                                                )}
                                            />
                                            <div className="flex space-x-4 md:space-x-5 lg:space-x-6 overflow-hidden py-4 md:py-6 lg:py-8 px-4">
                                                {isLoading ? (
                                                    // Skeleton loading for carousel items
                                                    [...Array(6)].map((_, index) => (
                                                        <div
                                                            key={index}
                                                            className="flex-shrink-0 text-center w-[160px] sm:w-[180px] md:w-[200px]"
                                                        >
                                                            <div className="relative w-[160px] h-[96px] sm:w-[180px] sm:h-[108px] md:w-[200px] md:h-[120px] mb-2 bg-gray-700/50 rounded-lg animate-pulse">
                                                            </div>
                                                            <div className="h-4 w-20 bg-gray-700/50 rounded animate-pulse mx-auto mb-2"></div>
                                                            <div className="h-5 w-32 bg-gray-700/50 rounded animate-pulse mx-auto"></div>
                                                        </div>
                                                    ))
                                                ) : (
                                                    visibleModels.map((model, index) => (
                                                        <motion.div
                                                            key={`${model.brand}-${model.name}`}
                                                            className="flex-shrink-0 text-center w-[160px] sm:w-[180px] md:w-[200px] cursor-pointer"
                                                            layout="preserve-aspect"
                                                            initial={{ opacity: 0, y: 20 }}
                                                            animate={{ opacity: 1, y: 0 }}
                                                            whileHover={{
                                                                scale: 1.05,
                                                                zIndex: 10,
                                                                transition: { duration: 0.2 }
                                                            }}
                                                            transition={{ duration: 0.5, delay: index * 0.05 }}
                                                            onClick={() => setSelectedModel(model)}
                                                        >
                                                            <motion.div
                                                                className={`relative w-[160px] h-[96px] sm:w-[180px] sm:h-[108px] md:w-[200px] md:h-[120px] mb-2 bg-[#0D1117]/50 rounded-lg overflow-hidden ${selectedModel.id === model.id
                                                                    ? 'border-2 border-amber-500'
                                                                    : 'border border-gray-800/50'
                                                                    }`}
                                                                whileHover={{
                                                                    boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
                                                                    transition: { duration: 0.2 }
                                                                }}
                                                            >
                                                                <div className="flex items-center justify-center h-full">
                                                                    <Image
                                                                        src={model.image.replace('height=60&width=100', 'height=120&width=200')}
                                                                        alt={`${model.brand} ${model.name}`}
                                                                        width={200}
                                                                        height={120}
                                                                        className="object-cover"
                                                                    />
                                                                </div>
                                                                {/* 在轮播图中添加试用车型标签 */}
                                                                {token && accountStatus === '1' && model?.status === 3 && (
                                                                    <Badge
                                                                        variant="secondary"
                                                                        className="text-[11px] px-1.5 py-0.5 absolute top-2 right-2 bg-amber-500/80 text-black z-10"
                                                                    >
                                                                        {translations[language].trialVehicle}
                                                                    </Badge>
                                                                )}
                                                            </motion.div>
                                                            <div
                                                                className={`text-sm mt-2 transition-all ${selectedModel.id === model.id ? 'text-amber-500 font-medium' : 'text-gray-400'
                                                                    }`}
                                                            >
                                                                {model.brand}
                                                            </div>
                                                            <div
                                                                className={`text-sm transition-all ${selectedModel.id === model.id
                                                                    ? 'text-amber-500 font-semibold text-base'
                                                                    : 'text-white font-medium'
                                                                    }`}
                                                            >
                                                                {model.name}
                                                            </div>
                                                        </motion.div>
                                                    ))
                                                )}
                                            </div>
                                            <Button
                                                variant="ghost"
                                                size="icon"
                                                onClick={handleNext}
                                                disabled={startIndex >= maxStartIndex || isLoading}
                                                className="absolute -right-4 top-1/2 -translate-y-1/2 z-20 bg-[#0D1117]/90 backdrop-blur-sm text-white hover:text-amber-500 h-8 w-8 rounded-full shadow-lg"
                                            >
                                                <ChevronRight className="h-4 w-4" />
                                            </Button>
                                        </div>
                                    </div>
                                </div>
                            </motion.div>
                        </div>
                    </div>
                </div>
            </div>

            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <DialogContent className="sm:max-w-md">
                    <DialogHeader>
                        <DialogTitle className="text-center text-xl">{translations[language].pleaseWait}</DialogTitle>
                    </DialogHeader>
                </DialogContent>
            </Dialog>
        </div>
    )
}
