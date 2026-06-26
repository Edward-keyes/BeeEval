'use client'

import * as React from "react"
import { motion } from "framer-motion"
import { Button } from "@/components/ui/button"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area"
import { cn } from "@/lib/utils"
import { Eye, EyeOff } from 'lucide-react'
import { BrandSelectionPopover } from "@/components/vehicle-detail/brand-selection-popover"
import { FilterDropdown } from "./filter-dropdown"
import { Card, CardContent } from "@/components/ui/card"
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs"

interface FilterBarProps {
  showIndustryAverage: boolean;
  setShowIndustryAverage: (show: boolean) => void;
  selectedBrands: string[];
  setSelectedBrands: (brands: string[]) => void;
  currentVehicle?: { name: string };
}

const filterCategories = [
  { id: 'all', label: '全部' },
  { id: 'basic', label: '基础能力' },
  { id: 'domain', label: '功能域' },
  { id: 'perception', label: '感知能力' },
  { id: 'personality', label: '性格特征' },
];

const timeOptions = [
  { id: 'latest', label: '最新版本', description: '当前最新发布的版本' },
  { id: 'v6.2', label: 'V6.2', description: '2024年3月发布' },
  { id: 'v6.1', label: 'V6.1', description: '2024年1月发布' },
  { id: 'v6.0', label: 'V6.0', description: '2023年12月发布' },
];

const scenarioOptions = [
  { id: 'all', label: '全部场景' },
  { id: 'daily', label: '日常对话', description: '日常交互和闲聊场景' },
  { id: 'travel', label: '出行服务', description: '导航、路况、周边服务等' },
  { id: 'control', label: '车辆控制', description: '车辆功能控制和状态查询' },
  { id: 'entertainment', label: '娱乐服务', description: '音乐、视频、游戏等' },
];

export function FilterBar({
  showIndustryAverage,
  setShowIndustryAverage,
  selectedBrands,
  setSelectedBrands,
  currentVehicle = { name: "极氪007" }
}: FilterBarProps) {
  const [selectedTime, setSelectedTime] = React.useState('latest')
  const [selectedScenario, setSelectedScenario] = React.useState('all')
  const [selectedCategory, setSelectedCategory] = React.useState('all')

  return (
    <div className="mb-6">
      <div className="flex flex-col gap-4">
        {/* 主要筛选器 */}
        <div className="flex space-x-1 glass-card p-1 rounded-lg">
          {filterCategories.map((category) => (
            <div
              key={category.id}
              className="relative flex-1"
            >
              <motion.button
                onClick={() => setSelectedCategory(category.id)}
                className={cn(
                  "w-full py-2.5 text-center text-sm font-medium rounded-md transition-colors",
                  selectedCategory === category.id ? "text-amber-500" : "text-muted-foreground"
                )}
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
              >
                {category.label}
                {selectedCategory === category.id && (
                  <motion.div
                    className="absolute bottom-0 left-0 right-0 h-0.5 bg-amber-500"
                    layoutId="activeCategory"
                  />
                )}
              </motion.button>
            </div>
          ))}
        </div>

        {/* 辅助筛选器 */}
        <div className="flex items-center justify-between px-1">
          <div className="flex items-center gap-4">
            <FilterDropdown
              label="版本选择"
              options={timeOptions}
              selectedOption={selectedTime}
              onSelect={setSelectedTime}
            />
            <FilterDropdown
              label="场景选择"
              options={scenarioOptions}
              selectedOption={selectedScenario}
              onSelect={setSelectedScenario}
            />
            <div className="h-4 w-px bg-gray-800" />
            <div className="flex items-center gap-2">
              <div className="flex items-center gap-2">
                <span className="inline-block w-3 h-3 rounded-full bg-amber-500" />
                <span className="text-sm text-muted-foreground">{currentVehicle.name}</span>
              </div>
              {selectedBrands
                .filter(brand => brand !== currentVehicle.name)
                .map((brand, index) => (
                  <div key={brand} className="flex items-center gap-2">
                    <span className="inline-block w-3 h-3 rounded-full" style={{ backgroundColor: index === 0 ? "#326199" : "#4FB1A1" }} />
                    <span className="text-sm text-muted-foreground">{brand}</span>
                  </div>
                ))}
            </div>
          </div>

          <div className="flex items-center gap-3">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setShowIndustryAverage(!showIndustryAverage)}
              className="h-8 px-4 text-sm font-medium rounded-md bg-black/20 text-gray-400 hover:bg-black/30 border-0 transition-colors"
            >
              {showIndustryAverage ? (
                <>
                  <Eye className="w-4 h-4 mr-2" />
                  隐藏行业均值
                </>
              ) : (
                <>
                  <EyeOff className="w-4 h-4 mr-2" />
                  显示行业均值
                </>
              )}
            </Button>

            <BrandSelectionPopover
              selectedBrands={selectedBrands}
              onBrandsChange={setSelectedBrands}
              currentVehicle={currentVehicle}
            >
              <Button
                variant="outline"
                size="sm"
                className="h-8 px-4 text-sm font-medium rounded-md bg-black/20 text-gray-400 hover:bg-black/30 border-0 transition-colors"
              >
                {selectedBrands.length > 1 ? (
                  <div className="flex items-center gap-2">
                    <span>{currentVehicle.name}</span>
                    <span className="px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-500 text-xs">
                      +{selectedBrands.length - 1}
                    </span>
                  </div>
                ) : (
                  "添加对比车型"
                )}
              </Button>
            </BrandSelectionPopover>
          </div>
        </div>
      </div>
    </div>
  );
} 