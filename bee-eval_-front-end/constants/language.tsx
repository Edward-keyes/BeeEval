'use client'

import { createContext, useContext, useState, useEffect } from 'react'

export type Language = 'zh_home_top' | 'en' | 'ja'

interface LanguageContextType {
    language: Language
    setLanguage: (lang: Language) => void
}

const LanguageContext = createContext<LanguageContextType | undefined>(undefined)

export const LanguageProvider = ({ children }: { children: React.ReactNode }) => {
    const [language, setLanguage] = useState<Language>('zh_home_top')

    // 从 localStorage 加载语言设置
    useEffect(() => {
        const savedLanguage = localStorage.getItem('language') as Language
        if (savedLanguage) {
            setLanguage(savedLanguage)
        }
    }, [])

    // 当语言改变时更新 HTML lang 属性并保存到 localStorage
    const handleSetLanguage = (lang: Language) => {
        setLanguage(lang)
        localStorage.setItem('language', lang)
        // 更新 HTML lang 属性
        document.documentElement.lang = lang === 'zh_home_top' ? 'zh' : lang
    }

    // 初始化时也设置 HTML lang 属性
    useEffect(() => {
        document.documentElement.lang = language === 'zh_home_top' ? 'zh' : language
    }, [language])

    return (
        <LanguageContext.Provider value={{ language, setLanguage: handleSetLanguage }}>
            {children}
        </LanguageContext.Provider>
    )
}

export const useLanguage = () => {
    const context = useContext(LanguageContext)
    if (!context) throw new Error('useLanguage must be used within LanguageProvider')
    return context
}