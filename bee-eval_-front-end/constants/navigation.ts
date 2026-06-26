import { translations } from '@/language'

export interface NavItem {
    id: string
    href: string
    translationKey: keyof typeof translations.zh_home_top
}

export const NAV_ITEMS: NavItem[] = [
    { id: 'home', href: '/', translationKey: 'home' },
    { id: 'all-models', href: '/all-models', translationKey: 'allModels' },
    { id: 'rankings', href: '/rankings', translationKey: 'rankings' },
    { id: 'feature-analysis', href: '/feature-analysis', translationKey: 'featureAnalysis' },
    { id: 'why-us', href: '/why-us', translationKey: 'whyUs' },
    { id: 'BeeEVALFunctionIntroduction', href: '/faq', translationKey: 'BeeEVALFunctionIntroduction' },
    { id: 'solution', href: '/evaluation-system', translationKey: 'solution' },
    { id: 'enterprise-membership', href: '/enterprise-membership', translationKey: 'enterpriseMembership' },
    { id: 'contact', href: '/contact', translationKey: 'contact' }
]