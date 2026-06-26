'use client'

import { DomainScores } from "./domain-scores"
import { HighPerformance } from "./high-performance"
import { FrequentQuestions } from "./frequent-questions"
import { motion } from "framer-motion"

export function LLMAnalytics() {
  return (
    <div className="container mx-auto px-4 py-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="space-y-8"
      >
        <DomainScores />
        <HighPerformance />
        <FrequentQuestions />
      </motion.div>
    </div>
  )
}

