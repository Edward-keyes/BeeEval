'use client'

import { PageLayout } from '@/components/layouts/page-layout'
import { AllModelsGrid } from '@/components/all-models/all-models-grid'
import { Button } from '@/components/ui/button'
import { BarChart2, ChevronDown } from 'lucide-react'
import { Card, CardContent } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Car, FileText, ChevronRight } from 'lucide-react'
import Link from 'next/link'
import Image from 'next/image'
import { useEffect, useState } from 'react'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import http from '@/utils/request'
import { useRouter } from 'next/navigation'
import { toast } from 'sonner'
import { useAuthStore } from '@/store/useAuthStore'
import { cn } from '@/lib/utils'
import { translations } from '@/language'
import { useLanguage } from '@/constants/language'


// 添加类型定义
interface VehicleInfo {
    id: string
    vehicleName: string
    vehicleVersion: string
    vehiclePicture: string
    vehicleSystemVersion: string
    testDate: string
    timeToMarket: string
    enduranceMileage: string
    modelName: string
    energyType: string
    testIndicators: string
    testCases: string
    bigModelFunctionCount: string
    status: number  // 1: 正常, 2: 敬请期待, 3: 试用车型
}

// 试用账号可查看的车型配置
const TRIAL_VEHICLES = {
    description: '当前账号为试用账号，仅支持查看试用车型'
}

export default function AllModelsPage() {
    const router = useRouter()
    const { token, accountStatus } = useAuthStore()
    const [sortType, setSortType] = useState<'default' | 'time'>('default')
    const [vehicles, setVehicles] = useState<VehicleInfo[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const { language } = useLanguage()

    // 检查是否是试用车型
    const isTrialVehicle = (vehicle: VehicleInfo) => {
        return vehicle.status === 3
    }

    // 检查是否应该限制访问
    const shouldRestrictAccess = (vehicle: VehicleInfo) => {
        return (token && accountStatus === '1' && !isTrialVehicle(vehicle))
    }

    const fetchData = async () => {
        try {
            setIsLoading(true)
            const response = await http.post<VehicleInfo[]>('/ware/baseinfo/allVehicleInfo', {
                language: language
            })
            setVehicles(response.data)
        } catch (error) {
            // console.error('Error fetching data:', error)
            toast.error('获取车型数据失败', {
                description: '请稍后重试',
                style: {
                    background: '#FFC107',
                    color: '#000'
                }
            })
        } finally {
            setIsLoading(false)
        }
    }

    // 监听语言变化，重新获取数据
    useEffect(() => {
        fetchData()
    }, [language])

    // 初始数据加载
    useEffect(() => {
        fetchData()
    }, [])

    const handleViewDetails = (vehicle: VehicleInfo) => {
        if (!token) {
            toast.error('请先登录', {
                description: '登录后即可查看详情',
                style: {
                    background: '#FFC107',
                    color: '#000'
                }
            })
            router.push('/login')
            return
        }

        // 检查是否是状态为2的车型（敬请期待）
        if (vehicle.status === 2) {
            toast.error('暂未开放', {
                description: '此车型正在加速测试中，敬请期待',
                style: {
                    background: '#FFC107',
                    color: '#000'
                }
            })
            return
        }

        // 检查是否是受限车辆（试用账号但不是试用车型）
        if (shouldRestrictAccess(vehicle)) {
            toast.error('试用账号暂不支持', {
                description: TRIAL_VEHICLES.description,
                style: {
                    background: '#FFC107',
                    color: '#000'
                }
            })
            return
        }

        router.push(`/vehicle-detail?id=${vehicle.id}`)
    }

    // 根据排序类型对车型进行排序
    const sortedVehicles = [...vehicles].sort((a, b) => {
        if (sortType === 'default') {
            return 0 // 保持原始顺序
        }
        // 时间排序：按测试日期从新到旧排序
        const dateA = new Date(a.testDate)
        const dateB = new Date(b.testDate)
        return dateB.getTime() - dateA.getTime()
    })

    return (
        <PageLayout activeItem="all-models" darkMode>
            <main className="flex-grow py-24">
                <div className="container mx-auto px-4 py-8">
                    <div className="flex justify-between items-center mb-12">
                        <div>
                            <h1 className="text-3xl font-bold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent mb-3">
                                {translations[language].allModels}
                            </h1>
                            <p className="text-gray-400">{translations[language].exploreAndUnderstandFeatures}</p>
                        </div>
                        <div className="flex items-center">
                            <DropdownMenu>
                                <DropdownMenuTrigger asChild>
                                    <Button
                                        variant="outline"
                                        className="bg-gradient-to-r from-[#353B47] to-[#2A303C] text-gray-300 hover:from-amber-500 hover:to-amber-600 hover:text-black border-gray-800/50 transition-all duration-300"
                                    >
                                        <BarChart2 className="mr-2 h-4 w-4" />
                                        {sortType === 'default' ? translations[language].defaultSort : translations[language].timeSort}
                                        <ChevronDown className="ml-2 h-4 w-4" />
                                    </Button>
                                </DropdownMenuTrigger>
                                <DropdownMenuContent align="end" className="w-32 bg-[#353B47] border-gray-800/50">
                                    <DropdownMenuItem
                                        onClick={() => setSortType('default')}
                                        className="text-gray-300 hover:bg-amber-500/90 hover:text-black focus:bg-amber-500/90 focus:text-black"
                                    >
                                        {translations[language].defaultSort}
                                    </DropdownMenuItem>
                                    <DropdownMenuItem
                                        onClick={() => setSortType('time')}
                                        className="text-gray-300 hover:bg-amber-500/90 hover:text-black focus:bg-amber-500/90 focus:text-black"
                                    >
                                        {translations[language].timeSort}
                                    </DropdownMenuItem>
                                </DropdownMenuContent>
                            </DropdownMenu>
                        </div>
                    </div>

                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {isLoading ? (
                            // 加载状态显示
                            Array.from({ length: 8 }).map((_, index) => (
                                <Card
                                    key={index}
                                    className="bg-gradient-to-b from-[#2A303C] to-[#1F242D] border border-gray-800/30 rounded-xl overflow-hidden shadow-lg animate-pulse"
                                >
                                    <CardContent className="p-0">
                                        <div className="relative h-48 bg-[#1C2028] overflow-hidden">
                                            <div className="absolute inset-0 bg-gradient-to-t from-black/40 to-transparent z-10" />
                                            <div className="absolute inset-0 bg-gray-700/50 animate-pulse" />
                                        </div>
                                        <div className="p-5">
                                            <div className="flex items-start justify-between mb-4">
                                                <div className="space-y-2">
                                                    <div className="h-6 w-32 bg-gray-700 rounded animate-pulse" />
                                                    <div className="h-4 w-24 bg-gray-700 rounded animate-pulse" />
                                                </div>
                                                <div className="h-6 w-16 bg-gray-700 rounded animate-pulse" />
                                            </div>
                                            <div className="space-y-3">
                                                <div className="flex items-center">
                                                    <div className="h-4 w-4 bg-gray-700 rounded mr-2 animate-pulse" />
                                                    <div className="h-4 w-32 bg-gray-700 rounded animate-pulse" />
                                                </div>
                                            </div>
                                            <div className="mt-6 pt-4 border-t border-gray-800/50">
                                                <div className="h-10 w-full bg-gray-700 rounded-lg animate-pulse" />
                                            </div>
                                        </div>
                                    </CardContent>
                                </Card>
                            ))
                        ) : (
                            sortedVehicles.map((vehicle) => (
                                <Card
                                    key={vehicle.id}
                                    className="group bg-gradient-to-b from-[#2A303C] to-[#1F242D] border border-gray-800/30 hover:border-amber-500/50 transition-all duration-300 rounded-xl overflow-hidden shadow-lg hover:shadow-amber-500/10"
                                >
                                    <CardContent className="p-0">
                                        <div className="relative h-48 bg-[#1C2028] overflow-hidden">
                                            <div className="absolute inset-0 bg-gradient-to-t from-black/40 to-transparent z-10" />
                                            <img
                                                src={vehicle.vehiclePicture}
                                                alt={vehicle.vehicleName}
                                                style={{
                                                    width: '87%',
                                                    height: '87%',
                                                    top: '50%',
                                                    left: '50%',
                                                    transform: 'translate(-50%, -50%)'
                                                }}
                                                className="object-contain absolute transform group-hover:scale-105 transition-transform duration-500"
                                            />
                                            {/* 添加试用车型标签 */}
                                            {token && accountStatus === '1' && vehicle.status === 3 && (
                                                <Badge
                                                    variant="secondary"
                                                    className="text-[11px] px-1.5 py-0.5 absolute top-2 right-2 bg-amber-500/80 text-black z-20"
                                                >
                                                    {translations[language].trialVehicle}
                                                </Badge>
                                            )}
                                        </div>
                                        <div className="p-5 bg-gradient-to-b from-[#2A303C]/50 to-[#1F242D]/50 backdrop-blur-sm">
                                            <div className="flex items-start justify-between mb-4">
                                                <div>
                                                    <h3 className="text-lg font-semibold text-white mb-1 group-hover:text-amber-400 transition-colors">
                                                        {vehicle.vehicleName}
                                                    </h3>
                                                    <p className="text-sm text-gray-400">{vehicle.vehicleVersion}</p>
                                                </div>
                                                <Badge variant="outline" className="border-amber-500/30 text-amber-500 bg-amber-500/5">
                                                    {vehicle.energyType}
                                                </Badge>
                                            </div>
                                            <div className="space-y-3">
                                                <div className="flex items-center text-sm">
                                                    <FileText className="h-4 w-4 text-amber-500/70 mr-2" />
                                                    <span className="text-gray-300">
                                                        {vehicle.status === 2 ? translations[language].comingSoon : `${translations[language].testDate}：${vehicle.testDate}`}
                                                    </span>
                                                </div>
                                            </div>
                                            <div className="mt-6 pt-4 border-t border-gray-800/50">
                                                <div className="grid grid-cols-2 gap-2">
                                                    <div
                                                        onClick={() => handleViewDetails(vehicle)}
                                                        className={cn(
                                                            "inline-flex items-center justify-center h-10 px-4 py-2.5 rounded-lg shadow-sm cursor-pointer transition-all duration-300",
                                                            vehicle.status === 2
                                                                ? "bg-gray-600 text-gray-400 cursor-not-allowed"
                                                                : "bg-gradient-to-r from-[#353B47] to-[#2A303C] hover:from-amber-500 hover:to-amber-600 text-gray-300 hover:text-black"
                                                        )}
                                                    >
                                                        <div className="flex items-center justify-center text-xs">
                                                            {vehicle.status === 2 ? translations[language].comingSoon : translations[language].viewModelEvaluation}
                                                            <ChevronRight className="ml-2 h-4 w-4" />
                                                        </div>
                                                    </div>
                                                    <div
                                                        onClick={() => vehicle.status !== 2 && router.push(`/llm-capabilities?id=${vehicle.id}`)}
                                                        className={cn(
                                                            "inline-flex items-center justify-center h-10 px-4 py-2.5 rounded-lg shadow-sm cursor-pointer transition-all duration-300",
                                                            vehicle.status === 2
                                                                ? "bg-gray-600 text-gray-400 cursor-not-allowed"
                                                                : "bg-gradient-to-r from-[#353B47] to-[#2A303C] hover:from-amber-500 hover:to-amber-600 text-gray-300 hover:text-black"
                                                        )}
                                                    >
                                                        <div className="flex items-center justify-center text-xs">
                                                            {vehicle.status === 2 ? translations[language].comingSoon : translations[language].viewModelCapabilities}
                                                            <ChevronRight className="ml-2 h-4 w-4" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </CardContent>
                                </Card>
                            ))
                        )}
                    </div>
                </div>
            </main>
        </PageLayout>
    )
}
