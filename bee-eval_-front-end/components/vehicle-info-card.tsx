import Image from 'next/image'
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"

interface VehicleInfo {
  modelYear: string
  releaseDate: string
  powerType: string
  range: string
  acceleration: string
  maxSpeed: string
}

interface VehicleInfoCardProps {
  vehicleName: string
  imageUrl: string
  vehicleInfo: VehicleInfo
}

export function VehicleInfoCard({ vehicleName, imageUrl, vehicleInfo }: VehicleInfoCardProps) {
  return (
    <div className="p-6 border-b">
      <div className="flex flex-col md:flex-row gap-6">
        <div className="relative w-full md:w-1/3 aspect-[16/9]">
          <Image
            src={imageUrl}
            alt={vehicleName}
            fill
            className="object-cover rounded-lg"
          />
        </div>
        <div className="flex-1">
          <h2 className="text-2xl font-bold mb-4">{vehicleName}</h2>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
            <div>
              <div className="text-sm text-muted-foreground">车型年份</div>
              <div className="font-medium">{vehicleInfo.modelYear}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground">上市时间</div>
              <div className="font-medium">{vehicleInfo.releaseDate}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground">动力类型</div>
              <div className="font-medium">{vehicleInfo.powerType}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground">续航里程</div>
              <div className="font-medium">{vehicleInfo.range}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground">百公里加速</div>
              <div className="font-medium">{vehicleInfo.acceleration}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground">最高时速</div>
              <div className="font-medium">{vehicleInfo.maxSpeed}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

