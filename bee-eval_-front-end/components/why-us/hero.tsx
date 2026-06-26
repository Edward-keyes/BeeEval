'use client'

import { motion } from "framer-motion"
import { useLanguage } from "@/constants/language"
import { translations } from "@/language"

export default function WhyUsHero() {
  const { language } = useLanguage()
  return (
    <section className="relative bg-black text-white py-24 overflow-hidden">
      <div className="absolute inset-0 bg-gradient-to-b from-black/50 to-black z-0"></div>
      <div className="container mx-auto px-4 relative z-10">
        <div className="max-w-[1440px] mx-auto">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            className="text-center mb-24"
          >
            <h2 className="text-lg text-amber-500 mb-4 tracking-wide">{translations[language].innovationDrivenByEvaluation}</h2>
            <h1 className="text-4xl md:text-6xl font-bold tracking-tight">
              {translations[language].oneStopSmartCockpitPlatform}
            </h1>
          </motion.div>
          
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.2 }}
          >
            <div className="relative bg-[#0D1117] rounded-[32px] p-16 border border-gray-800/50 overflow-hidden">
              {/* Background Gradient Effects */}
              <div className="absolute top-0 left-1/4 w-1/2 h-1/2 bg-amber-500/10 blur-[120px] rounded-full"></div>
              <div className="absolute bottom-0 right-1/4 w-1/2 h-1/2 bg-amber-500/5 blur-[120px] rounded-full"></div>
              
              <div className="relative grid grid-cols-1 md:grid-cols-2 gap-24">
                <div>
                  <div className="flex items-center gap-5 mb-12">
                    <div className="w-12 h-12 rounded-xl flex items-center justify-center bg-gradient-to-br from-amber-500/20 to-amber-500/5">
                      <svg className="w-8 h-8 text-amber-500" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                      </svg>
                    </div>
                    <h3 className="text-2xl font-bold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">{translations[language].industryStandards}</h3>
                  </div>
                  <ul className="space-y-7 pl-6">
                    {[
                      translations[language].leadingUnitForSmartCockpitBigModelStandard,
                      translations[language].responsibleUnitForSmartCockpitBigModelModule,
                      translations[language].builderOfSmartCockpitBigModelCertificationSystem
                    ].map((item, index) => (
                      <motion.li 
                        key={index}
                        initial={{ opacity: 0, x: -20 }}
                        animate={{ opacity: 1, x: 0 }}
                        transition={{ delay: 0.3 + index * 0.1 }}
                        className="flex items-start gap-5 group"
                      >
                        <span className="flex-shrink-0 w-1.5 h-1.5 rounded-full bg-amber-500 mt-2.5 group-hover:scale-125 transition-transform"></span>
                        <span className="text-gray-300 text-lg font-medium leading-relaxed tracking-wide group-hover:text-white transition-colors">{item}</span>
                      </motion.li>
                    ))}
                  </ul>
                </div>
                
                <div>
                  <div className="flex items-center gap-5 mb-12">
                    <div className="w-12 h-12 rounded-xl flex items-center justify-center bg-gradient-to-br from-amber-500/20 to-amber-500/5">
                      <svg className="w-8 h-8 text-amber-500" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M19 9l-7 7-7-7 1.41-1.41L12 14.17l5.59-5.58L19 9zm0 6l-7 7-7-7 1.41-1.41L12 20.17l5.59-5.58L19 15z"/>
                      </svg>
                    </div>
                    <h3 className="text-2xl font-bold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">{translations[language].industryInfluence}</h3>
                  </div>
                  <ul className="space-y-7 pl-6">
                    {[
                      translations[language].proposerOfHAITheoryAndBigModelEvaluationSystem,
                      translations[language].initiatorOfHuYuAwardAndXuanYuanAward,
                      translations[language].COVESAAllianceMember
                    ].map((item, index) => (
                      <motion.li 
                        key={index}
                        initial={{ opacity: 0, x: -20 }}
                        animate={{ opacity: 1, x: 0 }}
                        transition={{ delay: 0.3 + index * 0.1 }}
                        className="flex items-start gap-5 group"
                      >
                        <span className="flex-shrink-0 w-1.5 h-1.5 rounded-full bg-amber-500 mt-2.5 group-hover:scale-125 transition-transform"></span>
                        <span className="text-gray-300 text-lg font-medium leading-relaxed tracking-wide group-hover:text-white transition-colors">{item}</span>
                      </motion.li>
                    ))}
                  </ul>
                </div>
              </div>
            </div>
          </motion.div>
        </div>
      </div>
    </section>
  )
}

