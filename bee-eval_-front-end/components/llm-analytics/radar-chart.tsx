'use client'

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ResponsiveContainer, RadarChart as RechartsRadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, Radar, Legend } from 'recharts'

const domainData = {
  "车书域": [
    { subject: '准确性', A: 120, B: 110, fullMark: 150 },
    { subject: '响应速度', A: 98, B: 130, fullMark: 150 },
    { subject: '理解能力', A: 86, B: 130, fullMark: 150 },
    { subject: '知识覆盖', A: 99, B: 100, fullMark: 150 },
    { subject: '上下文理解', A: 85, B: 90, fullMark: 150 },
    { subject: '个性化', A: 65, B: 85, fullMark: 150 },
  ],
  // ... 其他域的数据
}

interface RadarChartProps {
  domain: string
}

export function RadarChart({ domain }: RadarChartProps) {
  const data = domainData[domain] || []

  return (
    <Card className="overflow-hidden border-2 border-primary/10">
      <CardHeader className="bg-gradient-to-r from-primary/5 to-primary/10 border-b border-primary/10">
        <CardTitle className="text-2xl font-bold text-primary">{domain}评价指标</CardTitle>
      </CardHeader>
      <CardContent className="p-6">
        <div className="h-[400px]">
          <ResponsiveContainer width="100%" height="100%">
            <RechartsRadarChart cx="50%" cy="50%" outerRadius="80%" data={data}>
              <PolarGrid />
              <PolarAngleAxis dataKey="subject" />
              <PolarRadiusAxis angle={30} domain={[0, 150]} />
              <Radar name="当前车型" dataKey="A" stroke="#8884d8" fill="#8884d8" fillOpacity={0.6} />
              <Radar name="行业平均" dataKey="B" stroke="#82ca9d" fill="#82ca9d" fillOpacity={0.6} />
              <Legend />
            </RechartsRadarChart>
          </ResponsiveContainer>
        </div>
      </CardContent>
    </Card>
  )
}

