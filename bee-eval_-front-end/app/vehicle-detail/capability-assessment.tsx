import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { ResponsiveContainer, RadarChart, PolarGrid, PolarAngleAxis, Radar, BarChart, XAxis, YAxis, Tooltip, Legend, Bar, Cell } from 'recharts'
import { Badge } from "@/components/ui/badge"
import { Progress } from "@/components/ui/progress"
import { useState, useMemo } from 'react'
import { BrandSelectionPopover } from './brand-selection-popover'
import { Mic, Star, Eye, EyeOff } from 'lucide-react'
import { Button } from "@/components/ui/button"
import { motion } from 'framer-motion'
import { CartesianGrid } from 'recharts'
import { LabelList } from 'recharts'
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area"
import Image from 'next/image'
import { PolarRadiusAxis } from 'recharts'
import { DotProps } from 'recharts'

// ... existing interfaces ...

// Add new NavigationBar component
function NavigationBar({ currentVehicle }: { currentVehicle: { name: string } }) {
  return (
    <div className="fixed top-0 left-0 right-0 z-50 bg-[#1C2028] border-b border-gray-800">
      <div className="max-w-[1440px] mx-auto px-6 py-4">
        <div className="flex justify-between items-center">
          <div className="space-y-1">
            <h1 className="text-2xl font-semibold text-white">{currentVehicle.name}</h1>
            <p className="text-sm text-gray-400">2024款 Max版</p>
          </div>
          <div className="flex items-center space-x-12 text-sm text-gray-400">
            <div className="flex items-center">
              <span className="mr-2">系统版本</span>
              <span className="text-white">Zeekr OS 5.0</span>
            </div>
            <div className="flex items-center">
              <span className="mr-2">测试时间</span>
              <span className="text-white">2024年02月01日</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export function CapabilityAssessment({ showIndustryAverage, selectedBrands, currentVehicle = { name: "极氪007" } }: CapabilityAssessmentProps) {
  const [selectedDomain, setSelectedDomain] = useState<keyof typeof domainDetailData | null>(null);
  const [selectedMetric, setSelectedMetric] = useState<string | null>(null);
  const [perceptionType, setPerceptionType] = useState<'hearing' | 'visual'>('hearing');
  const [showIndustryAverageState, setShowIndustryAverageState] = useState(showIndustryAverage);
  const [selectedBrandsState, setSelectedBrandsState] = useState<string[]>(selectedBrands);

  // 获取当前车型的感知能力数据
  const currentPerceptionData = useMemo(() => {
    return vehiclePerceptionData[currentVehicle.name] || vehiclePerceptionData["极氪007"];
  }, [currentVehicle.name]);

  const handleBrandsChange = (brands: string[]) => {
    setSelectedBrandsState(brands);
  };

  return (
    <div className="min-h-screen bg-[#141414]">
      {/* Fixed Navigation Bar */}
      <div className="fixed top-0 left-0 right-0 z-50 bg-[#1C2028] border-b border-gray-800">
        <div className="max-w-[1440px] mx-auto px-6 py-4">
          <div className="flex justify-between items-center">
            <div className="space-y-1">
              <h1 className="text-2xl font-semibold text-white">{currentVehicle.name}</h1>
              <p className="text-sm text-gray-400">2024款 Max版</p>
            </div>
            <div className="flex items-center space-x-12 text-sm text-gray-400">
              <div className="flex items-center">
                <span className="mr-2">系统版本</span>
                <span className="text-white">Zeekr OS 5.0</span>
              </div>
              <div className="flex items-center">
                <span className="mr-2">测试时间</span>
                <span className="text-white">2024年02月01日</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="pt-24 px-6 max-w-[1440px] mx-auto">
        <Tabs defaultValue="capability" className="w-full">
          <TabsList className="w-full bg-transparent border-b border-gray-800 p-0 h-12">
            <TabsTrigger
              value="capability"
              className="data-[state=active]:border-b-2 data-[state=active]:border-amber-500 data-[state=active]:text-amber-500 rounded-none h-full px-8"
            >
              能力评测
            </TabsTrigger>
          </TabsList>
          <TabsContent value="capability" className="mt-6">
            // ... rest of your existing content ...
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
} 
