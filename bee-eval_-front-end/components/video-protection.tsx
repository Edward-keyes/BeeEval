'use client'

import { useEffect, useState } from 'react'
import { cn } from '@/lib/utils'
import { toast } from 'sonner'

interface VideoProtectionProps {
    children: React.ReactNode
    className?: string
}

export function VideoProtection({ children, className }: VideoProtectionProps) {
    const [isScreenshotting, setIsScreenshotting] = useState(false)

    useEffect(() => {
        const handleKeyDown = (e: KeyboardEvent) => {
            // 检查是否按下 Alt + Ctrl + A 或 Alt + A
            if ((e.altKey && e.ctrlKey && e.key.toLowerCase() === 'a') ||
                (e.altKey && !e.ctrlKey && e.key.toLowerCase() === 'a')) {
                // 立即阻止默认行为
                e.preventDefault()
                e.stopPropagation()

                // 立即显示遮罩层
                setIsScreenshotting(true)

                // 显示提示
                toast.error('为了保护知识产权，请勿对视频内容进行截图', {
                    duration: 3000,
                    position: 'top-center',
                })
            }
        }

        const handleKeyUp = (e: KeyboardEvent) => {
            // 当释放任意按键时，重置状态
            if (!e.altKey && !e.ctrlKey) {
                setIsScreenshotting(false)
            }
        }

        // 添加对右键菜单的阻止
        const handleContextMenu = (e: MouseEvent) => {
            e.preventDefault()
        }

        window.addEventListener('keydown', handleKeyDown, { capture: true })
        window.addEventListener('keyup', handleKeyUp)
        window.addEventListener('contextmenu', handleContextMenu)

        return () => {
            window.removeEventListener('keydown', handleKeyDown, { capture: true })
            window.removeEventListener('keyup', handleKeyUp)
            window.removeEventListener('contextmenu', handleContextMenu)
        }
    }, [])

    return (
        <div
            className={cn('relative select-none', className)}
            style={{
                userSelect: 'none',
                WebkitUserSelect: 'none',
                MozUserSelect: 'none',
                msUserSelect: 'none',
            }}
        >
            {children}
            {/* 添加水印层 */}
            <div className="absolute inset-0 pointer-events-none">
                {/* 使用CSS渐变代替图片，避免404错误 */}
                <div className="absolute inset-0 bg-gradient-to-br from-transparent to-black/5 opacity-10"></div>
                {/* 添加文本水印 */}
                <div className="absolute inset-0 flex items-center justify-center">
                    <div className="transform rotate-45 opacity-5 text-white select-none whitespace-nowrap text-2xl">
                        BeeEval 版权保护
                    </div>
                </div>
            </div>
            {isScreenshotting && (
                <div className="absolute inset-0 bg-black/50 backdrop-blur-md flex items-center justify-center z-50">
                    <div className="text-white text-center p-4">
                        <p className="text-lg font-semibold mb-2">⚠️ 截图保护</p>
                        <p>为了保护知识产权，请勿对视频内容进行截图</p>
                    </div>
                </div>
            )}
        </div>
    )
} 