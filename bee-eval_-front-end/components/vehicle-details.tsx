import Image from 'next/image'
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"

export function VehicleDetails() {
  return (
    <div className="grid lg:grid-cols-2 gap-8">
      <div className="relative aspect-[16/9]">
        <Image
          src="/placeholder.svg?height=400&width=600"
          alt="Vehicle Preview"
          fill
          className="object-cover rounded-lg"
        />
      </div>
      <Card>
        <CardContent className="p-6">
          <div className="flex justify-between items-start mb-6">
            <div>
              <h2 className="text-2xl font-bold mb-2">特斯拉 Model S</h2>
              <div className="flex gap-2">
                <Badge>纯电动</Badge>
                <Badge variant="outline">续航600km</Badge>
              </div>
            </div>
            <Button>预约试驾</Button>
          </div>
          
          <div className="grid grid-cols-2 gap-4 mb-6">
            <div>
              <div className="text-sm text-muted-foreground">起步价</div>
              <div className="text-xl font-semibold">￥688,900</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground">最大功率</div>
              <div className="text-xl font-semibold">670 kW</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground">百公里加速</div>
              <div className="text-xl font-semibold">2.1 秒</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground">最高时速</div>
              <div className="text-xl font-semibold">322 km/h</div>
            </div>
          </div>

          <div className="space-y-4">
            <h3 className="font-semibold">配置亮点</h3>
            <div className="grid grid-cols-2 gap-2 text-sm">
              <div className="flex items-center gap-2">
                <div className="w-2 h-2 bg-green-500 rounded-full" />
                自动驾驶辅助
              </div>
              <div className="flex items-center gap-2">
                <div className="w-2 h-2 bg-green-500 rounded-full" />
                全景天窗
              </div>
              <div className="flex items-center gap-2">
                <div className="w-2 h-2 bg-green-500 rounded-full" />
                空气悬架
              </div>
              <div className="flex items-center gap-2">
                <div className="w-2 h-2 bg-green-500 rounded-full" />
                智能座舱
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

