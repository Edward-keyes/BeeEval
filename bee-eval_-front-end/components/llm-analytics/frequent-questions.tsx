'use client'

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { motion } from "framer-motion"
import { ArrowRight } from 'lucide-react'
import Image from "next/image"
import Link from "next/link"

const questionsData = [
  {
    title: "主观意图识别",
    count: 1000,
    image: "/placeholder.svg?height=200&width=300",
    link: "/analytics/intent"
  },
  {
    title: "用车技巧",
    count: 900,
    image: "/placeholder.svg?height=200&width=300",
    link: "/analytics/tips"
  },
  {
    title: "出行生态信息",
    count: 800,
    image: "/placeholder.svg?height=200&width=300",
    link: "/analytics/travel"
  }
]

export function FrequentQuestions() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5, delay: 0.4 }}
    >
      <Card className="overflow-hidden border-2 border-primary/10">
        <CardHeader className="bg-gradient-to-r from-primary/5 to-primary/10 border-b border-primary/10">
          <div className="flex items-center justify-between">
            <CardTitle className="text-2xl font-bold text-primary">高频问题</CardTitle>
            <Link 
              href="/analytics/questions"
              className="text-sm text-muted-foreground hover:text-primary transition-colors flex items-center gap-1"
            >
              查看全部 <ArrowRight className="h-4 w-4" />
            </Link>
          </div>
        </CardHeader>
        <CardContent className="p-6">
          <div className="grid md:grid-cols-3 gap-6">
            {questionsData.map((item, index) => (
              <motion.div
                key={item.title}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, delay: 0.1 * (index + 1) }}
              >
                <Card className="overflow-hidden hover:border-primary transition-colors">
                  <div className="relative aspect-video">
                    <Image
                      src={item.image}
                      alt={item.title}
                      fill
                      className="object-cover"
                    />
                    <div className="absolute top-2 right-2 bg-muted-foreground/20 backdrop-blur-sm text-foreground text-sm font-medium px-2 py-1 rounded-full">
                      {item.count}次
                    </div>
                  </div>
                  <CardContent className="p-4">
                    <h3 className="font-medium text-center">{item.title}</h3>
                  </CardContent>
                </Card>
              </motion.div>
            ))}
          </div>
        </CardContent>
      </Card>
    </motion.div>
  )
}

