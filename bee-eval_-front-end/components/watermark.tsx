'use client'

import { useEffect, useState } from 'react'

export function Watermark() {
    const [watermarkElement, setWatermarkElement] = useState<HTMLDivElement | null>(null)

    // 安全地移除水印元素的函数
    const safeRemoveWatermark = () => {
        if (watermarkElement && document.body.contains(watermarkElement)) {
            document.body.removeChild(watermarkElement)
            setWatermarkElement(null)
        }
    }

    useEffect(() => {
        const userEmail = localStorage.getItem('userEmail')
        if (!userEmail) {
            safeRemoveWatermark()
            return
        }

        const createWatermark = (email: string) => {
            // 创建 canvas 元素
            const canvas = document.createElement('canvas')
            const ctx = canvas.getContext('2d')
            if (!ctx) return ''

            // 设置 canvas 大小
            canvas.width = 300
            canvas.height = 200

            // 设置文字样式
            ctx.font = '14px Arial'
            ctx.fillStyle = 'rgba(128, 128, 128, 0.15)' // 灰色半透明
            ctx.textAlign = 'center'
            ctx.textBaseline = 'middle'

            // 旋转文字
            ctx.translate(150, 100)
            ctx.rotate(-30 * Math.PI / 180)
            ctx.fillText(email, 0, 0)

            // 转换为背景图片
            return canvas.toDataURL()
        }

        // 创建水印容器
        const watermark = document.createElement('div')
        watermark.style.position = 'fixed'
        watermark.style.top = '0'
        watermark.style.left = '0'
        watermark.style.width = '100vw'
        watermark.style.height = '100vh'
        watermark.style.zIndex = '9999'
        watermark.style.pointerEvents = 'none'
        watermark.style.backgroundImage = `url(${createWatermark(userEmail)})`
        watermark.style.backgroundRepeat = 'repeat'

        // 如果已经存在水印，先移除
        safeRemoveWatermark()

        // 添加到页面
        document.body.appendChild(watermark)
        setWatermarkElement(watermark)

        // 监听水印更新事件
        const handleWatermarkUpdate = () => {
            const currentEmail = localStorage.getItem('userEmail')
            if (!currentEmail) {
                safeRemoveWatermark()
            }
        }

        // 添加事件监听器
        window.addEventListener('watermarkUpdate', handleWatermarkUpdate)

        // 清理函数
        return () => {
            safeRemoveWatermark()
            window.removeEventListener('watermarkUpdate', handleWatermarkUpdate)
        }
    }, []) // 只在组件挂载时执行

    return null
} 