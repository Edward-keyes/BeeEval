'use client'

import { cn } from "@/lib/utils"
import { usePathname } from "next/navigation"
import Link from "next/link"
import { User, Bell, FileText, Settings } from "lucide-react"
import { Header } from "@/components/header"
import { Footer } from "@/components/footer"

const sidebarItems = [
  {
    title: "我的账号",
    href: "/account",
    icon: User
  },
  {
    title: "消息通知",
    href: "/account/notifications",
    icon: Bell,
    badge: 3
  },
  {
    title: "我的报告",
    href: "/account/reports",
    icon: FileText
  },
  {
    title: "设置",
    href: "/account/settings",
    icon: Settings
  }
]

export default function AccountLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const pathname = usePathname()

  return (
    <div className="min-h-screen flex flex-col bg-background">
      <Header activeItem="account" />
      <div className="flex-grow flex py-24">
        <div className="container mx-auto px-4 flex">
          <div className="hidden md:flex w-64 flex-col gap-4 pr-8 border-r">
            {sidebarItems.map((item) => {
              const isActive = pathname === item.href
              return (
                <Link
                  key={item.href}
                  href={item.href}
                  className={cn(
                    "flex items-center gap-3 px-3 py-2 rounded-lg text-sm transition-colors",
                    isActive ? "bg-primary text-primary-foreground" : "hover:bg-muted"
                  )}
                >
                  <item.icon className="h-4 w-4" />
                  <span>{item.title}</span>
                  {item.badge && (
                    <span className="ml-auto bg-primary/10 text-primary px-2 py-0.5 rounded-full text-xs">
                      {item.badge}
                    </span>
                  )}
                </Link>
              )
            })}
          </div>
          <main className="flex-grow pl-8">
            {children}
          </main>
        </div>
      </div>
      <Footer />
    </div>
  )
} 