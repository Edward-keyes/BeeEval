import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { Search } from "lucide-react"

export function VehicleDetailSearchBar() {
  return (
    <div className="border-b border-gray-800 bg-[#0F0F0F]">
      <div className="container mx-auto px-4 md:px-6 py-4">
        <div className="flex gap-4">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-500" />
            <Input
              type="text"
              placeholder="搜索车型、功能、技能..."
              className="pl-10 bg-[#1C2028] border-gray-800 text-gray-400 placeholder:text-gray-500"
            />
          </div>
          <Button variant="outline" className="border-gray-800 text-gray-400 hover:text-white hover:border-gray-700">
            搜索
          </Button>
        </div>
      </div>
    </div>
  )
}

