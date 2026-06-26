'use client'

import { PageLayout } from '@/components/layouts/page-layout'
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { motion } from 'framer-motion'
import { Brain, Car } from 'lucide-react'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'

export default function EvaluationSystemPage() {
    const { language } = useLanguage()

    return (
        <PageLayout activeItem="evaluation-system" className="min-h-screen bg-gradient-to-br from-gray-900 via-[#1C2028] to-gray-900">
            <div className="py-20">
                <div className="container mx-auto px-4">
                    <motion.h1
                        className="text-5xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-amber-400 to-amber-600 mb-6 text-center"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5 }}
                    >
                        {translations[language].solution}
                    </motion.h1>
                    <motion.p
                        className="text-xl text-gray-300 mb-16 max-w-3xl mx-auto text-center"
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5, delay: 0.1 }}
                    >
                        {translations[language].evaluationStr18}
                    </motion.p>

                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5, delay: 0.2 }}
                    >
                        <Tabs defaultValue="capi" className="w-full max-w-5xl mx-auto">
                            <TabsList className="w-full bg-black/20 backdrop-blur-lg p-2 rounded-2xl mb-8 border border-white/10">
                                <TabsTrigger
                                    value="capi"
                                    className="flex-1 bg-gradient-to-r from-transparent to-transparent data-[state=active]:from-amber-500 data-[state=active]:to-amber-600 data-[state=active]:text-black text-white hover:text-amber-400 transition-all duration-300 text-xl py-4 px-6 rounded-xl"
                                >
                                    <div className="flex items-center justify-center">
                                        <Brain className="w-6 h-6 mr-3" />
                                        <span>{translations[language].evaluationStr13}</span>
                                    </div>
                                </TabsTrigger>
                                <TabsTrigger
                                    value="cs"
                                    className="flex-1 bg-gradient-to-r from-transparent to-transparent data-[state=active]:from-amber-500 data-[state=active]:to-amber-600 data-[state=active]:text-black text-white hover:text-amber-400 transition-all duration-300 text-xl py-4 px-6 rounded-xl"
                                >
                                    <div className="flex items-center justify-center">
                                        <Car className="w-6 h-6 mr-3" />
                                        <span>{translations[language].evaluationStr14}</span>
                                    </div>
                                </TabsTrigger>
                            </TabsList>

                            <TabsContent value="capi">
                                <motion.div
                                    className="bg-[#0D1117] backdrop-blur-xl rounded-2xl p-10 shadow-2xl border border-white/10"
                                    initial={{ opacity: 0, y: 20 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    transition={{ duration: 0.5, delay: 0.4 }}
                                >
                                    <div className="space-y-8">
                                        <div>
                                            <motion.h2
                                                className="text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-amber-400 to-amber-600 mb-6 relative inline-block"
                                                initial={{ opacity: 0, y: 20 }}
                                                animate={{ opacity: 1, y: 0 }}
                                                transition={{ duration: 0.5 }}
                                            >
                                                {translations[language].evaluationStr11}
                                                <span className="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-amber-500/50 to-amber-600/50 rounded-full"></span>
                                            </motion.h2>
                                            <p className="text-gray-300 leading-relaxed text-lg">
                                                {translations[language].evaluationStr1}
                                            </p>
                                        </div>

                                        <div>
                                            <motion.h2
                                                className="text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-amber-400 to-amber-600 mb-6 relative inline-block"
                                                initial={{ opacity: 0, y: 20 }}
                                                animate={{ opacity: 1, y: 0 }}
                                                transition={{ duration: 0.5 }}
                                            >
                                                {translations[language].evaluationStr12}
                                                <span className="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-amber-500/50 to-amber-600/50 rounded-full"></span>
                                            </motion.h2>
                                            <div className="space-y-8">
                                                <motion.div
                                                    className="bg-[#1C2028]/80 border border-gray-700/30 backdrop-blur-sm rounded-2xl p-8"
                                                    initial={{ opacity: 0, y: 20 }}
                                                    animate={{ opacity: 1, y: 0 }}
                                                    transition={{ duration: 0.5 }}
                                                >
                                                    <p className="text-gray-300 leading-relaxed text-lg">
                                                        {translations[language].evaluationStr2}
                                                    </p>
                                                    <div className="mt-6">
                                                        <p className="text-gray-300 leading-relaxed text-lg">
                                                            {translations[language].evaluationStr3}
                                                        </p>
                                                        <ul className="list-disc list-inside space-y-2 text-gray-300 mt-4">
                                                            <li>{translations[language].evaluationStr9}</li>
                                                            <li>{translations[language].evaluationStr5}</li>
                                                            <li>{translations[language].evaluationStr4}</li>
                                                            <li>{translations[language].evaluationStr7}</li>
                                                            <li>{translations[language].evaluationStr6}</li>
                                                            <li>{translations[language].evaluationStr8}</li>
                                                        </ul>
                                                    </div>
                                                    <p className="text-gray-300 leading-relaxed text-lg mt-6">
                                                        {translations[language].evaluationStr10}
                                                    </p>
                                                </motion.div>
                                            </div>
                                        </div>
                                    </div>
                                </motion.div>
                            </TabsContent>

                            <TabsContent value="cs">
                                <motion.div
                                    className="bg-[#0D1117] backdrop-blur-xl rounded-2xl p-10 shadow-2xl border border-white/10"
                                    initial={{ opacity: 0, y: 20 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    transition={{ duration: 0.5, delay: 0.4 }}
                                >
                                    <div className="space-y-8">
                                        <div>
                                            <motion.h2
                                                className="text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-amber-400 to-amber-600 mb-6 relative inline-block"
                                                initial={{ opacity: 0, y: 20 }}
                                                animate={{ opacity: 1, y: 0 }}
                                                transition={{ duration: 0.5 }}
                                            >
                                                {translations[language].evaluationStr11}
                                                <span className="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-amber-500/50 to-amber-600/50 rounded-full"></span>
                                            </motion.h2>
                                            <p className="text-gray-300 leading-relaxed text-lg">
                                                {translations[language].evaluationStr15}
                                            </p>
                                        </div>

                                        <div>
                                            <motion.h2
                                                className="text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-amber-400 to-amber-600 mb-6 relative inline-block"
                                                initial={{ opacity: 0, y: 20 }}
                                                animate={{ opacity: 1, y: 0 }}
                                                transition={{ duration: 0.5 }}
                                            >
                                                {translations[language].evaluationStr12}
                                                <span className="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-amber-500/50 to-amber-600/50 rounded-full"></span>
                                            </motion.h2>
                                            <div className="grid grid-cols-1 gap-8">
                                                <motion.div
                                                    className="bg-[#1C2028]/80 border border-gray-700/30 backdrop-blur-sm rounded-2xl p-8"
                                                    initial={{ opacity: 0, y: 20 }}
                                                    animate={{ opacity: 1, y: 0 }}
                                                    transition={{ duration: 0.5 }}
                                                >
                                                    <p className="text-gray-300 leading-relaxed text-lg">
                                                        {translations[language].evaluationStr16}
                                                    </p>
                                                    <p className="text-gray-300 leading-relaxed text-lg mt-6">
                                                        {translations[language].evaluationStr17}
                                                    </p>
                                                </motion.div>
                                            </div>
                                        </div>
                                    </div>
                                </motion.div>
                            </TabsContent>
                        </Tabs>
                    </motion.div>
                </div>
            </div>
        </PageLayout>
    )
}

function getDimensionDescription(dimension: string): string {
    const descriptions: Record<string, string> = {
        '感知': '通过多模态输入准确理解用户意图和环境信息',
        '认知': '深度理解用户需求，进行逻辑推理和决策判断',
        '行动': '精准执行用户指令，提供流畅的交互体验',
        '反馈': '及时响应用户请求，提供清晰的执行结果',
        '进化': '持续学习优化，不断提升服务质量'
    }
    return descriptions[dimension] || ''
} 