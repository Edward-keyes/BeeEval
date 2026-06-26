import { PageLayout } from '@/components/layouts/page-layout'
import WhyUsHero from "@/components/why-us/hero"
import ValueProps from "@/components/why-us/value-props"
import ComparisonTable from "@/components/why-us/comparison-table"
import DownloadCTA from "@/components/why-us/download-cta"
import ClientLogos from "@/components/why-us/client-logos"

export default function WhyUsPage() {
  return (
    <PageLayout activeItem="why-us">
      <WhyUsHero />
      <ValueProps />
      <ComparisonTable />
      {/* <ClientLogos /> */}
      <DownloadCTA />
    </PageLayout>
  )
}

