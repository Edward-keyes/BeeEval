import { Header as MainHeader } from '@/components/header'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { ChevronDown } from 'lucide-react'

export function Header() {
  return (
    <>
      <MainHeader activeItem="llm-analytics" />
      <div className="border-b">
        <div className="container mx-auto flex items-center gap-3 h-12 px-4">
          <div className="relative max-w-md flex-1">
            <Input 
              type="search" 
              placeholder="搜索车型、功能、服务..." 
              className="w-full h-8"
            />
            <Button 
              size="sm"
              className="absolute right-0 top-0 h-8 px-3 bg-primary hover:bg-primary/90 text-primary-foreground rounded-l-none"
            >
              搜索
            </Button>
          </div>
          <Button 
            variant="outline" 
            size="sm"
            className="flex items-center gap-1 h-8 px-3"
          >
            全部车型 <ChevronDown className="h-4 w-4" />
          </Button>
        </div>
      </div>
    </>
  )
}

