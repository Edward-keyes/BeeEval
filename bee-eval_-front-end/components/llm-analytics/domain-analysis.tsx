'use client'

import { Card, CardContent } from "@/components/ui/card"
import { ResponsiveContainer, RadarChart, PolarGrid, PolarAngleAxis, Radar, Tooltip } from 'recharts'
import { motion } from "framer-motion"
import Image from 'next/image'
import { Button } from "@/components/ui/button"
import { useState } from 'react'
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area"
import { Mic, Star } from 'lucide-react'

interface DomainAnalysisProps {
  domain: string
  showIndustryAverage: boolean
  selectedBrands: string[]
}

const domainData = {
  "出行域": {
    score: 4.1,
    industryAverage: 3.7,
    metrics: [
      { subject: '出行生态信息', value: 90, industryAverage: 83 },
      { subject: '生活生态信息', value: 87, industryAverage: 79 },
      { subject: '在线搜索', value: 93, industryAverage: 86 },
      { subject: '复杂指令识别率', value: 89, industryAverage: 81 },
      { subject: '直接指令识别率', value: 85, industryAverage: 77 },
      { subject: '场景意图识别率', value: 88, industryAverage: 80 },
      { subject: '跨域协作能力', value: 90, industryAverage: 82 },
      { subject: '生成质量', value: 92, industryAverage: 84 },
      { subject: '单轮次对话记忆', value: 91, industryAverage: 83 },
    ],
    videos: [],
    highScoreVideos: [
      { id: 1, title: "精准导航", description: "展示了车载系统在复杂路况下的精准导航能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 2, title: "智能语音交互", description: "演示了系统对自然语言的理解和响应能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 3, title: "实时路况预测", description: "通过大数据分析，准确预测前方路况", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
    frequentQuestionVideos: [
      { id: 4, title: "系统设置指南", description: "详细介绍车载系统的各项设置和个性化选项", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 5, title: "紧急救援功能", description: "演示了系统在紧急情况下的自动救援呼叫功能", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 6, title: "远程控制演示", description: "通过手机App远程控制车辆功能的操作指南", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
  },
  "车书域": {
    score: 4.2,
    industryAverage: 3.8,
    metrics: [
      { subject: '汽车知识', value: 92, industryAverage: 85 },
      { subject: '交通知识', value: 88, industryAverage: 80 },
      { subject: '用车技巧', value: 95, industryAverage: 88 },
      { subject: '直接指令识别率', value: 90, industryAverage: 82 },
      { subject: '跨域协作能力', value: 87, industryAverage: 79 },
      { subject: '生成质量', value: 89, industryAverage: 81 },
      { subject: '模态丰富性', value: 91, industryAverage: 83 },
    ],
    highScoreVideos: [
      { id: 1, title: "精准导航", description: "展示了车载系统在复杂路况下的精准导航能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 2, title: "智能语音交互", description: "演示了系统对自然语言的理解和响应能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 3, title: "实时路况预测", description: "通过大数据分析，准确预测前方路况", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 4, title: "多模态交互", description: "结合语音、手势和触控的多模态交互体验", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 5, title: "个性化推荐", description: "基于用户习惯，提供智能化的个性推荐服务", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 6, title: "车载娱乐系统", description: "丰富的车载娱乐功能，包括音乐、视频和游戏", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
    frequentQuestionVideos: [
      { id: 7, title: "系统设置指南", description: "详细介绍车载系统的各项设置和个性化选项", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 8, title: "紧急救援功能", description: "演示了系统在紧急情况下的自动救援呼叫功能", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 9, title: "远程控制演示", description: "通过手机App远程控制车辆功能的操作指南", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 10, title: "车载WiFi设置", description: "如何设置和使用车载WiFi热点功能", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 11, title: "语音命令大全", description: "常用语音命令的完整列表和使用技巧", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 12, title: "OTA升级指南", description: "如何进行车载系统的在线升级及注意事项", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
  },
  "车控域": {
    score: 4.3,
    industryAverage: 3.9,
    metrics: [
      { subject: '复杂指令识别率', value: 94, industryAverage: 87 },
      { subject: '直接指令识别率', value: 91, industryAverage: 83 },
      { subject: '主观意图识别率', value: 96, industryAverage: 89 },
      { subject: '场景意图识别率', value: 92, industryAverage: 84 },
      { subject: '跨域协作能力', value: 89, industryAverage: 81 },
      { subject: '模态丰富性', value: 90, industryAverage: 82 },
    ],
    videos: [],
    highScoreVideos: [
      { id: 1, title: "精准导航", description: "展示了车载系统在复杂路况下的精准导航能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 2, title: "智能语音交互", description: "演示了系统对自然语言的理解和响应能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 3, title: "实时路况预测", description: "通过大数据分析，准确预测前方路况", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
    frequentQuestionVideos: [
      { id: 4, title: "系统设置指南", description: "详细介绍车载系统的各项设置和个性化选项", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 5, title: "紧急救援功能", description: "演示了系统在紧急情况下的自动救援呼叫功能", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 6, title: "远程控制演示", description: "通过手机App远程控制车辆功能的操作指南", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
  },
  "闲聊域": {
    score: 4.0,
    industryAverage: 3.6,
    metrics: [
      { subject: '在线搜索', value: 89, industryAverage: 82 },
      { subject: '复杂指令识别率', value: 86, industryAverage: 78 },
      { subject: '直接指令识别率', value: 92, industryAverage: 85 },
      { subject: '主观意图识别率', value: 88, industryAverage: 80 },
      { subject: '场景意图识别率', value: 84, industryAverage: 76 },
      { subject: '跨域协作能力', value: 87, industryAverage: 79 },
      { subject: '生成质量', value: 89, industryAverage: 81 },
      { subject: '模态丰富性', value: 91, industryAverage: 83 },
      { subject: '情感响应有效性', value: 90, industryAverage: 82 },
      { subject: '情感调节能力', value: 85, industryAverage: 77 },
      { subject: '单轮次对话记忆', value: 93, industryAverage: 86 },
      { subject: '跨对话记忆', value: 88, industryAverage: 80 },
    ],
    videos: [],
    highScoreVideos: [
      { id: 1, title: "精准导航", description: "展示了车载系统在复杂路况下的精准导航能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 2, title: "智能语音交互", description: "演示了系统对自然语言的理解和响应能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 3, title: "实时路况预测", description: "通过大数据分析，准确预测前方路况", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
    frequentQuestionVideos: [
      { id: 4, title: "系统设置指南", description: "详细介绍车载系统的各项设置和个性化选项", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 5, title: "紧急救援功能", description: "演示了系统在紧急情况下的自动救援呼叫功能", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 6, title: "远程控制演示", description: "通过手机App远程控制车辆功能的操作指南", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
  },
  "娱乐域": {
    score: 4.4,
    industryAverage: 4.0,
    metrics: [
      { subject: '复杂指令识别率', value: 95, industryAverage: 88 },
      { subject: '直接指令识别率', value: 92, industryAverage: 84 },
      { subject: '主观意图识别率', value: 97, industryAverage: 90 },
      { subject: '场景意图识别率', value: 93, industryAverage: 85 },
      { subject: '跨域协作能力', value: 90, industryAverage: 82 },
      { subject: '单轮次对话记忆', value: 91, industryAverage: 83 },
    ],
    videos: [],
    highScoreVideos: [
      { id: 1, title: "精准导航", description: "展示了车载系统在复杂路况下的精准导航能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 2, title: "智能语音交互", description: "演示了系统对自然语言的理解和响应能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 3, title: "实时路况预测", description: "通过大数据分析，准确预测前方路况", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
    frequentQuestionVideos: [
      { id: 4, title: "系统设置指南", description: "详细介绍车载系统的各项设置和个性化选项", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 5, title: "紧急救援功能", description: "演示了系统在紧急情况下的自动救援呼叫功能", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 6, title: "远程控制演示", description: "通过手机App远程控制车辆功能的操作指南", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
  },
  "创作域": {
    score: 4.2,
    industryAverage: 3.8,
    metrics: [
      { subject: '复杂指令识别率', value: 93, industryAverage: 86 },
      { subject: '直接指令识别率', value: 90, industryAverage: 82 },
      { subject: '场景意图识别率', value: 95, industryAverage: 88 },
      { subject: '文本生成质量', value: 91, industryAverage: 83 },
      { subject: '图像生成质量', value: 88, industryAverage: 80 },
      { subject: '模态丰富性', value: 89, industryAverage: 81 },
      { subject: '情感响应有效性', value: 92, industryAverage: 84 },
      { subject: '情感调节能力', value: 94, industryAverage: 87 },
    ],
    videos: [],
    highScoreVideos: [
      { id: 1, title: "精准导航", description: "展示了车载系统在复杂路况下的精准导航能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 2, title: "智能语音交互", description: "演示了系统对自然语言的理解和响应能力", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 3, title: "实时路况预测", description: "通过大数据分析，准确预测前方路况", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
    frequentQuestionVideos: [
      { id: 4, title: "系统设置指南", description: "详细介绍车载系统的各项设置和个性化选项", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 5, title: "紧急救援功能", description: "演示了系统在紧急情况下的自动救援呼叫功能", thumbnail: "/placeholder.svg?height=200&width=300" },
      { id: 6, title: "远程控制演示", description: "通过手机App远程控制车辆功能的操作指南", thumbnail: "/placeholder.svg?height=200&width=300" },
    ],
  },
}

export function DomainAnalysis({ domain, showIndustryAverage, selectedBrands }: DomainAnalysisProps) {
  const [selectedDomain, setSelectedDomain] = useState(domain)
  const [selectedMetric, setSelectedMetric] = useState<string | null>(null)
  const data = domainData[selectedDomain] || { score: 0, industryAverage: 0, metrics: [], videos: [], highScoreVideos: [], frequentQuestionVideos: [] }
  const { highScoreVideos = [], frequentQuestionVideos = [] } = data

  return (
    <Card className="overflow-hidden border-2 border-primary/10 shadow-lg">
      <CardContent className="p-6">
        <div className="space-y-6">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            className="w-full bg-gradient-to-br from-primary/5 to-primary/10 rounded-lg p-4"
          >
            <Tabs value={selectedDomain} onValueChange={setSelectedDomain}>
              <TabsList className="grid grid-cols-3 lg:grid-cols-6 gap-2">
                {Object.keys(domainData).map((domainKey) => (
                  <TabsTrigger key={domainKey} value={domainKey} className="text-sm">
                    {domainKey}
                  </TabsTrigger>
                ))}
              </TabsList>
            </Tabs>
            <div className="flex items-center justify-between mt-4">
              <div>
                <h3 className="text-lg font-semibold mb-1 text-primary">{selectedDomain}总得分</h3>
                <p className="text-sm text-muted-foreground">满分 5.0</p>
              </div>
              <div className="text-right">
                <div className="text-4xl font-bold text-primary">{data.score.toFixed(1)}</div>
                {showIndustryAverage && (
                  <p className="text-sm text-muted-foreground mt-1">行业均值: {data.industryAverage.toFixed(1)}</p>
                )}
              </div>
            </div>
            
            {/* New grid layout for radar chart and test case */}
            <div className="grid grid-cols-2 gap-6 mt-6">
              {/* Radar Chart */}
              <div className="h-[400px]">
                <ResponsiveContainer width="100%" height="100%">
                  <RadarChart data={data.metrics}>
                    <PolarGrid stroke="hsl(var(--muted))" />
                    <PolarAngleAxis
                      dataKey="subject"
                      tick={({ payload, x, y, cx, cy, ...rest }) => {
                        const isSelected = payload.value === selectedMetric;
                        return (
                          <text
                            {...rest}
                            x={x}
                            y={y}
                            cx={cx}
                            cy={cy}
                            fill={isSelected ? 'hsl(var(--primary))' : 'hsl(var(--foreground))'}
                            fontSize={12}
                            fontWeight={isSelected ? 'bold' : 'normal'}
                            textAnchor="middle"
                            cursor="pointer"
                            onClick={() => setSelectedMetric(payload.value)}
                          >
                            {payload.value}
                          </text>
                        );
                      }}
                    />
                    <Radar
                      name={selectedDomain}
                      dataKey="value"
                      stroke="hsl(var(--primary))"
                      fill="hsl(var(--primary))"
                      fillOpacity={0.6}
                    />
                    {showIndustryAverage && (
                      <Radar
                        name="行业均值"
                        dataKey="industryAverage"
                        stroke="hsl(var(--muted-foreground))"
                        fill="hsl(var(--muted-foreground))"
                        fillOpacity={0.3}
                      />
                    )}
                    <Tooltip
                      contentStyle={{
                        backgroundColor: 'hsl(var(--background))',
                        border: '1px solid hsl(var(--border))',
                        borderRadius: '8px'
                      }}
                    />
                  </RadarChart>
                </ResponsiveContainer>
              </div>

              {/* Test Case Display */}
              <div className="space-y-4">
                <div className="flex items-center gap-2 text-lg font-medium">
                  <Mic className="w-5 h-5 text-primary" />
                  <span>{selectedMetric ? `${selectedMetric}测试案例` : '选择一个指标查看测试案例'}</span>
                </div>
                
                {selectedMetric && (
                  <>
                    <div className="relative aspect-[16/9] rounded-lg overflow-hidden">
                      <Image
                        src="/placeholder.svg?height=720&width=1280"
                        alt={`${selectedMetric} Test Case`}
                        fill
                        className="object-cover"
                      />
                    </div>

                    <div className="space-y-2">
                      <div className="flex items-center gap-1">
                        {[1, 2, 3, 4, 5].map((star) => (
                          <Star
                            key={star}
                            className="w-5 h-5 fill-primary text-primary"
                          />
                        ))}
                        <span className="ml-2 text-sm font-bold text-primary">5分</span>
                      </div>
                      <p className="text-sm text-muted-foreground">
                        {`这里是关于${selectedMetric}的详细说明和测试案例解析。系统在该指标上表现出色，准确理解并响应了用户需求。`}
                      </p>
                    </div>
                  </>
                )}
              </div>
            </div>
          </motion.div>
          <div className="w-full space-y-6">
            <div>
              <h3 className="text-xl font-bold text-primary mb-4">高分表现</h3>
              <ScrollArea className="w-full whitespace-nowrap">
                <div className="flex space-x-4 pb-4">
                  {highScoreVideos.map((video) => (
                    <Card key={video.id} className="w-[300px] flex-shrink-0">
                      <CardContent className="p-0">
                        <div className="aspect-video relative">
                          <Image
                            src={video.thumbnail}
                            alt={video.title}
                            layout="fill"
                            objectFit="cover"
                          />
                          <div className="absolute inset-0 bg-black bg-opacity-30 flex items-center justify-center">
                            <Button variant="secondary" size="sm" className="text-white bg-primary/80 hover:bg-primary">
                              播放视频
                            </Button>
                          </div>
                        </div>
                        <div className="p-4">
                          <h4 className="font-semibold text-base mb-2 truncate">{video.title}</h4>
                          <p className="text-sm text-muted-foreground line-clamp-2">{video.description}</p>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
                <ScrollBar orientation="horizontal" />
              </ScrollArea>
            </div>
            <div>
              <h3 className="text-xl font-bold text-primary mb-4">高频问题</h3>
              <ScrollArea className="w-full whitespace-nowrap">
                <div className="flex space-x-4 pb-4">
                  {frequentQuestionVideos.map((video) => (
                    <Card key={video.id} className="w-[300px] flex-shrink-0">
                      <CardContent className="p-0">
                        <div className="aspect-video relative">
                          <Image
                            src={video.thumbnail}
                            alt={video.title}
                            layout="fill"
                            objectFit="cover"
                          />
                          <div className="absolute inset-0 bg-black bg-opacity-30 flex items-center justify-center">
                            <Button variant="secondary" size="sm" className="text-white bg-primary/80 hover:bg-primary">
                              播放视频
                            </Button>
                          </div>
                        </div>
                        <div className="p-4">
                          <h4 className="font-semibold text-base mb-2 truncate">{video.title}</h4>
                          <p className="text-sm text-muted-foreground line-clamp-2">{video.description}</p>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
                <ScrollBar orientation="horizontal" />
              </ScrollArea>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

