'use client'

import { useState } from 'react'
import { motion } from "framer-motion"
import { BackButton } from "./back-button"
import { FunctionalDomains } from "./functional-domains"
import { DomainAnalysis } from "./domain-analysis"

interface DetailedAnalysisProps {
  showIndustryAverage: boolean
  selectedBrands: string[]
}

export function DetailedAnalysis({ showIndustryAverage, selectedBrands }: DetailedAnalysisProps) {
  const [selectedDomain, setSelectedDomain] = useState<string>("车书域")

  return (
    <div className="space-y-8">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <BackButton />
        <FunctionalDomains selectedDomain={selectedDomain} onSelectDomain={setSelectedDomain} />
        <DomainAnalysis 
          domain={selectedDomain} 
          showIndustryAverage={showIndustryAverage}
          selectedBrands={selectedBrands}
        />
      </motion.div>
    </div>
  )
}

