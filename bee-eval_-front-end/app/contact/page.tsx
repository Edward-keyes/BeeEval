'use client'

import { useState } from 'react'
import { PageLayout } from '@/components/layouts/page-layout'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Mail, MapPin, MessageSquare } from 'lucide-react'
import { motion } from 'framer-motion'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { toast } from 'react-hot-toast'
import { useLanguage } from "@/constants/language"
import { translations } from "@/language"
import Image from 'next/image'

// 定义表单数据的类型
type FormData = {
    name: string;
    email: string;
    message: string;
};

export default function Contact() {
    const { language } = useLanguage()
    const trans = translations[language] as any;

    // 根据当前语言设置表单验证信息
    const getFormSchema = () => {
        return z.object({
            name: z.string().min(2, {
                message: language === 'en'
                    ? 'Name must be at least 2 characters'
                    : '姓名至少需要2个字符'
            }),
            email: z.string().email({
                message: language === 'en'
                    ? 'Please enter a valid email address'
                    : '请输入有效的邮箱地址'
            }),
            message: z.string().min(10, {
                message: language === 'en'
                    ? 'Message must be at least 10 characters'
                    : '消息至少需要10个字符'
            }),
        });
    };

    const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
        resolver: zodResolver(getFormSchema()),
    });

    const onSubmit = (data: FormData) => {
        toast.success(
            language === 'en'
                ? 'Message sent! We will reply to you as soon as possible.'
                : '消息已发送！我们会尽快回复您。'
        );
    };

    return (
        <PageLayout activeItem="contact" darkMode>
            <div className="container mx-auto px-4 py-24">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                    className="text-center mb-20"
                >
                    <h2 className="text-lg font-medium text-amber-500 mb-6 tracking-wide">{trans.contactUs}</h2>
                    <h1 className="text-4xl md:text-6xl font-bold tracking-tight max-w-4xl mx-auto text-white">
                        {trans.provideProfessionalSupport}
                    </h1>
                </motion.div>

                <div className="relative">
                    {/* Background Gradient Effects */}
                    <div className="absolute top-0 left-1/4 w-1/2 h-1/2 bg-amber-500/10 blur-[120px] rounded-full"></div>
                    <div className="absolute bottom-0 right-1/4 w-1/2 h-1/2 bg-amber-500/5 blur-[120px] rounded-full"></div>

                    <div className="relative grid md:grid-cols-2 gap-16">
                        <motion.div
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            transition={{ duration: 0.5 }}
                        >
                            <Card className="h-full bg-[#0D1117] border-gray-800/50">
                                <CardHeader>
                                    <CardTitle className="text-xl font-bold bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">
                                        {trans.sendMessage}
                                    </CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                                        <div>
                                            <Input
                                                placeholder={trans.yourName}
                                                {...register('name')}
                                                className="bg-black/50 border-gray-800 text-white placeholder:text-gray-500"
                                            />
                                            {errors.name && <p className="text-amber-500 text-sm mt-1">{errors.name.message as string}</p>}
                                        </div>
                                        <div>
                                            <Input
                                                type="email"
                                                placeholder={trans.yourEmail}
                                                {...register('email')}
                                                className="bg-black/50 border-gray-800 text-white placeholder:text-gray-500"
                                            />
                                            {errors.email && <p className="text-amber-500 text-sm mt-1">{errors.email.message as string}</p>}
                                        </div>
                                        <div>
                                            <Textarea
                                                placeholder={trans.yourMessage}
                                                rows={5}
                                                {...register('message')}
                                                className="bg-black/50 border-gray-800 text-white placeholder:text-gray-500"
                                            />
                                            {errors.message && <p className="text-amber-500 text-sm mt-1">{errors.message.message as string}</p>}
                                        </div>
                                        <Button
                                            type="submit"
                                            className="w-full bg-gradient-to-r from-amber-500 to-amber-600 hover:from-amber-600 hover:to-amber-700 text-black font-semibold"
                                        >
                                            {trans.send}
                                        </Button>
                                    </form>
                                </CardContent>
                            </Card>
                        </motion.div>

                        <div className="space-y-8">
                            {[
                                { icon: Mail, title: trans.email, content: 'beeeval@xailab.cn' },
                                {
                                    icon: MessageSquare,
                                    title: trans.officialAccounts,
                                    content: (
                                        <div className="group relative cursor-pointer">
                                            <span className="text-gray-300 text-lg">
                                                {language === 'en' ? 'Scan code to add or follow' : '扫码添加或关注'}
                                            </span>
                                            <div className="absolute -top-[180px] left-1/2 -translate-x-1/2 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                                                <div className="relative bg-white p-8 rounded-xl shadow-2xl min-w-[500px]">
                                                    <div className="flex gap-8">
                                                        <div className="text-center">
                                                            <Image
                                                                src="/images/WechatIMG68.jpg"
                                                                alt={language === 'en' ? 'WeChat ID' : '微信号'}
                                                                width={160}
                                                                height={160}
                                                                className="rounded-lg mb-2"
                                                            />
                                                            <p className="text-sm font-medium text-gray-500">
                                                                {language === 'en' ? 'WeChat ID' : '微信号'}
                                                            </p>
                                                        </div>
                                                        <div className="text-center">
                                                            <Image
                                                                src="/images/WechatIMG69.jpg"
                                                                alt={language === 'en' ? 'Bee Eval Official Account' : 'Bee Eval 公众号'}
                                                                width={160}
                                                                height={160}
                                                                className="rounded-lg mb-2"
                                                            />
                                                            <p className="text-sm font-medium text-gray-500">
                                                                {language === 'en' ? 'Bee Eval Official Account' : 'Bee Eval 公众号'}
                                                            </p>
                                                        </div>
                                                        <div className="text-center">
                                                            <Image
                                                                src="/images/XAI LAB公众号二维码.jpg"
                                                                alt={language === 'en' ? 'XAI Lab Official Account' : 'XAI Lab 公众号'}
                                                                width={160}
                                                                height={160}
                                                                className="rounded-lg mb-2"
                                                            />
                                                            <p className="text-sm font-medium text-gray-500">
                                                                {language === 'en' ? 'XAI Lab Official Account' : 'XAI Lab 公众号'}
                                                            </p>
                                                        </div>
                                                    </div>
                                                    <div className="absolute -bottom-2 left-1/2 -translate-x-1/2 w-4 h-4 bg-white rotate-45"></div>
                                                </div>
                                            </div>
                                        </div>
                                    )
                                },
                                {
                                    icon: MapPin,
                                    title: trans.address,
                                    content: language === 'en'
                                        ? 'No. 39, Lane 1028, Siping Road, Yangpu District, Shanghai, 102'
                                        : '上海市杨浦区四平路1028弄39号102'
                                }
                            ].map((item, index) => (
                                <motion.div
                                    key={item.title}
                                    initial={{ opacity: 0, x: 20 }}
                                    animate={{ opacity: 1, x: 0 }}
                                    transition={{ duration: 0.5, delay: index * 0.1 }}
                                >
                                    <Card className="bg-[#0D1117] border-gray-800/50 hover:border-amber-500 transition-all duration-300">
                                        <CardHeader>
                                            <CardTitle className="flex items-center gap-3 text-xl">
                                                <div className="w-10 h-10 rounded-lg bg-gradient-to-br from-amber-500/20 to-amber-500/5 flex items-center justify-center">
                                                    <item.icon className="w-5 h-5 text-amber-500" />
                                                </div>
                                                <span className="bg-gradient-to-r from-amber-200 to-amber-500 bg-clip-text text-transparent">
                                                    {item.title}
                                                </span>
                                            </CardTitle>
                                        </CardHeader>
                                        <CardContent>
                                            <p className="text-gray-300 text-lg">{item.content}</p>
                                        </CardContent>
                                    </Card>
                                </motion.div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </PageLayout>
    )
}

