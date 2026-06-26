'use client'

import { motion } from 'framer-motion'
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { LazyImage } from '@/components/lazy-image'
import { Play, Calendar, ChevronRight } from 'lucide-react'
import Link from 'next/link'

const carModels = [
  { 
    id: 16, 
    name: '007', 
    brand: '极氪', 
    image: '/placeholder.svg?height=200&width=300&text=极氪+007',
    version: "2024款 长续航版",
    testDate: '2024-03-15',
  },
  { 
    id: 17, 
    name: 'L6', 
    brand: '理想', 
    image: '/placeholder.svg?height=200&width=300&text=理想+L6',
    version: "2024款 旗舰版",
    testDate: '2024-03-14',
  },
  { 
    id: 18, 
    name: 'SU7', 
    brand: '小米', 
    image: '/placeholder.svg?height=200&width=300&text=小米+SU7',
    version: "2024款 高性能版",
    testDate: '2024-03-13',
  },
  { 
    id: 19, 
    name: '07', 
    brand: '极越', 
    image: '/placeholder.svg?height=200&width=300&text=极越+07',
    version: "2024款 智能豪华版",
    testDate: '2024-03-12',
  },
  { 
    id: 1, 
    name: 'Model S', 
    brand: 'Tesla', 
    image: '/placeholder.svg?height=200&width=300&text=Tesla+Model+S',
    version: "2024款 长续航版",
    testDate: '2024-03-11',
  },
  { 
    id: 2, 
    name: 'ET7', 
    brand: 'NIO', 
    image: '/placeholder.svg?height=200&width=300&text=NIO+ET7',
    version: "2024款 100kWh 智能电动旗舰轿车",
    testDate: '2024-03-10',
  },
  { 
    id: 3, 
    name: 'EQS', 
    brand: 'Mercedes-Benz', 
    image: '/placeholder.svg?height=200&width=300&text=Mercedes+EQS',
    version: "2024款 EQS 580 4MATIC",
    testDate: '2024-03-09',
  },
  { 
    id: 4, 
    name: 'P7', 
    brand: 'XPeng', 
    image: '/placeholder.svg?height=200&width=300&text=XPeng+P7',
    version: "2024款 智能性能版",
    testDate: '2024-03-08',
  },
  { 
    id: 5, 
    name: 'Air', 
    brand: 'Lucid', 
    image: '/placeholder.svg?height=200&width=300&text=Lucid+Air',
    version: "2024款 Grand Touring",
    testDate: '2024-03-07',
  },
  { 
    id: 6, 
    name: 'iX', 
    brand: 'BMW', 
    image: '/placeholder.svg?height=200&width=300&text=BMW+iX',
    version: "2024款 xDrive50",
    testDate: '2024-03-06',
  },
  { 
    id: 7, 
    name: 'Taycan', 
    brand: 'Porsche', 
    image: '/placeholder.svg?height=200&width=300&text=Porsche+Taycan',
    version: "2024款 Turbo S",
    testDate: '2024-03-05',
  },
  { 
    id: 8, 
    name: 'e-tron GT', 
    brand: 'Audi', 
    image: '/placeholder.svg?height=200&width=300&text=Audi+e-tron+GT',
    version: "2024款 RS e-tron GT",
    testDate: '2024-03-04',
  },
  { 
    id: 9, 
    name: 'EQE', 
    brand: 'Mercedes-Benz', 
    image: '/placeholder.svg?height=200&width=300&text=Mercedes+EQE',
    version: "2024款 EQE 350+",
    testDate: '2024-03-03',
  },
  { 
    id: 10, 
    name: 'I-PACE', 
    brand: 'Jaguar', 
    image: '/placeholder.svg?height=200&width=300&text=Jaguar+I-PACE',
    version: "2024款 EV400 SE",
    testDate: '2024-03-02',
  },
  { 
    id: 11, 
    name: 'Ioniq 5', 
    brand: 'Hyundai', 
    image: '/placeholder.svg?height=200&width=300&text=Hyundai+Ioniq+5',
    version: "2024款 长续航四驱版",
    testDate: '2024-03-01',
  },
  { 
    id: 12, 
    name: 'EV6', 
    brand: 'Kia', 
    image: '/placeholder.svg?height=200&width=300&text=Kia+EV6',
    version: "2024款 GT-Line AWD",
    testDate: '2024-02-28',
  },
  { 
    id: 13, 
    name: 'Ocean', 
    brand: 'Fisker', 
    image: '/placeholder.svg?height=200&width=300&text=Fisker+Ocean',
    version: "2024款 Extreme",
    testDate: '2024-02-27',
  },
  { 
    id: 14, 
    name: 'Polestar 2', 
    brand: 'Polestar', 
    image: '/placeholder.svg?height=200&width=300&text=Polestar+2',
    version: "2024款 长续航双电机版",
    testDate: '2024-02-26',
  },
  { 
    id: 15, 
    name: 'ID.4', 
    brand: 'Volkswagen', 
    image: '/placeholder.svg?height=200&width=300&text=Volkswagen+ID.4',
    version: "2024款 Pro S AWD",
    testDate: '2024-02-25',
  }
];

export function AllModelsGrid() {
  return (
    <div className="relative">
      {/* Background Gradient Effects */}
      <div className="absolute top-0 left-1/4 w-1/2 h-1/2 bg-amber-500/10 blur-[120px] rounded-full"></div>
      <div className="absolute bottom-0 right-1/4 w-1/2 h-1/2 bg-amber-500/5 blur-[120px] rounded-full"></div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 relative">
        {carModels.map((model) => (
          <motion.div
            key={model.id}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            whileHover={{ scale: 1.03 }}
          >
            <Card className="overflow-hidden h-full flex flex-col group bg-[#0D1117] border-gray-800/50 hover:border-amber-500 transition-all duration-300">
              <div className="relative aspect-[4/3]">
                <LazyImage
                  src={model.image}
                  alt={`${model.brand} ${model.name}`}
                  fill
                  className="object-cover"
                />
                <div className="absolute top-2 right-2 flex gap-2">
                  {model.id <= 2 && (
                    <Badge className="bg-amber-500/80 text-black">
                      Preview
                    </Badge>
                  )}
                </div>
                <div className="absolute inset-0 bg-black/60 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                  <div className="flex flex-col space-y-2 w-full px-4">
                    <Link href={`/vehicle-detail/${model.id}`}>
                      <Button variant="secondary" size="lg" className="w-full bg-amber-500 hover:bg-amber-600 text-black font-semibold">
                        <div className="flex items-center justify-center text-sm">
                          大模型能力测评
                          <ChevronRight className="ml-2 h-4 w-4" />
                        </div>
                      </Button>
                    </Link>
                    <Link href={`/llm-capabilities?id=${model.id}`}>
                      <Button variant="secondary" size="lg" className="w-full bg-amber-500 hover:bg-amber-600 text-black font-semibold">
                        <div className="flex items-center justify-center text-sm">
                          大模型能力功能树
                          <ChevronRight className="ml-2 h-4 w-4" />
                        </div>
                      </Button>
                    </Link>
                  </div>
                </div>
              </div>
              <CardContent className="flex-grow p-4">
                <h3 className="text-lg font-semibold mb-2 text-white group-hover:text-amber-500 transition-colors">{model.brand} {model.name}</h3>
                <p className="text-sm text-gray-400 mb-4">{model.version}</p>
                <div className="text-sm text-gray-400">
                  <div className="flex items-center gap-2">
                    <Calendar className="h-4 w-4 text-amber-500" />
                    <span>测试日期：{model.testDate}</span>
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

