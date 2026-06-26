'use client'

import { useEffect, useState } from 'react'
import { Watermark } from './watermark'

export function WatermarkWrapper() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)

  useEffect(() => {
    // 检查登录状态的函数
    const checkLoginStatus = () => {
      const token = localStorage.getItem('token')
      const userEmail = localStorage.getItem('userEmail')
      return !!(token && userEmail)
    }

    // 初始检查
    setIsLoggedIn(checkLoginStatus())

    // 创建一个定时器来检查登录状态
    const intervalId = setInterval(() => {
      const currentStatus = checkLoginStatus()
      if (isLoggedIn !== currentStatus) {
        setIsLoggedIn(currentStatus)
      }
    }, 100) // 每 100ms 检查一次

    return () => {
      clearInterval(intervalId)
    }
  }, [isLoggedIn]) // 添加 isLoggedIn 作为依赖

  // 如果未登录，确保移除水印
  useEffect(() => {
    if (!isLoggedIn) {
      const watermarks = document.querySelectorAll('div[style*="background-image"]')
      watermarks.forEach(watermark => {
        if (watermark.parentNode === document.body) {
          document.body.removeChild(watermark)
        }
      })
    }
  }, [isLoggedIn])

  return isLoggedIn ? <Watermark /> : null
} 