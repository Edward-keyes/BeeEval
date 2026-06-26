'use client'

import Link from 'next/link'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Search, X, User2 } from 'lucide-react'
import { useState, useEffect, useMemo, useCallback } from 'react'
import { cn } from "@/lib/utils"
import { motion, AnimatePresence } from 'framer-motion'
import { usePathname, useRouter } from 'next/navigation'
import Image from 'next/image'
import DOMPurify from 'isomorphic-dompurify'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import { NAV_ITEMS } from '@/constants/navigation'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Globe } from 'lucide-react'
import { UserMenu } from "@/components/user-menu"
import { toast } from 'sonner'
import http from "@/utils/request"
import { useAuthStore } from '@/store/useAuthStore'

interface User {
    name: string
    email: string
    avatar: string
}

export function Header({ activeItem = 'home' }: { activeItem?: string }) {
    const [scrolled, setScrolled] = useState(false)
    const [isLoggedIn, setIsLoggedIn] = useState(false)
    const [userEmail, setUserEmail] = useState<string>('')
    const [user, setUser] = useState<User | null>(null)
    const pathname = usePathname()
    const router = useRouter()
    const [isSearchOpen, setIsSearchOpen] = useState(false)
    const { logout, userEmail: storeEmail, accountStatus } = useAuthStore()

    const [showSearch, setShowSearch] = useState(false)

    // 判断是否在特殊页面（企业会员或联系我们）
    const isSpecialPage = false

    const isActive = useCallback((item: typeof NAV_ITEMS[0]) => {
        // 首先检查当前路径
        if (pathname === item.href) return true;
        // 只有在首页（/）时才检查activeItem
        if (pathname === '/' && item.id === activeItem) return true;
        return false;
    }, [activeItem, pathname]);

    useEffect(() => {
        const handleScroll = () => {
            setScrolled(window.scrollY > 0)
        }
        window.addEventListener('scroll', handleScroll)
        return () => window.removeEventListener('scroll', handleScroll)
    }, [])

    // Check login status and email on mount
    useEffect(() => {
        const token = localStorage.getItem('token')
        setIsLoggedIn(!!token)
        if (storeEmail) {
            setUserEmail(storeEmail)
        } else {
            const storedEmail = localStorage.getItem('userEmail')
            setUserEmail(storedEmail || '未知账号')
        }
    }, [storeEmail])

    const handleSearch = useCallback((e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        const searchTerm = DOMPurify.sanitize((e.target as HTMLFormElement).search.value);
    }, [])

    // 处理退出登录
    const handleLogout = async () => {
        try {
            await http.get('/vehicleuser/logout')
            // 调用 logout 函数清除所有状态
            logout()
            setIsLoggedIn(false)

            // 直接移除水印
            const watermarks = document.querySelectorAll('div[style*="background-image"]')
            watermarks.forEach(watermark => {
                if (watermark.parentNode === document.body) {
                    document.body.removeChild(watermark)
                }
            })

            toast.success(trans.logout, {
                description: trans.logoutSuccess
            })

            // 强制刷新页面，确保所有组件重新加载
            window.location.href = '/'
        } catch (error) {
            toast.error(trans.logoutFailed, {
                description: trans.tryAgainLater
            })
        }
    }

    // 处理登录点击
    const handleLogin = () => {
        router.push('/login')
    }

    // 新增语言上下文
    const { language, setLanguage } = useLanguage()
    const trans = translations[language] as any

    const memoizedNavItems = useMemo(() => (
        NAV_ITEMS.map((item) => (
            <motion.div
                key={item.id}
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
            >
                <div
                    onClick={(e) => {
                        // 检查是否需要登录
                        if ((item.id === 'rankings' || item.id === 'feature-analysis') && !isLoggedIn) {
                            e.preventDefault();
                            toast.error(trans.pleaseLoginFirst, {
                                description: trans.loginToView,
                                style: {
                                    background: '#FFC107',
                                    color: '#000'
                                }
                            });
                            router.push('/login');
                            return;
                        }
                    }}
                >
                    <Link
                        href={item.href}
                        className={cn(
                            "relative py-2 text-xs font-medium transition-colors whitespace-nowrap",
                            isActive(item)
                                ? 'text-primary'
                                : isSpecialPage
                                    ? 'text-white hover:text-primary'
                                    : 'text-foreground/80 hover:text-primary'
                        )}
                        aria-current={isActive(item) ? 'page' : undefined}
                    >
                        {translations[language][item.translationKey]}
                        {isActive(item) && (
                            <motion.div
                                className="absolute bottom-0 left-0 right-0 h-0.5 bg-primary"
                                layoutId="activeTab"
                            />
                        )}
                    </Link>
                </div>
            </motion.div>
        ))
    ), [pathname, activeItem, isSpecialPage, isActive, language, isLoggedIn, router, trans])

    const LanguageSwitch = () => (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                {/* 隐藏语言选择 */}
                <Button
                    variant="ghost"
                    size="icon"
                    className={cn(
                        "text-primary transition-colors hover:text-primary/80"
                    )}
                // style={{ display: 'none' }}
                >
                    <Globe className="h-4 w-4" />

                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="bg-[#1C2028] border-gray-800">
                <DropdownMenuItem
                    onClick={() => setLanguage('zh_home_top')}
                    className={cn(
                        "cursor-pointer",
                        language === 'zh_home_top' && "bg-accent"
                    )}
                >
                    中文
                </DropdownMenuItem>
                <DropdownMenuItem
                    onClick={() => setLanguage('en')}
                    className={cn(
                        "cursor-pointer",
                        language === 'en' && "bg-accent"
                    )}
                >
                    English
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    )

    return (
        <motion.header
            className={cn(
                "sticky top-0 w-full z-50 transition-all duration-200",
                scrolled ? "glass-card shadow-lg bg-background/80" : "bg-background"
            )}
            initial={{ opacity: 0, y: -50 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
        >
            <div className="container mx-auto px-4">
                <div className="flex items-center h-16 text-sm">
                    <Link href="/" className="flex items-center">
                        <div className="w-100 h-100 relative flex items-center justify-center mr-2">
                            <Image
                                src="/bee-logo.png"
                                alt="BeeEval Logo"
                                width={160}
                                height={50}
                                className="object-contain"
                                priority
                                quality={100}
                            />
                        </div>
                    </Link>

                    <nav className="flex-1 flex justify-center px-8 space-x-12">
                        {memoizedNavItems}
                    </nav>

                    <div className="flex items-center space-x-8">
                        <div className="relative flex items-center">


                        </div>
                        <LanguageSwitch />
                        <div className="w-10 flex justify-end">
                            {isLoggedIn ? (
                                <DropdownMenu>
                                    <DropdownMenuTrigger asChild>
                                        <Button variant="ghost" size="icon" className="text-primary transition-colors hover:text-primary/80">
                                            <User2 className="h-4 w-4" />
                                        </Button>
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent align="end" className="w-40 bg-[#1C2028] border-gray-800">
                                        {/* <DropdownMenuItem className="text-gray-300 hover:text-amber-500 cursor-pointer">
                      <Link href="/profile" className="w-full">个人主页</Link>
                    </DropdownMenuItem> */}
                                        <DropdownMenuItem className="text-gray-400 cursor-default hover:bg-transparent" disabled>
                                            {trans.currentLoggedInAccount}{userEmail}
                                        </DropdownMenuItem>
                                        <DropdownMenuItem className="text-gray-300 hover:text-amber-500 cursor-pointer" onClick={handleLogout}>
                                            {trans.logout}
                                        </DropdownMenuItem>
                                    </DropdownMenuContent>
                                </DropdownMenu>
                            ) : (
                                <Link href="/login">
                                    <Button variant="ghost" size="icon" className="text-primary transition-colors hover:text-primary/80">
                                        <User2 className="h-4 w-4" />
                                    </Button>
                                </Link>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </motion.header>
    )
}

