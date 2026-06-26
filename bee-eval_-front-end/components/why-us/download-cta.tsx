'use client'

import { motion } from "framer-motion"
import { Button } from "@/components/ui/button"
import { useLanguage } from "@/constants/language"
import { translations } from "@/language"



export default function DownloadCTA() {
  const { language } = useLanguage()
  return (
    <section className="bg-black text-white py-20">
      <div className="container mx-auto px-4">
        <div className="max-w-4xl mx-auto text-center">
          {/* <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            className="space-y-6"
          >
            <h2 className="text-3xl md:text-4xl font-bold">
              {translations[language].readyToStartUsing}
            </h2>
            <p className="text-gray-400 text-lg max-w-2xl mx-auto">
              {translations[language].startFreeTrial}
            </p>
            <div className="flex flex-wrap justify-center gap-4 mt-8">
              <button className="bg-amber-500 hover:bg-amber-600 text-black font-semibold px-8 py-3 rounded-lg transition-colors">
                {translations[language].freeTrial}
              </button>
              <button className="border border-gray-600 hover:border-amber-500 text-gray-300 hover:text-amber-500 font-semibold px-8 py-3 rounded-lg transition-colors">
                {translations[language].learnMore}
              </button>
            </div>
          </motion.div> */}
        </div>
      </div>
    </section>
  )
}

