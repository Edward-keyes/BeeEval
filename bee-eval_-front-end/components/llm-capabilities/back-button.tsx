'use client'
import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { ChevronLeft, ChevronDown } from 'lucide-react'
import { useEffect, useState } from 'react'
import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuItem } from '@/components/ui/dropdown-menu'
import http from '@/utils/request'
import { useSearchParams } from 'next/navigation'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import { useAuthStore } from '@/store/useAuthStore'

interface BackButtonProps {
    onVehicleChange: (vehicle: string) => void
    defaultVehicle?: VehicleData
}

interface VehicleData {
    brandModel: string
    vehicleVersion: string
    vehicleSystemVersion: string
    testDate: string
    id: string
    status: number
}

export function BackButton({ onVehicleChange, defaultVehicle }: BackButtonProps) {
    const [selectedVehicle, setSelectedVehicle] = useState<VehicleData | null>(null)
    const [vehicles, setVehicles] = useState<VehicleData[]>([])
    const searchParams = useSearchParams()
    const id = searchParams.get('id')
    const { token, accountStatus } = useAuthStore()
    const { language } = useLanguage()

    useEffect(() => {
        fetchData()
    }, [id, token, accountStatus, language])

    const fetchData = async () => {
        const res = await http.post<VehicleData[]>('/ware/baseinfo/allVehicle', { language: language })
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
        // 根据 URL 中的 id 选中对应的车型
        if (filteredVehicles.length > 0) {
            const targetVehicle = filteredVehicles.find((vehicle) => vehicle.id === id) || filteredVehicles[0]
            setSelectedVehicle(targetVehicle)
            onVehicleChange(targetVehicle)
        }
    }
    const handleVehicleSelect = (vehicle: VehicleData) => {
        setSelectedVehicle(vehicle)
        onVehicleChange(vehicle as any)
    }

    return (
        <div className="sticky top-[64px] z-40">
            <div className="bg-[#1C2028] rounded-lg border border-gray-800">
                <div className="px-6 py-4">
                    <div className="flex justify-between items-center">
                        <div className="flex items-center space-x-6">
                            <Link href="/">
                                <Button variant="ghost" size="sm" className="flex items-center text-gray-400 hover:text-gray-300">
                                    <ChevronLeft className="h-5 w-5" />
                                    <span>{translations[language].back}</span>
                                </Button>
                            </Link>
                            <div className="flex items-center space-x-4">
                                <div className="flex items-center gap-2">
                                    <h2 className="text-white">{selectedVehicle?.brandModel}</h2>
                                    <span className="text-sm text-gray-400">{selectedVehicle?.vehicleVersion}</span>
                                </div>
                                <DropdownMenu>
                                    <DropdownMenuTrigger asChild>
                                        <Button
                                            variant="ghost"
                                            size="sm"
                                            className="flex items-center gap-2 text-sm hover:text-amber-500 cursor-pointer transition-colors bg-amber-500/10 px-3 py-1.5 rounded-full border border-amber-500/20 hover:border-amber-500/50 hover:bg-amber-500/20"
                                        >
                                            <span className="text-amber-500 font-medium">{translations[language].switchCarModel}</span>
                                            <svg className="w-4 h-4 text-amber-500" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                                <path d="M6 9l6 6 6-6" />
                                            </svg>
                                        </Button>
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent
                                        align="end"
                                        className="w-[200px] p-2 bg-[#1C2028] border border-gray-800 rounded-lg shadow-lg"
                                    >
                                        {vehicles.map((vehicle: any) => (
                                            <DropdownMenuItem
                                                key={vehicle.id}
                                                onClick={() => handleVehicleSelect(vehicle)}
                                                className={`flex items-center px-2 py-1.5 rounded-md text-sm ${vehicle.id === selectedVehicle?.id
                                                    ? 'bg-amber-500/10 text-amber-500'
                                                    : 'text-gray-400 hover:bg-gray-800/50 hover:text-gray-300'
                                                    }`}
                                            >
                                                {vehicle.brandModel}
                                            </DropdownMenuItem>
                                        ))}
                                    </DropdownMenuContent>
                                </DropdownMenu>
                            </div>
                        </div>
                        <div className="flex items-center space-x-4">
                            <span className="text-sm text-gray-400">{translations[language].systemVersion}: {selectedVehicle?.vehicleSystemVersion}</span>
                            <span className="text-sm text-gray-400">{translations[language].updateTime}: {selectedVehicle?.testDate}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
