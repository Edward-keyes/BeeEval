import { Header } from '@/components/header'
import { Footer } from '@/components/footer'
import { cn } from '@/lib/utils'

interface PageLayoutProps {
  children: React.ReactNode
  activeItem?: string
  className?: string
  darkMode?: boolean
}

export function PageLayout({ 
  children, 
  activeItem = 'home',
  className,
  darkMode = false
}: PageLayoutProps) {
  return (
    <div className={cn(
      "min-h-screen flex flex-col",
      darkMode ? "bg-[#0D1117] text-white" : "bg-background",
      className
    )}>
      <Header activeItem={activeItem} />
      <main className="flex-grow bg-[#0D1117]">
        {children}
      </main>
      <Footer />
    </div>
  )
} 