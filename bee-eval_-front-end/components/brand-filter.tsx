import { Button } from "@/components/ui/button"
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area"

const brands = [
  "全部", "保时捷", "奔驰", "宝马", "奥迪", 
  "大众", "特斯拉", "蔚来", "小鹏", "理想",
  "比亚迪", "极氪", "问界", "智己", "岚图",
  "阿维塔", "高合", "飞凡", "深蓝", "零跑"
]

export function BrandFilter() {
  return (
    <ScrollArea className="w-full whitespace-nowrap">
      <div className="flex space-x-2 p-4">
        {brands.map((brand) => (
          <Button
            key={brand}
            variant="outline"
            className="flex-shrink-0"
          >
            {brand}
          </Button>
        ))}
      </div>
      <ScrollBar orientation="horizontal" />
    </ScrollArea>
  )
}

