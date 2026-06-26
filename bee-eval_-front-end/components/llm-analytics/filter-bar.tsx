'use client'

import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Switch } from "@/components/ui/switch"
import { Label } from "@/components/ui/label"
import { BrandSelectionPopover } from "@/components/vehicle-detail/brand-selection-popover"
import { Eye, EyeOff } from 'lucide-react'

interface FilterBarProps {
  showIndustryAverage: boolean
  setShowIndustryAverage: (show: boolean) => void
  selectedBrands: string[]
  setSelectedBrands: (brands: string[]) => void
}

export function FilterBar({
  showIndustryAverage,
  setShowIndustryAverage,
  selectedBrands,
  setSelectedBrands
}: FilterBarProps) {
  return (
    <Card className="mb-6 glass-card">
      <CardContent className="py-3">
        <div className="flex items-center justify-between">
          <div className="text-sm font-medium">筛选条件：</div>
          <div className="flex items-center gap-4">
            <div className="flex items-center space-x-2">
              <Switch
                id="industry-average"
                checked={showIndustryAverage}
                onCheckedChange={setShowIndustryAverage}
              />
              <Label htmlFor="industry-average" className="text-sm">
                {showIndustryAverage ? (
                  <span className="flex items-center">
                    <Eye className="w-4 h-4 mr-1" /> 显示行业均值
                  </span>
                ) : (
                  <span className="flex items-center">
                    <EyeOff className="w-4 h-4 mr-1" /> 隐藏行业均值
                  </span>
                )}
              </Label>
            </div>
            <BrandSelectionPopover
              selectedBrands={selectedBrands}
              onBrandsChange={setSelectedBrands}
              currentVehicle={{ name: '极氪007' }}
            >
              <Button variant="outline" size="sm">
                选择对比车型
              </Button>
            </BrandSelectionPopover>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

