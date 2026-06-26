'use client'

import Image from "next/image"
import { motion } from "framer-motion"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { FileText, Map, Pencil, Play } from 'lucide-react'

const models = [
  {
    id: 1,
    name: "方程豹 8",
    image: "/placeholder.svg?height=200&width=300",
    features: 1183,
    duration: { hours: 4, minutes: 30 },
    preview: false,
    is4k: true
  },
  {
    id: 2,
    name: "智界 R7",
    image: "/placeholder.svg?height=200&width=300",
    features: 66,
    duration: { hours: 0, minutes: 0 },
    preview: true,
    is4k: true
  },
  {
    id: 3,
    name: "小鹏 P7+",
    image: "/placeholder.svg?height=200&width=300",
    features: 1019,
    duration: { hours: 3, minutes: 35 },
    preview: false,
    is4k: true
  },
  {
    id: 4,
    name: "极越 07",
    image: "/placeholder.svg?height=200&width=300",
    features: 873,
    duration: { hours: 4, minutes: 10 },
    preview: false,
    is4k: true
  }
]

export function ModelGrid() {
  return (
    <div className="py-6">
      <div className="text-sm text-muted-foreground mb-4">
        共116车型符合要求
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {models.map((model) => (
          <motion.div
            key={model.id}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
          >
            <Card className="overflow-hidden">
              <CardContent className="p-0">
                <div className="relative aspect-[4/3]">
                  <Image
                    src={model.image}
                    alt={model.name}
                    fill
                    className="object-cover"
                  />
                  <div className="absolute top-2 right-2 flex gap-2">
                    {model.is4k && (
                      <Badge variant="secondary" className="bg-black/50 text-white">
                        4K
                      </Badge>
                    )}
                    {model.preview && (
                      <Badge className="bg-primary text-primary-foreground">
                        Preview
                      </Badge>
                    )}
                  </div>
                </div>
                <div className="p-4">
                  <div className="flex justify-between items-start mb-4">
                    <h3 className="text-lg font-semibold">{model.name}</h3>
                    <Button variant="ghost" size="icon" className="text-primary">
                      <Play className="h-4 w-4" />
                    </Button>
                  </div>
                  <div className="grid grid-cols-2 gap-4 text-sm mb-4">
                    <div>
                      <div className="text-muted-foreground">功能拆解</div>
                      <div className="font-medium">{model.features} 个</div>
                    </div>
                    <div>
                      <div className="text-muted-foreground">视频总时长</div>
                      <div className="font-medium">
                        {model.duration.hours}小时{model.duration.minutes}分钟
                      </div>
                    </div>
                  </div>
                  <div className="grid grid-cols-2 gap-2">
                    <Button variant="outline" className="w-full" size="sm">
                      <FileText className="h-4 w-4 mr-2" />
                      车型报告
                    </Button>
                    <Button variant="outline" className="w-full" size="sm">
                      <Map className="h-4 w-4 mr-2" />
                      功能地图
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          </motion.div>
        ))}
      </div>
    </div>
  )
}

