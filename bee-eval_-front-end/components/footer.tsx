'use client'

import { motion } from 'framer-motion'
import { Mail, QrCode, HelpCircle, MessageSquare } from 'lucide-react'
import Image from 'next/image'
import Link from 'next/link'
import {
    HoverCard,
    HoverCardContent,
    HoverCardTrigger,
} from "@/components/ui/hover-card"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { useState } from 'react'
import { useToast } from "@/components/ui/use-toast"
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'
import http from '@/utils/request'

const contactInfo = {
    email: "beeeval@xailab.cn",
    qrCodeUrl: "/images/qrcode.png",
    wechatQrCodeUrl: "/images/wechat-qrcode.png"
}

const feedbackContent = {
    title: "发送消息",
    description: "如果您在使用过程中遇到任何问题，或有任何建议，欢迎给我们发送消息",
}

export function Footer() {
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const { language } = useLanguage()
    const { toast } = useToast()
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        message: ''
    })

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            // 调用接口
            const response = await http.post('/vehiclesuggestion/save', {
                userName: formData.name,
                userMail: formData.email,
                userMessage: formData.message
            });

            // 如果成功
            if (response.code === 0) {
                toast({
                    title: "发送成功",
                    description: "感谢您的反馈，我们会尽快处理！",
                    variant: "default",
                    className: "bg-[#1C2028] border-gray-800 text-white border-l-4 border-l-green-500",
                    duration: 3000,
                });
                // 清空表单
                setFormData({
                    name: '',
                    email: '',
                    message: ''
                });
                // 关闭对话框
                setIsDialogOpen(false);
            } else {
                toast({
                    title: "发送失败",
                    description: response.msg || "请稍后重试",
                    variant: "destructive",
                    className: "bg-[#1C2028] border-gray-800 text-white border-l-4 border-l-red-500",
                    duration: 3000,
                });
            }
        } catch (error) {
            // console.error('提交反馈失败:', error);
            toast({
                title: "发送失败",
                description: "请稍后重试",
                variant: "destructive",
                className: "bg-[#1C2028] border-gray-800 text-white",
                duration: 3000,
            });
        }
    };

    return (
        <footer className="bg-[#0D1117] border-t border-gray-800/50">
            <div className="max-w-screen-2xl mx-auto px-4 py-20">
                <div className="grid grid-cols-1 md:grid-cols-12 gap-12 lg:gap-16 items-start">
                    {/* 品牌信息 */}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        className="md:col-span-3 space-y-8"
                    >
                        <div className="flex items-center gap-5">
                            <Image
                                src="/bee-logo.png"
                                alt="BeeEval Logo"
                                width={180}
                                height={60}
                                className="object-contain"
                                priority
                                quality={100}
                            />
                        </div>
                        <p className="text-gray-400 leading-relaxed text-base max-w-xl">
                            {translations[language].professionalEvaluation}
                        </p>
                    </motion.div>

                    {/* 技术支持 */}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        transition={{ delay: 0.1 }}
                        className="md:col-span-3 space-y-8"
                    >
                        <h3 className="text-xl font-bold text-white">{translations[language].techSupport}</h3>
                        <div className="space-y-6">
                            <Link
                                href="/faq"
                                className="flex items-center gap-5 text-gray-400 hover:text-amber-500 transition-colors group cursor-pointer"
                            >
                                <div className="w-14 h-14 rounded-xl bg-gray-800/50 flex items-center justify-center group-hover:bg-amber-500/10">
                                    <HelpCircle className="h-7 w-7 group-hover:text-amber-500" />
                                </div>
                                <div>
                                    <p className="text-sm text-gray-500 mb-1.5">{translations[language].faq}</p>
                                    <p className="text-white group-hover:text-amber-500 font-medium">{translations[language].viewHelpDoc}</p>
                                </div>
                            </Link>

                            <div
                                className="flex items-center gap-5 text-gray-400 hover:text-amber-500 transition-colors group cursor-pointer"
                                onClick={() => setIsDialogOpen(true)}
                            >
                                <div className="w-14 h-14 rounded-xl bg-gray-800/50 flex items-center justify-center group-hover:bg-amber-500/10">
                                    <MessageSquare className="h-7 w-7 group-hover:text-amber-500" />
                                </div>
                                <div>
                                    <p className="text-sm text-gray-500 mb-1.5">{translations[language].problemFeedback}</p>
                                    <p className="text-white group-hover:text-amber-500 font-medium">{translations[language].submitFeedback}</p>
                                </div>
                            </div>
                        </div>
                    </motion.div>

                    {/* 联系方式 */}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        transition={{ delay: 0.2 }}
                        className="md:col-span-3 space-y-8"
                    >
                        <h3 className="text-xl font-bold text-white">{translations[language].contactUs}</h3>
                        <div className="space-y-6">
                            <a
                                href={`mailto:${contactInfo.email}`}
                                className="flex items-center gap-5 text-gray-400 hover:text-amber-500 transition-colors group"
                            >
                                <div className="w-14 h-14 rounded-xl bg-gray-800/50 flex items-center justify-center group-hover:bg-amber-500/10">
                                    <Mail className="h-7 w-7 group-hover:text-amber-500" />
                                </div>
                                <div>
                                    <p className="text-sm text-gray-500 mb-1.5">{translations[language].businessCooperation}</p>
                                    <p className="text-white group-hover:text-amber-500 font-medium">{contactInfo.email}</p>
                                </div>
                            </a>

                            <HoverCard openDelay={0} closeDelay={0}>
                                <HoverCardTrigger asChild>
                                    <div className="flex items-center gap-5 text-gray-400 hover:text-amber-500 transition-colors group">
                                        <div className="w-14 h-14 rounded-xl bg-gray-800/50 flex items-center justify-center group-hover:bg-amber-500/10">
                                            <QrCode className="h-7 w-7 group-hover:text-amber-500" />
                                        </div>
                                        <div className="flex-1">
                                            <p className="text-sm text-gray-500 mb-1.5">{translations[language].wechatAndPublicAccount}</p>
                                            <p className="text-white group-hover:text-amber-500 font-medium text-left">{translations[language].scanToAddOrFollow}</p>
                                        </div>
                                    </div>
                                </HoverCardTrigger>
                                <HoverCardContent
                                    className="w-auto p-6 bg-white rounded-xl border-none shadow-2xl"
                                    side="top"
                                    align="start"
                                    sideOffset={5}
                                >
                                    <div className="flex gap-6">
                                        <div className="text-center">
                                            <Image
                                                src="/images/WechatIMG68.jpg"
                                                alt="微信二维码"
                                                width={140}
                                                height={140}
                                                className="rounded-lg mb-2"
                                            />
                                            <p className="text-sm font-medium text-gray-600">微信号</p>
                                        </div>
                                        <div className="text-center">
                                            <Image
                                                src="/images/WechatIMG69.jpg"
                                                alt="微信公众号二维码"
                                                width={140}
                                                height={140}
                                                className="rounded-lg mb-2"
                                            />
                                            <p className="text-sm font-medium text-gray-600">Bee Eval 公众号</p>
                                        </div>
                                        <div className="text-center">
                                            <Image
                                                src="/images/XAI LAB公众号二维码.jpg"
                                                alt="微信公众号二维码"
                                                width={140}
                                                height={140}
                                                className="rounded-lg mb-2"
                                            />
                                            <p className="text-sm font-medium text-gray-600">XAI Lab 公众号</p>
                                        </div>
                                    </div>
                                </HoverCardContent>
                            </HoverCard>
                        </div>
                    </motion.div>

                    {/* 合作平台 */}
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        transition={{ delay: 0.3 }}
                        className="md:col-span-3 space-y-8"
                    >
                        <h3 className="text-xl font-bold text-white">合作平台</h3>
                        <div className="space-y-6">
                            <a
                                href="https://www.i4research.com/"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-gray-400 hover:text-amber-500 transition-colors flex items-center gap-2"
                            >
                                <div className="w-14 h-14 rounded-xl bg-gray-800/50 flex items-center justify-center group-hover:bg-amber-500/10">

                                    <Image
                                        src="/Autolab.png"
                                        alt="i4research icon"
                                        width={35}
                                        height={35}
                                        className="object-contain"
                                    />
                                </div>
                                {/* <p style={{ marginLeft: '10px', fontSize: '18px', color: '#fff' }}>智能驾驶测评</p> */}
                                <div className="flex-1" style={{ marginLeft: '10px' }}>
                                    <p className="text-sm text-gray-500 mb-1.5">{translations[language].i4research}</p>
                                    <p className="text-white group-hover:text-amber-500 font-medium text-left">{translations[language].i4researchDescription}</p>
                                </div>
                            </a>
                        </div>
                    </motion.div>


                </div>

                {/* 底部版权信息 */}
                <motion.div
                    initial={{ opacity: 0 }}
                    whileInView={{ opacity: 1 }}
                    viewport={{ once: true }}
                    transition={{ delay: 0.3 }}
                    className="mt-20 pt-10 border-t border-gray-800/50"
                >
                    <div className="flex flex-col items-center gap-2">
                        <p className="text-sm text-center text-gray-500">
                            {translations[language].copyright}
                        </p>
                        <a
                            href="https://beian.miit.gov.cn/"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-sm text-gray-500 hover:text-amber-500 transition-colors"
                        >
                            沪ICP备20022324号-3
                        </a>
                    </div>
                </motion.div>
            </div>

            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <DialogContent className="bg-[#1C2028] border-gray-800 sm:max-w-[425px]">
                    <DialogHeader>
                        <DialogTitle className="text-2xl font-semibold text-amber-500">
                            {feedbackContent.title}
                        </DialogTitle>
                        <DialogDescription className="text-gray-400">
                            {feedbackContent.description}
                        </DialogDescription>
                    </DialogHeader>
                    <form onSubmit={handleSubmit} className="space-y-6 py-4">
                        <div className="space-y-4">
                            <div className="space-y-2">
                                <Input
                                    placeholder="您的姓名"
                                    className="bg-black/20 border-gray-800 text-white placeholder:text-gray-500"
                                    value={formData.name}
                                    onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                                />
                            </div>
                            <div className="space-y-2">
                                <Input
                                    type="email"
                                    placeholder="您的邮箱"
                                    className="bg-black/20 border-gray-800 text-white placeholder:text-gray-500"
                                    value={formData.email}
                                    onChange={(e) => setFormData(prev => ({ ...prev, email: e.target.value }))}
                                />
                            </div>
                            <div className="space-y-2">
                                <Textarea
                                    placeholder="您的消息"
                                    className="bg-black/20 border-gray-800 text-white placeholder:text-gray-500 min-h-[120px]"
                                    value={formData.message}
                                    onChange={(e) => setFormData(prev => ({ ...prev, message: e.target.value }))}
                                />
                            </div>
                        </div>
                        <Button
                            type="submit"
                            className="w-full bg-gradient-to-r from-amber-500 to-amber-600 hover:from-amber-600 hover:to-amber-700 text-black font-semibold"
                        >
                            发送
                        </Button>
                    </form>
                </DialogContent>
            </Dialog>
        </footer>
    )
}

