'use client'

import * as React from "react"
import { motion } from "framer-motion"
import { cn } from "@/lib/utils"
import Link from 'next/link'
import { usePathname } from 'next/navigation'

const tabs = [
  { id: 'skill-tree', label: '大模型技能树详情' },
  { id: 'evaluation', label: '大模型评测详情' },
  { id: 'overview', label: '概览' },
  { id: 'features', label: '功能详情' },
  { id: 'comparison', label: '竞品对比' },
  { id: 'reviews', label: '用户评价' },
]

export function VehicleDetailTabs() {
  const pathname = usePathname()

  return (
    <div className="mb-6">
      <div className="flex space-x-1 glass-card p-1 rounded-lg">
        {tabs.map((tab) => (
          <Link
            key={tab.id}
            href={`/vehicle-detail/${tab.id}`}
            className="relative flex-1"
          >
            <motion.div
              className={`w-full py-2 text-center text-sm font-medium rounded-md transition-colors ${
                pathname.includes(tab.id) ? 'text-primary' : 'text-muted-foreground'
              }`}
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              {tab.label}
              {pathname.includes(tab.id) && (
                <motion.div
                  className="absolute bottom-0 left-0 right-0 h-0.5 bg-primary"
                  layoutId="activeTab"
                />
              )}
            </motion.div>
          </Link>
        ))}
      </div>
    </div>
  )
}

export default VehicleDetailTabs;

