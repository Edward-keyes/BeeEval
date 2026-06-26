import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion"
import { motion } from "framer-motion"

const faqs = [
  {
    question: "如何支付？是否能开具发票？",
    answer: "目前购买企业会员都需要与企业签订服务合同，通过公对公方式完成付款。我们提供普通电子发票或增值税专用发票，付款成功后三个工作日开具并发出。"
  },
  {
    question: "企业会员有效期多长时间？",
    answer: "除以内容体验为主的季度会员外（3个月有效期），其他均为年会员，从开通企业账号之日起一个合同年内有效。在有效期内，所有权限内的内容及产品无限次使用。"
  },
  {
    question: "不同企业会员之间区别是什么？",
    answer: "不同的企业会员主要针对不同的团队规模，也就是一个账号的同时登录人数不同，超出最大登录人数会导致最早登录的设备下线。另外一个区别就是不同的企业会员的权限不同，即网站中平台的部分内容只对具体的会员开放。"
  },
  {
    question: "不同企业会员之间可以升级么？",
    answer: "除季度会员外，年会员可以从低级别升级为高级别会员，按照会员之间差价补齐，有效期延续之前会员有效期。"
  },
  {
    question: "竞品分析视频等相关平台内容能否下载？",
    answer: "除明确提供下载入口或下载链接的内容外，竞品分析视频等均不允许使用非法手段下载到本地，仅可以在网站平台、APP等渠道查看。"
  },
  {
    question: "新车型选择和上线是如何安排的？",
    answer: "我们会根据市场关注度选择最热门的车型进行拍摄和分析，也会根据客户的需求选择目标车型，持续一年不间断更新，保证每周两周上线一款新车或更新一个新版本。"
  }
]

export function EnterpriseFAQ() {
  return (
    <section className="py-20 bg-muted/50">
      <div className="container px-4 md:px-6">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-center mb-12"
        >
          <h2 className="text-3xl font-bold tracking-tighter mb-4">
            常见问题
          </h2>
        </motion.div>
        <motion.div
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 1 }}
          viewport={{ once: true }}
          className="max-w-3xl mx-auto"
        >
          <Accordion type="single" collapsible>
            {faqs.map((faq, index) => (
              <AccordionItem key={index} value={`item-${index}`}>
                <AccordionTrigger>{faq.question}</AccordionTrigger>
                <AccordionContent>{faq.answer}</AccordionContent>
              </AccordionItem>
            ))}
          </Accordion>
        </motion.div>
      </div>
    </section>
  )
}

