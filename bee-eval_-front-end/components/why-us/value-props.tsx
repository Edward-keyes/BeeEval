'use client'

import { motion } from "framer-motion"
import Image from "next/image"
import { useLanguage } from "@/constants/language"
import { translations } from "@/language"

export default function ValueProps() {
    const { language } = useLanguage()

    // 使用类型断言解决TypeScript类型错误
    const trans = translations[language] as any;

    const beevalModules = [
        {
            title: trans.functionBeeCell,
            subtitle: trans.aiFunctionExistence,
            description: trans.structuredFunctionScenesAndVideoPresentation,
            icon: "M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
        },
        {
            title: trans.evaluationBeeCell,
            subtitle: trans.aiPerformanceEvaluation,
            description: trans.dataDrivenBenchmarking,
            icon: "M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"
        },
        {
            title: trans.designBeeCell,
            subtitle: trans.aiInteraction,
            description: trans.paradigmUpgradeFromHMIToHAI,
            icon: "M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01"
        },
        {
            title: trans.customerBeeCell,
            subtitle: trans.aiExperienceEvaluation,
            description: trans.realSceneRestorationAndUserDataCollection,
            icon: "M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"
        }
    ];

    return (
        <section className="bg-black text-white py-20">
            <div className="container mx-auto px-4">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                    className="text-center mb-16"
                >
                    <h2 className="text-3xl font-bold mb-4">{trans.aboutBeeEval}</h2>
                    <p className="text-xl text-gray-400">{trans.empoweringOEMSmartCockpitProductDefinition}</p>
                </motion.div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    {beevalModules.map((module, index) => (
                        <motion.div
                            key={index}
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5, delay: index * 0.1 }}
                            className="bg-gray-900 p-8 rounded-2xl border border-gray-800 hover:border-amber-500 transition-colors"
                        >
                            <div className="w-12 h-12 bg-amber-500/10 rounded-lg flex items-center justify-center mb-6">
                                <svg className="w-6 h-6 text-amber-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d={module.icon} />
                                </svg>
                            </div>
                            <div className="space-y-3">
                                <h3 className="text-xl font-semibold">{module.title}</h3>
                                <p className="text-amber-500 font-medium">{module.subtitle}</p>
                                <p className="text-gray-400">{module.description}</p>
                            </div>
                        </motion.div>
                    ))}
                </div>
            </div>
        </section>
    );
}

