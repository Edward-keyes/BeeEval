'use client'

import { motion } from "framer-motion"
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import { useLanguage } from "@/constants/language"
import { translations } from "@/language"

// const { language } = useLanguage()

export default function ComparisonTable() {
    const { language } = useLanguage()

    // 使用类型断言解决TypeScript类型错误
    const trans = translations[language] as any;

    const comparisonData = [
        {
            feature: trans.vehicleCost,
            competitor: trans.compVehicleCostCompetitor,
            beeeval: trans.compVehicleCostBeeeval
        },
        {
            feature: trans.compDataAnalysis,
            competitor: trans.compDataAnalysisCompetitor,
            beeeval: trans.compDataAnalysisBeeeval
        },
        {
            feature: trans.compHighQualityVideo,
            competitor: trans.compHighQualityVideoCompetitor,
            beeeval: trans.compHighQualityVideoBeeeval
        },
        {
            feature: trans.compEvaluationVisualization,
            competitor: trans.compEvaluationVisualizationCompetitor,
            beeeval: trans.compEvaluationVisualizationBeeeval
        },
        {
            feature: trans.compCompetitiveBenchmarking,
            competitor: trans.compCompetitiveBenchmarkingCompetitor,
            beeeval: trans.compCompetitiveBenchmarkingBeeeval
        },
        {
            feature: trans.compCockpitMBTI,
            competitor: trans.compCockpitMBTICompetitor,
            beeeval: trans.compCockpitMBTIBeeeval
        },
        {
            feature: trans.compMultidimensionalRanking,
            competitor: trans.compMultidimensionalRankingCompetitor,
            beeeval: trans.compMultidimensionalRankingBeeeval
        },
        {
            feature: trans.compTotalCost,
            competitor: trans.compTotalCostCompetitor,
            beeeval: trans.compTotalCostBeeeval
        }
    ]

    return (
        <section className="bg-black text-white py-20">
            <div className="container mx-auto px-4">
                <div className="max-w-4xl mx-auto">
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5 }}
                        className="text-center mb-12"
                    >
                        <h2 className="text-3xl font-bold mb-4">{trans.BeeEvalInSmartCarEvaluation}</h2>
                        <p className="text-gray-400">{trans.allRoundComparisonForSmartChoices}</p>
                    </motion.div>

                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5, delay: 0.2 }}
                        className="overflow-x-auto"
                        style={{ width: '120%' }}
                    >
                        <table className="w-full border-collapse">
                            <thead>
                                <tr className="border-b border-gray-1000">
                                    <th className="py-4 px-6 text-left font-semibold">{trans.feature}</th>
                                    <th className="py-4 px-6 text-center font-semibold">{trans.traditionalMethods}</th>
                                    <th className="py-4 px-6 text-center font-semibold">{trans.BeeEvalMethod}</th>
                                </tr>
                            </thead>
                            <tbody>
                                {comparisonData.map((item, index) => (
                                    <tr key={index} className="border-b border-gray-800">
                                        <td style={{ paddingTop: '12px', paddingBottom: '12px' }} className=" text-gray-300">{item.feature}</td>
                                        <td style={{ paddingTop: '12px', paddingBottom: '12px' }} className=" text-center text-gray-400">{item.competitor}</td>
                                        <td style={{ paddingTop: '12px', paddingBottom: '12px' }} className=" text-center text-amber-500">{item.beeeval}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </motion.div>
                </div>
            </div>
        </section>
    );
}

