import { Button } from "@/components/ui/button"
import { motion } from "framer-motion"

export function EnterpriseHero() {
  return (
    <section className="py-20 md:py-28">
      <div className="container px-4 md:px-6">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="text-center max-w-[800px] mx-auto"
        >
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold tracking-tighter mb-6">
            随时随地，深入调研竞品
          </h1>
          <p className="text-xl text-muted-foreground mb-4">
            多种调研分析产品，专业的对标内容，实时跟踪竞品版本迭代。
          </p>
          <p className="text-xl text-muted-foreground mb-8">
            低成本满足一整年竞品对标的全部需求，使每一个产品决策都有着力点，永远保持市场前沿。
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Button size="lg" className="bg-primary text-primary-foreground">
              获取报价
            </Button>
            <Button size="lg" variant="outline">
              如何向公司申请购买BeeEval?
            </Button>
          </div>
        </motion.div>
      </div>
    </section>
  )
}

