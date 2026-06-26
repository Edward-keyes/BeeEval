'use client'

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ResponsiveContainer, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts'
import { motion } from "framer-motion"

const domainData = [
  { name: '车书域', score: 3.583, average: 3.1 },
  { name: '出行域', score: 2.989, average: 2.7 },
  { name: '娱乐域', score: 3.493, average: 3.2 },
  { name: '闲聊域', score: 3.559, average: 3.3 },
  { name: '创作域', score: 3.884, average: 3.5 },
  { name: '有话域', score: 3.139, average: 2.9 },
]

export function DomainScores() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
    >
      <Card className="overflow-hidden border-2 border-primary/10">
        <CardHeader className="bg-gradient-to-r from-primary/5 to-primary/10 border-b border-primary/10">
          <CardTitle className="text-2xl font-bold text-primary">功能域评分</CardTitle>
        </CardHeader>
        <CardContent className="p-6">
          <div className="h-[400px]">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart
                data={domainData}
                margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
              >
                <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
                <XAxis 
                  dataKey="name"
                  axisLine={false}
                  tickLine={false}
                  className="text-xs font-medium"
                />
                <YAxis 
                  axisLine={false}
                  tickLine={false}
                  className="text-xs font-medium"
                />
                <Tooltip
                  contentStyle={{
                    backgroundColor: 'hsl(var(--background))',
                    border: '1px solid hsl(var(--border))',
                    borderRadius: '8px',
                  }}
                />
                <Legend />
                <Bar 
                  dataKey="score" 
                  name="当前车型" 
                  fill="hsl(var(--primary))"
                  radius={[4, 4, 0, 0]}
                />
                <Bar 
                  dataKey="average" 
                  name="行业平均" 
                  fill="hsl(var(--muted-foreground))"
                  radius={[4, 4, 0, 0]}
                  fillOpacity={0.3}
                />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  )
}

