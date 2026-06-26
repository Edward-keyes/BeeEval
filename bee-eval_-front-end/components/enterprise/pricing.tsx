import { Check } from 'lucide-react'
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { motion } from "framer-motion"

const tiers = [
  {
    name: "V1 年度企业会员",
    subtitle: "视频略享",
    teamSize: "6人",
    features: [
      "全车型功能视频",
      "全OTA版本视频",
      "OTA 更新日志",
      "竞品功能推荐",
      "竞品功能&设计分析",
      "手机App分析"
    ]
  },
  {
    name: "V2 年度企业会员",
    subtitle: "初创团队",
    teamSize: "10人",
    features: [
      "全车型功能视频",
      "全OTA版本视频",
      "OTA 更新日志",
      "竞品功能推荐",
      "竞品功能&设计分析",
      "手机App分析",
      "虚拟镜像"
    ]
  },
  {
    name: "V3 年度企业会员",
    subtitle: "小型团队",
    teamSize: "40人",
    features: [
      "全车型功能视频",
      "全OTA版本视频",
      "OTA 更新日志",
      "竞品功能推荐",
      "竞品功能&设计分析",
      "手机App分析",
      "虚拟镜像",
      "全功能地图",
      "车型报告",
      "HMI 设计规范"
    ]
  },
  {
    name: "V4 年度企业会员",
    subtitle: "中型团队",
    teamSize: "80人",
    features: [
      "全车型功能视频",
      "全OTA版本视频",
      "OTA 更新日志",
      "竞品功能推荐",
      "竞品功能&设计分析",
      "手机App分析",
      "虚拟镜像",
      "全功能地图",
      "车型报告",
      "HMI 设计规范",
      "智能功能清单"
    ]
  },
  {
    name: "V5 年度企业会员",
    subtitle: "大型企业",
    teamSize: "无限制",
    features: [
      "全车型功能视频",
      "全OTA版本视频",
      "OTA 更新日志",
      "竞品功能推荐",
      "竞品功能&设计分析",
      "手机App分析",
      "虚拟镜像",
      "全功能地图",
      "车型报告",
      "HMI 设计规范",
      "智能功能清单",
      "全场景多视角视频库",
      "HMI 交互文档"
    ]
  }
]

export function EnterprisePricing() {
  return (
    <section className="py-20 bg-muted/50">
      <div className="container px-4 md:px-6">
        <div className="grid md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6">
          {tiers.map((tier, index) => (
            <motion.div
              key={tier.name}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
            >
              <Card className="h-full">
                <CardHeader className="pb-4">
                  <Badge variant="outline" className="w-fit mb-2">
                    {tier.subtitle}
                  </Badge>
                  <h3 className="text-lg font-bold">{tier.name}</h3>
                  <div className="text-sm text-muted-foreground">团队人数：{tier.teamSize}</div>
                </CardHeader>
                <CardContent>
                  <ul className="space-y-2 text-sm">
                    {tier.features.map((feature) => (
                      <li key={feature} className="flex items-center">
                        <Check className="h-4 w-4 text-primary mr-2" />
                        {feature}
                      </li>
                    ))}
                  </ul>
                </CardContent>
              </Card>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  )
}

