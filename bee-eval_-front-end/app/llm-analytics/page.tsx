'use client'

import { useState } from 'react'
import { Header } from "@/components/header"
import { DetailedAnalysis } from "@/components/llm-analytics/detailed-analysis"
import { FilterBar } from "@/components/llm-analytics/filter-bar"
import { LineChart } from "@tremor/react"

export default function LLMAnalyticsPage() {
  const [showIndustryAverage, setShowIndustryAverage] = useState(true)
  const [selectedBrands, setSelectedBrands] = useState<string[]>([])

  return (
    <main className="min-h-screen bg-background">
      <Header />
      <div className="container mx-auto px-4 py-6">
        <FilterBar 
          showIndustryAverage={showIndustryAverage}
          setShowIndustryAverage={setShowIndustryAverage}
          selectedBrands={selectedBrands}
          setSelectedBrands={setSelectedBrands}
        />
        <DetailedAnalysis 
          showIndustryAverage={showIndustryAverage}
          selectedBrands={selectedBrands}
        />
      </div>
    </main>
  )
}

