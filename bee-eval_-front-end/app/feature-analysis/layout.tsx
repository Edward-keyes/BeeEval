'use client'

import { PageLayout } from '@/components/layouts/page-layout'

interface FeatureAnalysisLayoutProps {
  children: React.ReactNode
}

export default function FeatureAnalysisLayout({ children }: FeatureAnalysisLayoutProps) {
  return (
    <PageLayout activeItem="feature-analysis">
      {children}
    </PageLayout>
  )
} 