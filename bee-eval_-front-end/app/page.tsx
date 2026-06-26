'use client'
import { PageLayout } from '@/components/layouts/page-layout'
import { HeroSection } from '@/components/hero-section'
import { FeaturedFunctions } from '@/components/featured-functions'
import { Button } from '@/components/ui/button'
import Link from 'next/link'
import { translations } from '@/language'
import { useLanguage } from '@/constants/language'
export const dynamic = 'force-dynamic'
export default function Home() {
    const { language } = useLanguage()
    return (
        <PageLayout activeItem="home" darkMode>
            <HeroSection />
            <FeaturedFunctions />
            <section className="py-20 bg-[#0D1117]">
                <div className="container mx-auto px-4 text-center">
                    <h2 className="text-3xl font-bold mb-4 text-white">{translations[language].readyToStartYourJourney}</h2>
                    <p className="text-lg text-gray-400 mb-8">{translations[language].joinBeeEval}</p>
                    <div className="flex justify-center space-x-4">
                        <Button asChild className="bg-amber-500 hover:bg-amber-600 text-black font-semibold">
                            <Link href="/enterprise-membership">{translations[language].learnAboutEnterpriseMembership}</Link>
                        </Button>
                        <Button
                            asChild
                            variant="outline"
                            className="border-gray-800 hover:border-amber-500 text-gray-400 hover:text-amber-500"
                        >
                            <Link href="/contact">{translations[language].contact}</Link>
                        </Button>
                    </div>
                </div>
            </section>
        </PageLayout>
    )
}

