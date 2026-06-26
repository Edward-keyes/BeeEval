'use client'

import { Card, CardContent } from '@/components/ui/card'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'

interface SkillModule {
    functionName: string
    molecule: string
    denominator: string
    ratio: string
}

interface FeatureRichnessProps {
    skillModules: SkillModule[]
}

const SkillModuleCard = ({ module }: { module: SkillModule }) => {
    const percentage = parseFloat(module.ratio)
    const available = parseFloat(module.molecule)
    const total = parseFloat(module.denominator)
    const { language } = useLanguage()

    return (
        <Card className="bg-[#1C2028] border-gray-800 flex-1 min-w-[140px] max-w-[200px]">
            <CardContent className="p-2">
                <div className="text-gray-400 text-xs mb-1 whitespace-nowrap overflow-hidden text-ellipsis">{module.functionName}</div>
                <div className="text-base font-bold mb-1 bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">
                    {available}/{total}
                </div>
                <div className="space-y-1">
                    <div className="w-full bg-gray-800/50 rounded-full h-1.5">
                        <div className="bg-amber-500 h-1.5 rounded-full" style={{ width: `${percentage}%` }} />
                    </div>
                </div>
                <div className="text-xs text-gray-400 mt-1">{percentage}% </div>
            </CardContent>
        </Card>
    )
}

export function FeatureRichness({ skillModules }: FeatureRichnessProps) {
    const { language } = useLanguage()

    return (
        <div className="space-y-4">
            <Card className="bg-[#1C2028] border-gray-800">
                <div className="p-4">
                    <div className="space-y-3">
                        <h3 className="text-base font-semibold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">
                            {translations[language].featureRichness}
                        </h3>
                        <div className="flex gap-2 justify-between">
                            {skillModules.map((module) => (
                                <SkillModuleCard key={module.functionName} module={module} />
                            ))}
                        </div>
                    </div>
                </div>
            </Card>
        </div>
    )
} 