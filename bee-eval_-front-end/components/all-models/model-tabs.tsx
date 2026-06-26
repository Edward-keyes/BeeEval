'use client'

import * as React from "react"
import { motion } from "framer-motion"
import { cn } from "@/lib/utils"

const tabs = [
  { id: 'all', label: '全部车型' },
  { id: 'vip', label: 'VIP车型' },
  { id: 'preview', label: '速览车型' },
  { id: 'free', label: '免费车型' },
]

export function ModelTabs() {
  const [activeTab, setActiveTab] = React.useState('all')

  return (
    <div className="border-b">
      <div className="flex space-x-8">
        {tabs.map((tab) => (
          <motion.button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={cn(
              "relative py-4 text-sm font-medium transition-colors hover:text-primary",
              activeTab === tab.id ? "text-primary" : "text-muted-foreground"
            )}
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
          >
            {tab.label}
            {activeTab === tab.id && (
              <motion.div
                className="absolute bottom-0 left-0 right-0 h-0.5 bg-primary"
                layoutId="activeTab"
              />
            )}
          </motion.button>
        ))}
      </div>
    </div>
  )
}

