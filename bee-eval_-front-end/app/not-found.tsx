'use client'

import { motion } from 'framer-motion'
import { ChevronLeft } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { useRouter } from 'next/navigation'

export default function NotFound() {
  const router = useRouter()

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="text-center p-8"
      >
        <h2 className="text-4xl font-bold text-gray-900 mb-4">页面未找到</h2>
        <p className="text-gray-500 mb-8">
          抱歉，您访问的页面不存在或已被移除。
        </p>
        <Button
          onClick={() => router.back()}
          className="flex items-center gap-2"
        >
          <ChevronLeft className="w-4 h-4" />
          返回上一页
        </Button>
      </motion.div>
    </div>
  )
} 