'use client'

import { motion } from "framer-motion"
import Image from "next/image"
import { useLanguage } from "@/constants/language"
import { translations } from "@/language"

const logos = [
  // First row
  [
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120"
  ],
  // Second row
  [
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120"
  ],
  // Third row
  [
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120",
    "/placeholder.svg?height=40&width=120"
  ]
]

export default function ClientLogos() {
  const { language } = useLanguage()
  return (
    <section className="bg-gray-900 text-white py-20">
      <div className="container mx-auto px-4">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="text-center mb-12"
        >
          <h2 className="text-3xl font-bold mb-4">{translations[language].partners}</h2>
          <p className="text-gray-400">{translations[language].trustedIndustryLeaders}</p>
        </motion.div>
        
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-8 items-center justify-items-center"
        >
          {/* Logo placeholders with hover effect */}
          {[...Array(6)].map((_, index) => (
            <div
              key={index}
              className="w-32 h-16 bg-gray-800 rounded-lg flex items-center justify-center hover:bg-gray-700 transition-colors"
            >
              <span className="text-gray-400 font-semibold">LOGO {index + 1}</span>
            </div>
          ))}
        </motion.div>
      </div>
    </section>
  )
}

