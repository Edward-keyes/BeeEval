import { useState, useEffect } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { BrandSelectionPopover } from './brand-selection-popover'
import { Eye, EyeOff } from 'lucide-react'
import { CapabilityAssessment } from './capability-assessment'

export function VehicleInfo() {
  const [showIndustryAverage, setShowIndustryAverage] = useState(true)
  const [selectedBrands, setSelectedBrands] = useState<string[]>([])

  useEffect(() => {
    // Simulated API call to get all brands and set them as selected
    const allBrands = ['Brand1', 'Brand2', 'Brand3', 'Brand4', 'Brand5'] // Replace with actual brand data
    setSelectedBrands(allBrands)
  }, [])

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle>车型基本信息</CardTitle>
          <div className="flex items-center gap-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setShowIndustryAverage(!showIndustryAverage)}
              className="gap-2"
            >
              {showIndustryAverage ? (
                <>
                  <EyeOff className="h-4 w-4" />
                  隐藏行业均值
                </>
              ) : (
                <>
                  <Eye className="h-4 w-4" />
                  显示行业均值
                </>
              )}
            </Button>
            <BrandSelectionPopover
              selectedBrands={selectedBrands}
              onSelectedBrandsChange={setSelectedBrands}
            />
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {/* Vehicle basic information content */}
        {/* ... */}
        
        {/* Capability Assessment */}
        <CapabilityAssessment 
          showIndustryAverage={showIndustryAverage} 
          selectedBrands={selectedBrands} 
        />
      </CardContent>
    </Card>
  )
}

