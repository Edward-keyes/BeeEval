'use client'

import { Header } from '@/components/header'

interface WhyUsLayoutProps {
  children: React.ReactNode
}

export default function WhyUsLayout({ children }: WhyUsLayoutProps) {
  return (
    <div className="min-h-screen flex flex-col">
      
      <main className="flex-1 bg-gray-50">
        {children}
      </main>
    </div>
  )
} 