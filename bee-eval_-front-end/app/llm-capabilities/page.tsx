import { Header } from "@/components/header"
import { LLMCapabilitiesLayout } from "@/components/llm-capabilities/layout"
import { Footer } from "@/components/footer"
export default function LLMCapabilitiesPage() {
  return (
    <main className="min-h-screen bg-[#171717]">
      <Header />
      <LLMCapabilitiesLayout />
      <Footer />
    </main>
  )
}

