import Link from 'next/link'
import { Button } from "@/components/ui/button"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"

export function Navigation() {
  return (
    <nav className="fixed top-0 left-0 h-screen w-64 bg-white border-r border-gray-200 p-4 flex flex-col">
      <div className="flex items-center space-x-2 mb-6">
        <Avatar>
          <AvatarImage src="/placeholder.svg?height=40&width=40" alt="用户头像" />
          <AvatarFallback>用户</AvatarFallback>
        </Avatar>
        <div>
          <div className="font-semibold">张三</div>
          <div className="text-sm text-muted-foreground">VIP 会员</div>
        </div>
      </div>
      <div className="space-y-4 flex-grow">
        {['概览', '我的车辆', '消息中心', '设置', '帮助支持'].map((item) => (
          <Link
            key={item}
            href="#"
            className="block px-4 py-2 text-sm text-gray-600 hover:bg-gray-50 rounded-md"
          >
            {item}
          </Link>
        ))}
      </div>
      <Button variant="outline" className="w-full">退出登录</Button>
    </nav>
  )
}

