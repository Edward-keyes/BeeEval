import Image from 'next/image'
import { Card, CardContent } from "@/components/ui/card"

export function UXSync() {
  return (
    <div className="py-16 bg-gray-50">
      <div className="container mx-auto px-6">
        <div className="grid lg:grid-cols-2 gap-12 items-center">
          <div>
            <h2 className="text-3xl font-bold mb-6">UXSync 新体验</h2>
            <p className="text-lg text-gray-600 mb-8">
              我们打造了全新的用户体验系统，让您能够更轻松地管理和使用您的爱车。
              通过智能化的界面设计和直观的操作流程，为您提供更好的服务体验。
            </p>
            <div className="grid grid-cols-2 gap-6">
              <Card>
                <CardContent className="pt-6">
                  <h3 className="font-semibold mb-2">智能推荐</h3>
                  <p className="text-sm text-muted-foreground">
                    基于您的使用习惯，为您推荐最适合的功能和服务
                  </p>
                </CardContent>
              </Card>
              <Card>
                <CardContent className="pt-6">
                  <h3 className="font-semibold mb-2">数据同步</h3>
                  <p className="text-sm text-muted-foreground">
                    实时同步您的车辆数据，随时掌握车辆状态
                  </p>
                </CardContent>
              </Card>
            </div>
          </div>
          <div className="relative h-[400px]">
            <Image
              src="/placeholder.svg?height=400&width=600"
              alt="UXSync Interface"
              fill
              className="object-cover rounded-lg"
            />
          </div>
        </div>
      </div>
    </div>
  )
}

