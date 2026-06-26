'use client'

import { Space_Grotesk } from 'next/font/google'
import './globals.css'
import { ThemeProvider } from '@/providers/theme-provider'
import { cn } from '@/lib/utils'
import { LanguageProvider } from '@/constants/language'
import { Watermark } from '@/components/watermark'
import { Toaster } from "sonner"
import { WatermarkWrapper } from '../components/watermark-wrapper'
import { UserTracker } from '@/components/user-tracker'
import { FeedbackButton } from '@/components/feedback-button'

const spaceGrotesk = Space_Grotesk({ subsets: ['latin'] })

export function ClientLayout({
    children,
}: {
    children: React.ReactNode
}) {
    return (
        <div className={cn("min-h-screen bg-[#171717]", spaceGrotesk.className)}>
            <ThemeProvider
                attribute="class"
                defaultTheme="dark"
                enableSystem
                disableTransitionOnChange
            >
                <LanguageProvider>
                    <UserTracker />
                    {children}
                    <FeedbackButton />
                </LanguageProvider>
                <WatermarkWrapper />
                <Toaster richColors position="top-right" />
            </ThemeProvider>
        </div>
    )
} 