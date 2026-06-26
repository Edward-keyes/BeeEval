'use client'

import { Button } from '@/components/ui/button'
import { Checkbox } from '@/components/ui/checkbox'
import { Header } from '@/components/header'
// import { VehicleDetailTabs } from "@/components/vehicle-detail/tabs"
import { CapabilityAssessment } from '@/components/vehicle-detail/capability-assessment'
import { UserFeedback } from '@/components/vehicle-detail/user-feedback'
import { PerformanceVideos } from '@/components/vehicle-detail/performance-videos'
import { useEffect, useState } from 'react'
import { ChevronLeft, Check } from 'lucide-react'
import { useRouter, useSearchParams } from 'next/navigation'
import http from '@/utils/request'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import { Footer } from "@/components/footer"
import { useAuthStore } from '@/store/useAuthStore'
import { handleTokenExpired, handleTokenReplaced } from '@/utils/auth'

interface SkillModule {
    name: string
    total: number
    available: number
}

interface CarModel {
    id: string
    brandModel: string
    vehicleVersion: string
    vehicleSystemVersion: string
    testDate: string
    status: number
}

interface VehicleDetail {
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
    chipInformation: string
    testCases: string
    bigModelFunctionCount: string
}

function CarSelector({
    selectedCar,
    isSelectOpen,
    onCarSelect,
    onToggleSelect,
    carModels
}: {
    selectedCar: CarModel
    isSelectOpen: boolean
    onCarSelect: (car: CarModel) => void
    onToggleSelect: () => void
    carModels: CarModel[]
}) {
    const { language } = useLanguage()
    return (
        <div className="relative">
            <div
                className="flex items-center gap-2 text-sm hover:text-amber-500 cursor-pointer transition-colors bg-amber-500/10 px-3 py-1.5 rounded-full border border-amber-500/20 hover:border-amber-500/50 hover:bg-amber-500/20"
                onClick={onToggleSelect}
            >
                <span className="text-amber-500 font-medium">{translations[language].switchCarModel}</span>
                <svg className="w-4 h-4 text-amber-500" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M6 9l6 6 6-6" />
                </svg>
            </div>
            {isSelectOpen && (
                <div className="absolute right-0 mt-2 w-64 max-h-96 overflow-y-auto bg-[#1C2028] border border-gray-800 rounded-lg shadow-lg z-50">
                    {carModels.map((car) => (
                        <div
                            key={car.id}
                            className="flex items-center gap-2 p-2 hover:bg-amber-500/10 hover:text-amber-500 rounded cursor-pointer"
                            onClick={() => {
                                onCarSelect(car)
                                onToggleSelect()
                            }}
                        >
                            <div
                                className={`w-4 h-4 border rounded flex items-center justify-center ${selectedCar.id === car.id ? 'bg-amber-500 border-amber-500' : 'border-gray-600'
                                    }`}
                            >
                                {selectedCar.id === car.id && <Check className="w-3 h-3 text-black" />}
                            </div>
                            <span className="text-sm text-gray-300">{car.brandModel}</span>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}

function VehicleInfoBar({
    currentVehicle,
    onVehicleChange,
    vehicleDetail,
    vehicles,
    onFetchDetail,
    onRichnessRatio
}: {
    currentVehicle: CarModel
    onVehicleChange: (vehicle: CarModel) => void
    vehicleDetail: VehicleDetail | null
    vehicles: CarModel[]
    onFetchDetail: (id: string) => void
    onRichnessRatio: (id: string) => void
}) {
    const [isSelectOpen, setIsSelectOpen] = useState(false)
    const router = useRouter()
    const { language } = useLanguage()

    return (
        <div className="sticky top-[64px] z-40 mt-4">
            <div className="container mx-auto px-4 md:px-6">
                <div className="bg-[#1C2028] rounded-lg border border-gray-800">
                    <div className="px-6 py-4">
                        <div className="flex justify-between items-center">
                            <div className="flex items-center space-x-6">
                                <button
                                    className="flex items-center text-gray-400 hover:text-gray-300"
                                    style={{ marginRight: 20 }}
                                    onClick={() => router.push('/')}
                                >
                                    <ChevronLeft className="h-5 w-5" />
                                    <span>{translations[language].back}</span>
                                </button>
                                <div className="flex items-center space-x-4">
                                    <div className="flex flex-col">
                                        <h2 style={{ marginLeft: 10, marginRight: 10, fontSize: 27, fontWeight: 'bold' }} className="text-white">{vehicleDetail?.vehicleName}</h2>
                                        <span style={{ marginLeft: 10 }} className="text-sm text-gray-400">{vehicleDetail?.vehicleVersion}</span>
                                    </div>
                                    <div className="flex items-center space-x-4">
                                        <CarSelector
                                            selectedCar={currentVehicle}
                                            isSelectOpen={isSelectOpen}
                                            onCarSelect={(car) => {
                                                onVehicleChange(car)
                                                onFetchDetail(car.id)
                                                onRichnessRatio(car.id)
                                            }}
                                            onToggleSelect={() => setIsSelectOpen(!isSelectOpen)}
                                            carModels={vehicles}
                                        />
                                    </div>
                                </div>
                            </div>
                            <div className="flex items-center space-x-8">
                                <div className="flex items-center text-sm text-gray-400">
                                    <span className="mr-2">{translations[language].chipInformation}:</span>
                                    <span>{vehicleDetail?.chipInformation || translations[language].emptyData}</span>
                                </div>
                                <div className="flex items-center text-sm text-gray-400">
                                    <span className="mr-2">{translations[language].systemVersion}:</span>
                                    <span>{vehicleDetail?.vehicleSystemVersion}</span>
                                </div>
                                <div className="flex items-center text-sm text-gray-400">
                                    <span className="mr-2">{translations[language].updateTime}:</span>
                                    <span>{vehicleDetail?.testDate}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

const VehicleDetailPage = () => {
    const searchParams = useSearchParams()
    const id = searchParams.get('id')
    const { token, accountStatus } = useAuthStore()
    const [vehicles, setVehicles] = useState<CarModel[]>([])
    const [currentVehicle, setCurrentVehicle] = useState<CarModel | null>(null)
    const [selectedBrands, setSelectedBrands] = useState<string[]>([])
    const [vehicleDetail, setVehicleDetail] = useState<VehicleDetail | null>(null)
    const [skillModules, setSkillModules] = useState<SkillModule[]>([])
    const { language } = useLanguage()
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        const initData = async () => {
            setIsLoading(true)
            await fetchData()
            if (id) {
                await fetchDetailData(id)
                await fetchFunctionRichnessRatio(id)
            }
            setIsLoading(false)
        }
        initData()
    }, [id])

    // 监听语言变化，重新获取数据
    useEffect(() => {
        const refreshData = async () => {
            setIsLoading(true)
            await fetchData()
            if (id) {
                await fetchDetailData(id)
                await fetchFunctionRichnessRatio(id)
            }
            setIsLoading(false)
        }
        refreshData()
    }, [language])

    useEffect(() => {
        if (vehicles.length > 0 && id) {
            const vehicle = vehicles.find((v) => v.id === id)
            if (vehicle) {
                setCurrentVehicle(vehicle)
                setSelectedBrands([vehicle.brandModel])
            }
        }
    }, [vehicles, id])

    const fetchData = async () => {
        try {
            const res = await http.post<CarModel[]>('/ware/baseinfo/allVehicle', {
                language: language
            })
            // 根据账号类型和车辆状态过滤车型
            const filteredVehicles = res.data.filter((vehicle) => {
                // status为2的车型永远不显示
                if (vehicle.status === 2) return false;

                // 如果是试用账号，只显示试用车型（status === 3）
                if (token && accountStatus === '1') {
                    return vehicle.status === 3;
                }

                // 如果是正式账号，显示status为1和3的车型
                return vehicle.status === 1 || vehicle.status === 3;
            });
            setVehicles(filteredVehicles)
        } catch (error) {
            // console.error('获取车辆列表失败:', error)
        }
    }

    const fetchDetailData = async (id: string) => {
        try {
            const res = await http.post<VehicleDetail>('/ware/baseinfo/vehicleInfoByVehicleId', {
                id: id,
                language: language
            })
            setVehicleDetail(res.data)
        } catch (error) {
            // console.error('获取车辆详情失败:', error)
        }
    }

    const fetchFunctionRichnessRatio = async (id: string) => {
        try {
            const res = await http.post<SkillModule[]>('/ware/functiontree/queryFunctionRichnessRatioByVehicleId', {
                id,
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

            setSkillModules(res.data)
        } catch (error) {
            // console.error('获取车辆功能丰富度失败:', error)
        }
    }

    const handleVehicleChange = (vehicle: CarModel) => {
        setCurrentVehicle(vehicle)
        if (!selectedBrands.includes(vehicle.brandModel)) {
            setSelectedBrands((prev) => [...prev.filter((brand) => brand !== currentVehicle?.brandModel), vehicle.brandModel])
        }
    }

    const handleBrandsChange = (brands: string[]) => {
        setSelectedBrands(brands)
    }

    if (isLoading) {
        return <div className="text-center text-gray-400 py-20">Loading...</div>
    }
    if (!isLoading && vehicles.length === 0) {
        return <div className="text-center text-gray-400 py-20">暂无可用车型，或当前账号无权限查看。</div>
    }
    if (!currentVehicle) {
        return <div className="text-center text-gray-400 py-20">未找到该车型信息。</div>
    }

    return (
        <main className="min-h-screen bg-[#171717]">
            <Header />
            <VehicleInfoBar
                currentVehicle={currentVehicle}
                onVehicleChange={handleVehicleChange}
                vehicleDetail={vehicleDetail}
                vehicles={vehicles}
                onFetchDetail={fetchDetailData}
                onRichnessRatio={fetchFunctionRichnessRatio}
            />
            <div className="container mx-auto px-4 md:px-6 mt-8">
                <div className="space-y-8">
                    <div className="bg-[#1C2028] rounded-lg border border-gray-800">
                        <div className="px-6 py-8">
                            <CapabilityAssessment
                                showIndustryAverage={true}
                                selectedBrands={selectedBrands}
                                currentVehicle={currentVehicle}
                                onBrandsChange={handleBrandsChange}
                                vehicles={vehicles}
                            />
                        </div>
                    </div>
                    <div className="bg-[#1C2028] rounded-lg border border-gray-800">
                        <div className="px-6 py-8">
                            <PerformanceVideos currentVehicle={currentVehicle} vehicleId={currentVehicle.id} />
                        </div>
                    </div>
                    <div style={{ marginBottom: '50px' }} className="bg-[#1C2028] rounded-lg border border-gray-800">
                        <div className="px-6 py-8">
                            <UserFeedback />
                        </div>
                    </div>
                </div>
            </div>
            <Footer />
        </main>
    )
}

export default VehicleDetailPage
