'use client'

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Bell, Mail, MessageSquare } from "lucide-react"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"

const notifications = [
  {
    id: 1,
    type: 'system',
    title: '系统更新提醒',
    content: '您的账号有新的系统更新待处理，请及时查看。',
    date: '2024-03-20 10:30',
    isRead: false
  },
  {
    id: 2,
    type: 'report',
    title: '新报告生成通知',
    content: '您的最新评测报告已生成，点击查看详情。',
    date: '2024-03-19 15:45',
    isRead: false
  },
  {
    id: 3,
    type: 'message',
    title: '新消息提醒',
    content: '您有一条新的消息待查看。',
    date: '2024-03-18 09:15',
    isRead: false
  }
]

export default function NotificationsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-lg font-medium">消息通知</h3>
        <p className="text-sm text-muted-foreground">
          查看您的系统通知、报告更新和消息提醒
        </p>
      </div>
      <Separator />
      
      <Tabs defaultValue="all" className="w-full">
        <TabsList>
          <TabsTrigger value="all">全部通知</TabsTrigger>
          <TabsTrigger value="system">系统通知</TabsTrigger>
          <TabsTrigger value="report">报告通知</TabsTrigger>
          <TabsTrigger value="message">消息提醒</TabsTrigger>
        </TabsList>

        <TabsContent value="all" className="space-y-4 mt-4">
          {notifications.map((notification) => (
            <Card key={notification.id}>
              <CardHeader className="flex flex-row items-center gap-4 space-y-0 pb-2">
                <div className="bg-primary/10 p-2 rounded-full">
                  {notification.type === 'system' && <Bell className="h-4 w-4 text-primary" />}
                  {notification.type === 'report' && <Mail className="h-4 w-4 text-primary" />}
                  {notification.type === 'message' && <MessageSquare className="h-4 w-4 text-primary" />}
                </div>
                <div className="flex-1">
                  <div className="flex items-center justify-between">
                    <CardTitle className="text-base">{notification.title}</CardTitle>
                    {!notification.isRead && (
                      <Badge variant="secondary" className="bg-primary text-primary-foreground">
                        新
                      </Badge>
                    )}
                  </div>
                  <CardDescription className="mt-1 text-xs">
                    {notification.date}
                  </CardDescription>
                </div>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-muted-foreground">{notification.content}</p>
              </CardContent>
            </Card>
          ))}
        </TabsContent>

        <TabsContent value="system" className="space-y-4 mt-4">
          {notifications.filter(n => n.type === 'system').map((notification) => (
            <Card key={notification.id}>
              <CardHeader className="flex flex-row items-center gap-4 space-y-0 pb-2">
                <div className="bg-primary/10 p-2 rounded-full">
                  <Bell className="h-4 w-4 text-primary" />
                </div>
                <div className="flex-1">
                  <div className="flex items-center justify-between">
                    <CardTitle className="text-base">{notification.title}</CardTitle>
                    {!notification.isRead && (
                      <Badge variant="secondary" className="bg-primary text-primary-foreground">
                        新
                      </Badge>
                    )}
                  </div>
                  <CardDescription className="mt-1 text-xs">
                    {notification.date}
                  </CardDescription>
                </div>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-muted-foreground">{notification.content}</p>
              </CardContent>
            </Card>
          ))}
        </TabsContent>

        <TabsContent value="report" className="space-y-4 mt-4">
          {notifications.filter(n => n.type === 'report').map((notification) => (
            <Card key={notification.id}>
              <CardHeader className="flex flex-row items-center gap-4 space-y-0 pb-2">
                <div className="bg-primary/10 p-2 rounded-full">
                  <Mail className="h-4 w-4 text-primary" />
                </div>
                <div className="flex-1">
                  <div className="flex items-center justify-between">
                    <CardTitle className="text-base">{notification.title}</CardTitle>
                    {!notification.isRead && (
                      <Badge variant="secondary" className="bg-primary text-primary-foreground">
                        新
                      </Badge>
                    )}
                  </div>
                  <CardDescription className="mt-1 text-xs">
                    {notification.date}
                  </CardDescription>
                </div>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-muted-foreground">{notification.content}</p>
              </CardContent>
            </Card>
          ))}
        </TabsContent>

        <TabsContent value="message" className="space-y-4 mt-4">
          {notifications.filter(n => n.type === 'message').map((notification) => (
            <Card key={notification.id}>
              <CardHeader className="flex flex-row items-center gap-4 space-y-0 pb-2">
                <div className="bg-primary/10 p-2 rounded-full">
                  <MessageSquare className="h-4 w-4 text-primary" />
                </div>
                <div className="flex-1">
                  <div className="flex items-center justify-between">
                    <CardTitle className="text-base">{notification.title}</CardTitle>
                    {!notification.isRead && (
                      <Badge variant="secondary" className="bg-primary text-primary-foreground">
                        新
                      </Badge>
                    )}
                  </div>
                  <CardDescription className="mt-1 text-xs">
                    {notification.date}
                  </CardDescription>
                </div>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-muted-foreground">{notification.content}</p>
              </CardContent>
            </Card>
          ))}
        </TabsContent>
      </Tabs>
    </div>
  )
} 