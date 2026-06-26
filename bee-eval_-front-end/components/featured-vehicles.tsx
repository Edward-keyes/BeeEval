'use client'

import { useState } from 'react'
import Image from 'next/image'
import { ChevronLeft, ChevronRight } from 'lucide-react'
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"

const vehicles = [
  { id: 1, name: '特斯拉 Model S', price: '688,900', image: '/placeholder.svg?height=400&width=600' },
  { id: 2, name: '保时捷 Taycan', price: '898,000', image: '/placeholder.svg?height=400&width=600' },
  { id: 3, name: '蔚来 ET7', price: '458,000', image: '/placeholder.svg?height=400&width=600' },
]

export function FeaturedVehicles() {
  const [currentIndex, setCurrentIndex] = useState(0)

  const nextSlide = () => {
    setCurrentIndex((prevIndex) => (prevIndex + 1) % vehicles.length)
  }

  const prevSlide = () => {
    setCurrentIndex((prevIndex) => (prevIndex - 1 + vehicles.length) % vehicles.length)
  }

  return (
    <Card className="relative overflow-hidden">
      <CardContent className="p-0">
        <div className="relative h-[400px]">
          <Image
            src={vehicles[currentIndex].image}
            alt={vehicles[currentIndex].name}
            fill
            className="object-cover"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent" />
          <div className="absolute bottom-0 left-0 right-0 p-6 text-white">
            <h3 className="text-2xl font-bold mb-2">{vehicles[currentIndex].name}</h3>
            <p className="text-xl">起步价：¥{vehicles[currentIndex].price}</p>
          </div>
        </div>
        <div className="absolute top-1/2 left-4 transform -translate-y-1/2">
          <Button variant="outline" size="icon" onClick={prevSlide}>
            <ChevronLeft className="h-4 w-4" />
          </Button>
        </div>
        <div className="absolute top-1/2 right-4 transform -translate-y-1/2">
          <Button variant="outline" size="icon" onClick={nextSlide}>
            <ChevronRight className="h-4 w-4" />
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}

