'use client'

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { FileText, Download, Eye } from "lucide-react"
import { Badge } from "@/components/ui/badge"

const reports = [
  {
    id: 1,
    title: '小米 SU7 智能座舱评测报告',
    date: '2024-03-20',
    status: '已完成',
    score: 92.5,
    type: '完整评测'
  },
  {
    id: 2,
    title: '理想 L7 智能座舱评测报告',
    date: '2024-03-15',
    status: '已完成',
    score: 89.5,
    type: '完整评测'
  },
  {
    id: 3,
    title: '问界 M7 智能座舱评测报告',
    date: '2024-03-10',
    status: '处理中',
    type: '快速评测'
  }
]

export default function ReportsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-lg font-medium">我的报告</h3>
        <p className="text-sm text-muted-foreground">
          查看和下载您的评测报告
        </p>
      </div>
      <Separator />
      
      <div className="grid gap-4">
        {reports.map((report) => (
          <Card key={report.id}>
            <CardHeader className="flex flex-row items-center gap-4 space-y-0 pb-2">
              <div className="bg-primary/10 p-2 rounded-full">
                <FileText className="h-4 w-4 text-primary" />
              </div>
              <div className="flex-1">
                <div className="flex items-center justify-between">
                  <CardTitle className="text-base">{report.title}</CardTitle>
                  <Badge 
                    variant={report.status === '已完成' ? 'default' : 'secondary'}
                    className="ml-2"
                  >
                    {report.status}
                  </Badge>
                </div>
                <CardDescription className="mt-1">
                  <div className="flex items-center gap-4 text-xs">
                    <span>{report.date}</span>
                    <span>{report.type}</span>
                    {report.score && <span>评分：{report.score}</span>}
                  </div>
                </CardDescription>
              </div>
            </CardHeader>
            <CardContent>
              <div className="flex items-center justify-end gap-2">
                {report.status === '已完成' && (
                  <>
                    <Button variant="outline" size="sm" className="gap-2">
                      <Eye className="h-4 w-4" />
                      查看
                    </Button>
                    <Button variant="outline" size="sm" className="gap-2">
                      <Download className="h-4 w-4" />
                      下载
                    </Button>
                  </>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
} 