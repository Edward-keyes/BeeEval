'use client'

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Separator } from "@/components/ui/separator"
import { Camera, Mail, Phone } from "lucide-react"

export default function AccountPage() {
  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-lg font-medium">个人资料</h3>
        <p className="text-sm text-muted-foreground">
          管理您的个人信息和偏好设置
        </p>
      </div>
      <Separator />
      <div className="flex flex-col gap-8">
        <div className="flex items-start gap-6">
          <div className="relative">
            <Avatar className="h-24 w-24">
              <AvatarImage src="/placeholder.svg?height=96&width=96&text=张" alt="张三" />
              <AvatarFallback>张</AvatarFallback>
            </Avatar>
            <Button 
              size="icon" 
              variant="outline"
              className="absolute -bottom-2 -right-2 h-8 w-8 rounded-full bg-background"
            >
              <Camera className="h-4 w-4" />
            </Button>
          </div>
          <div className="space-y-1">
            <h4 className="text-base font-medium">头像</h4>
            <p className="text-sm text-muted-foreground">
              支持 JPG、PNG 格式，文件大小不超过 2MB
            </p>
          </div>
        </div>

        <div className="grid gap-6">
          <div className="grid gap-2">
            <Label htmlFor="name">姓名</Label>
            <Input id="name" defaultValue="张三" />
          </div>

          <div className="grid gap-2">
            <Label htmlFor="email">邮箱</Label>
            <div className="flex items-center gap-2">
              <Input id="email" type="email" defaultValue="zhangsan@example.com" />
              <Button variant="outline" size="icon">
                <Mail className="h-4 w-4" />
              </Button>
            </div>
            <p className="text-xs text-muted-foreground">用于接收重要通知和报告</p>
          </div>

          <div className="grid gap-2">
            <Label htmlFor="phone">手机号码</Label>
            <div className="flex items-center gap-2">
              <Input id="phone" type="tel" defaultValue="13800138000" />
              <Button variant="outline" size="icon">
                <Phone className="h-4 w-4" />
              </Button>
            </div>
            <p className="text-xs text-muted-foreground">用于账号安全验证</p>
          </div>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>账号安全</CardTitle>
            <CardDescription>管理您的密码和安全设置</CardDescription>
          </CardHeader>
          <CardContent className="grid gap-4">
            <div className="flex items-center justify-between">
              <div>
                <div className="font-medium">修改密码</div>
                <div className="text-sm text-muted-foreground">上次修改时间：2024-03-15</div>
              </div>
              <Button variant="outline">修改</Button>
            </div>
            <Separator />
            <div className="flex items-center justify-between">
              <div>
                <div className="font-medium">双重认证</div>
                <div className="text-sm text-muted-foreground">使用手机验证码进行双重认证</div>
              </div>
              <Button variant="outline">设置</Button>
            </div>
          </CardContent>
        </Card>

        <div className="flex justify-end gap-4">
          <Button variant="outline">取消</Button>
          <Button>保存更改</Button>
        </div>
      </div>
    </div>
  )
} 