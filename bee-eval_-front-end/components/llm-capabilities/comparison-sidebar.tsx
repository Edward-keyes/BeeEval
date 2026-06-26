'use client'

import { Card, CardContent } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import Image from 'next/image'
import { motion } from 'framer-motion'
import { cn } from '@/lib/utils'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'

interface OtherVideoResponse {
    description: string
    functionLabel: string[]
    videoNumber: string | null
    brandModel: string
}

interface ComparisonSidebarProps {
    selectedCompetitor: number | null
    onCompetitorSelect: (index: number) => void
    selectedCategory: string
    selectedSubcategory: string
    selectedItem: string
    currentVehicle: any
    otherVehicles: OtherVideoResponse[]
    isLoading?: boolean
}

export function ComparisonSidebar({
    selectedCompetitor,
    onCompetitorSelect,
    selectedCategory,
    selectedSubcategory,
    selectedItem,
    currentVehicle,
    otherVehicles,
    isLoading
}: ComparisonSidebarProps) {
    const { language } = useLanguage()
    return (
        <div className="space-y-4">
            <h3 className="text-lg font-semibold text-gray-200">{translations[language].competitiveProductComparison}</h3>
            {isLoading ? (
                <div className="flex items-center justify-center h-[200px]">
                    <div className="flex items-center space-x-2">
                        <div className="w-3 h-3 bg-amber-500 rounded-full animate-pulse"></div>
                        <div className="w-3 h-3 bg-amber-500 rounded-full animate-pulse delay-150"></div>
                        <div className="w-3 h-3 bg-amber-500 rounded-full animate-pulse delay-300"></div>
                    </div>
                </div>
            ) : otherVehicles.length === 0 ? (
                <div className="text-center text-gray-400 py-8">暂无竞品数据</div>
            ) : (
                <div className="grid grid-cols-2 gap-4">
                    {otherVehicles.map((vehicle, index) => (
                        <button
                            key={index}
                            onClick={() => onCompetitorSelect(index)}
                            className={cn(
                                'flex flex-col items-center justify-center p-4 rounded-lg border transition-colors',
                                selectedCompetitor === index
                                    ? 'border-amber-500 bg-amber-500/5'
                                    : 'border-gray-800 hover:border-gray-700 bg-[#1C2028] hover:bg-[#252933]'
                            )}
                        >
                            <div className="text-base font-medium text-gray-200">{vehicle.brandModel}</div>
                            <div className="text-sm text-gray-500 mt-1.5">{translations[language].open}</div>
                        </button>
                    ))}
                </div>
            )}
        </div>
    )
}
