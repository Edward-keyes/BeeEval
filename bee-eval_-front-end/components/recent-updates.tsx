'use client'

import { useState } from 'react'
import { motion } from 'framer-motion'
import { Button } from "@/components/ui/button"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area"
import { ChevronLeft, ChevronRight } from 'lucide-react'
import { cn } from '@/lib/utils'
import { LazyImage } from '@/components/lazy-image'

interface Update {
  id: number
  title: string
  description: string
  date: string
  image: string
  category: string
}

const updates: Update[] = [
  {
    id: 1,
    title: "全新智能座舱系统发布",
    description: "搭载最新一代智能座舱系统，提供更直观的人机交互体验。",
    date: "2024-01-15",
    image: "/placeholder.svg?height=400&width=600&text=智能座舱",
    category: "系统更新"
  },
  {
    id: 2,
    title: "自动驾驶功能升级",
    description: "新增高速公路自动驾驶辅助、自动泊车等多项智能驾驶功能。",
    date: "2024-01-12",
    image: "/placeholder.svg?height=400&width=600&text=自动驾驶",
    category: "功能升级"
  },
  {
    id: 3,
    title: "车载娱乐系统优化",
    description: "支持更多流媒体平台，优化音频系统，提供更好的娱乐体验。",
    date: "2024-01-10",
    image: "/placeholder.svg?height=400&width=600&text=娱乐系统",
    category: "功能优化"
  },
  {
    id: 4,
    title: "远程控制功能增强",
    description: "新增远程启动空调、远程解锁等便捷功能，提升用车体验。",
    date: "2024-01-08",
    image: "/placeholder.svg?height=400&width=600&text=远程控制",
    category: "功能升级"
  },
  {
    id: 5,
    title: "安全系统更新",
    description: "升级碰撞预警系统，增强夜间行车安全性能。",
    date: "2024-01-05",
    image: "/placeholder.svg?height=400&width=600&text=安全系统",
    category: "安全更新"
  },
  {
    id: 6,
    title: "导航系统优化",
    description: "更新地图数据，优化路线规划算法，提供更准确的导航服务。",
    date: "2024-01-03",
    image: "/placeholder.svg?height=400&width=600&text=导航系统",
    category: "功能优化"
  }
]

const categories = ["全部", "系统更新", "功能升级", "功能优化", "安全更新"]

export function RecentUpdates() {
  const [selectedCategory, setSelectedCategory] = useState("全部")
  const [currentPage, setCurrentPage] = useState(1)
  const updatesPerPage = 3
  const [showVideo, setShowVideo] = useState(false)

  const filteredUpdates = updates.filter(update => 
    selectedCategory === "全部" || update.category === selectedCategory
  )

  const totalPages = Math.ceil(filteredUpdates.length / updatesPerPage)
  const paginatedUpdates = filteredUpdates.slice(
    (currentPage - 1) * updatesPerPage,
    currentPage * updatesPerPage
  )

  return (
    <div className="py-12 bg-[#151922] rounded-3xl shadow-md mb-12">
      <div className="container mx-auto px-4">
        <motion.h2 
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-2xl font-bold mb-8 text-white"
        >
          最新更新
        </motion.h2>

        {/* Category Filter */}
        <ScrollArea className="w-full mb-8">
          <div className="flex space-x-2 pb-4">
            {categories.map((category, index) => (
              <motion.div
                key={category}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, delay: index * 0.1 }}
              >
                <Button
                  variant={category === selectedCategory ? "default" : "outline"}
                  onClick={() => {
                    setSelectedCategory(category)
                    setCurrentPage(1)
                  }}
                  className={cn(
                    "transition-all duration-200",
                    category === selectedCategory 
                      ? "bg-amber-500 text-black" 
                      : "border-gray-700 hover:border-amber-500 text-gray-400 hover:text-amber-500"
                  )}
                >
                  {category}
                </Button>
              </motion.div>
            ))}
          </div>
          <ScrollBar orientation="horizontal" />
        </ScrollArea>

        {/* Updates Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {paginatedUpdates.map((update) => (
            <motion.div
              key={update.id}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5 }}
              className="bg-[#1C2028] border border-gray-700/50 rounded-xl overflow-hidden shadow-md hover:shadow-lg transition-shadow"
            >
              <div className="relative aspect-video">
                <LazyImage
                  src={update.image}
                  alt={update.title}
                  width={600}
                  height={400}
                  className="object-cover w-full h-full"
                />
              </div>
              <div className="p-6">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-sm text-gray-400">{update.date}</span>
                  <span className="text-sm font-medium text-amber-500">{update.category}</span>
                </div>
                <h3 className="text-xl font-semibold mb-2 text-white">{update.title}</h3>
                <p className="text-gray-400">{update.description}</p>
              </div>
            </motion.div>
          ))}
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="flex items-center justify-center gap-2 mt-8">
            <Button
              variant="ghost"
              size="icon"
              onClick={() => setCurrentPage(page => Math.max(1, page - 1))}
              disabled={currentPage === 1}
              className="text-gray-400 hover:text-amber-500"
            >
              <ChevronLeft className="h-4 w-4" />
            </Button>
            {Array.from({length: totalPages}, (_, i) => i + 1).map(page => (
              <Button
                key={page}
                variant={currentPage === page ? "default" : "ghost"}
                className={cn(
                  "w-10 h-10 p-0 rounded-full",
                  currentPage === page 
                    ? "bg-amber-500 text-black" 
                    : "text-gray-400 hover:text-amber-500"
                )}
                onClick={() => setCurrentPage(page)}
              >
                {page}
              </Button>
            ))}
            <Button
              variant="ghost"
              size="icon"
              onClick={() => setCurrentPage(page => Math.min(totalPages, page + 1))}
              disabled={currentPage === totalPages}
              className="text-gray-400 hover:text-amber-500"
            >
              <ChevronRight className="h-4 w-4" />
            </Button>
          </div>
        )}
      </div>
    </div>
  )
}

