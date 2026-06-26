'use client'

import { useState } from 'react'
import { PageLayout } from '@/components/layouts/page-layout'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Check, Mail, QrCode, Sparkles } from 'lucide-react'
import { motion, AnimatePresence } from 'framer-motion'
import Image from 'next/image'
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogDescription,
} from "@/components/ui/dialog"
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { useLanguage } from "@/constants/language"
import { translations } from "@/language"

export default function EnterpriseMembership() {
    const { language } = useLanguage()
    const [isContactModalOpen, setIsContactModalOpen] = useState(false)
    const [selectedPlan, setSelectedPlan] = useState<string | null>(null)
    const [currentVersion, setCurrentVersion] = useState<"1.0" | "pro">("1.0")

    // 使用类型断言解决TypeScript类型错误
    const trans = translations[language] as any;

    const basicFeatures = [
        trans.bigModelFullFunctionVideo,
        trans.competitiveFeatureVideoComparison,
        trans.bigModelFeatureAnalysis,
        trans.penetrationRateHeatMap,
        trans.functionEvaluationDetails,
        trans.bigModelAbilityEvaluationScoreAndExample,
        trans.highScoreVideo,
        trans.competitiveAbilityComparison,
        trans.customCompetitiveCombination,
        trans.bigModelBasicAbilityRanking,
        trans.sixFunctionDomainsRanking,
        trans.teamSize10
    ]

    const proFeatures = [

        trans.highFrequencyQuestionVideo,
        trans.onsiteCommunication1Session,
        trans.teamCollaborationFeature,
        trans.teamSizeExpandedTo20,
        trans.openCase
    ]

    const contactInfo = {
        email: "beeeval@xailab.cn",
        qrCodeUrl: "/images/WechatIMG69.jpg"
    }

    const handlePlanSelect = (planName: string) => {
        setSelectedPlan(planName)
        setIsContactModalOpen(true)
    }

    const features = currentVersion === "1.0" ? basicFeatures : [...basicFeatures, ...proFeatures]

    return (
        <PageLayout activeItem="enterprise-membership" darkMode>
            <main className="flex-grow py-24">
                <div className="container mx-auto px-4">
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5 }}
                        className="text-center mb-20"
                    >
                        <h2 className="text-lg font-medium text-amber-500 mb-6 tracking-wide">{trans.enterpriseMembership}</h2>
                        <h1 className="text-4xl md:text-6xl font-bold tracking-tight max-w-[1200px] mx-auto whitespace-pre-line text-white">
                            {trans.professionalSmartCockpitEvaluationServices}
                        </h1>
                    </motion.div>

                    <div className="relative">
                        {/* Background Gradient Effects */}
                        <div className="absolute top-0 left-1/4 w-1/2 h-1/2 bg-amber-500/10 blur-[120px] rounded-full"></div>
                        <div className="absolute bottom-0 right-1/4 w-1/2 h-1/2 bg-amber-500/5 blur-[120px] rounded-full"></div>

                        <motion.div
                            key={currentVersion}
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5 }}
                            className="relative max-w-5xl mx-auto"
                        >
                            <Card className="bg-[#0D1117] border-gray-800/50 hover:border-amber-500/50 transition-all duration-300">
                                <div className="absolute top-8 right-8 z-10">
                                    <Tabs
                                        value={currentVersion}
                                        onValueChange={(value) => setCurrentVersion(value as "1.0" | "pro")}
                                        className="bg-black/40 p-0.5 rounded-full"
                                    >
                                        <TabsList className="bg-transparent border border-amber-500/20 h-9">
                                            <TabsTrigger
                                                value="1.0"
                                                className="data-[state=active]:bg-amber-500 data-[state=active]:text-black px-6 h-8 text-sm font-medium"
                                            >
                                                1.0
                                            </TabsTrigger>
                                            <TabsTrigger
                                                value="pro"
                                                className="data-[state=active]:bg-amber-500 data-[state=active]:text-black px-6 h-8 text-sm font-medium"
                                            >
                                                Pro
                                            </TabsTrigger>
                                        </TabsList>
                                    </Tabs>
                                </div>
                                <CardHeader className="pb-8 pt-8 px-8">
                                    <CardTitle className="text-4xl font-bold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent flex items-center gap-3">
                                        BeeEval {currentVersion}
                                        {currentVersion === "pro" && (
                                            <Sparkles className="w-8 h-8 text-amber-500" />
                                        )}
                                    </CardTitle>
                                </CardHeader>
                                <CardContent className="px-8 pb-8">
                                    <div className="min-h-[600px]">
                                        <AnimatePresence mode="popLayout">
                                            <motion.ul
                                                key={currentVersion}
                                                className="grid grid-cols-2 gap-8"
                                                initial={{ opacity: 0, x: 20 }}
                                                animate={{ opacity: 1, x: 0 }}
                                                exit={{ opacity: 0, x: -20 }}
                                                transition={{ duration: 0.3 }}
                                            >
                                                {basicFeatures.map((feature, featureIndex) => (
                                                    <motion.li
                                                        key={`basic-${featureIndex}`}
                                                        className="flex items-center gap-4 group"
                                                        initial={{ opacity: 0, y: 20 }}
                                                        animate={{ opacity: 1, y: 0 }}
                                                        transition={{ delay: featureIndex * 0.05 }}
                                                    >
                                                        <div className="w-9 h-9 rounded-full bg-gradient-to-br from-amber-500/20 to-amber-500/5 flex items-center justify-center flex-shrink-0">
                                                            <Check className="w-5 h-5 text-amber-500" />
                                                        </div>
                                                        <span className="text-lg text-gray-300 group-hover:text-white transition-colors leading-tight flex items-center">{feature}</span>
                                                    </motion.li>
                                                ))}
                                                {currentVersion === "pro" && proFeatures.map((feature, featureIndex) => (
                                                    <motion.li
                                                        key={`pro-${featureIndex}`}
                                                        className="flex items-start gap-4 group relative"
                                                        initial={{ opacity: 0, y: 20 }}
                                                        animate={{ opacity: 1, y: 0 }}
                                                        transition={{ delay: (basicFeatures.length + featureIndex) * 0.05 }}
                                                    >
                                                        <div className="absolute -top-1 -right-1 bg-amber-500 text-black text-xs font-bold px-2 py-0.5 rounded-full">
                                                            NEW
                                                        </div>
                                                        <div className="w-9 h-9 rounded-full bg-gradient-to-br from-amber-500/20 to-amber-500/5 flex items-center justify-center flex-shrink-0 mt-0.5">
                                                            <Check className="w-5 h-5 text-amber-500" />
                                                        </div>
                                                        <span className="text-lg text-amber-50 group-hover:text-white transition-colors leading-tight font-medium">{feature}</span>
                                                    </motion.li>
                                                ))}
                                            </motion.ul>
                                        </AnimatePresence>
                                    </div>
                                    <div className="mt-12">
                                        <Button
                                            className="w-full bg-gradient-to-r from-amber-500 to-amber-600 hover:from-amber-600 hover:to-amber-700 text-black font-semibold h-12 text-lg"
                                            onClick={() => handlePlanSelect(`BeeEval ${currentVersion}`)}
                                        >
                                            {trans.chooseThisPlan}
                                        </Button>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>
                    </div>
                </div>
            </main>

            <Dialog open={isContactModalOpen} onOpenChange={setIsContactModalOpen}>
                <DialogContent className="bg-[#1C2028] border-gray-800 sm:max-w-[425px]">
                    <DialogHeader>
                        <DialogTitle className="text-xl font-semibold text-white">
                            {trans.contactUs}
                        </DialogTitle>
                        <DialogDescription className="text-gray-400">
                            {language === 'en'
                                ? `Contact us to learn more about ${selectedPlan} details`
                                : `选择以下方式联系我们，了解${selectedPlan}详情`}
                        </DialogDescription>
                    </DialogHeader>
                    <div className="grid gap-6 py-4">
                        <div className="space-y-4">
                            <div className="flex items-center gap-3 p-4 rounded-lg bg-black/20 border border-gray-800">
                                <Mail className="w-5 h-5 text-amber-500" />
                                <div>
                                    <p className="text-sm text-gray-400">
                                        {language === 'en' ? 'Business Email' : '商务邮箱'}
                                    </p>
                                    <p className="text-white font-medium">{contactInfo.email}</p>
                                </div>
                            </div>
                            <div className="p-4 rounded-lg bg-black/20 border border-gray-800">
                                <div className="flex items-center gap-3 mb-3">
                                    <QrCode className="w-5 h-5 text-amber-500" />
                                    <p className="text-sm text-gray-400">
                                        {language === 'en' ? 'Scan to follow our official account' : '扫描关注公众号'}
                                    </p>
                                </div>
                                <div className="flex justify-center bg-white p-2 rounded-lg">
                                    <Image
                                        src={contactInfo.qrCodeUrl}
                                        alt={language === 'en' ? 'WeChat QR Code' : '微信公众号二维码'}
                                        width={180}
                                        height={180}
                                        className="rounded-md"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </DialogContent>
            </Dialog>
        </PageLayout>
    )
}

