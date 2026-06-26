'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { toast } from 'sonner'
import { motion } from 'framer-motion'
import Link from 'next/link'
import Image from 'next/image'
import http from '@/utils/request'
import { useAuthStore } from '@/store/useAuthStore'
import { useLanguage } from '@/constants/language'
import { translations } from '@/language'

interface LoginResponse {
    code: number
    msg: string
    data: {
        status: string
    }
}

export default function LoginPage() {
    const router = useRouter()
    const setAuth = useAuthStore((state) => state.setAuth)
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [phone, setPhone] = useState('')
    const [verificationCode, setVerificationCode] = useState('')
    const [activeTab, setActiveTab] = useState('password')
    const [emailError, setEmailError] = useState(false)
    const [passwordError, setPasswordError] = useState(false)
    const [emailErrorMsg, setEmailErrorMsg] = useState('')
    const [passwordErrorMsg, setPasswordErrorMsg] = useState('')

    // 添加语言支持
    const { language } = useLanguage()
    const trans = translations[language] as any

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        try {
            const res = await http.post<LoginResponse>('/vehicleuser/doLogin', {
                email,
                password
            })

            // Reset error states
            setEmailError(false)
            setPasswordError(false)
            setEmailErrorMsg('')
            setPasswordErrorMsg('')

            if (res.code === 0) {
                setAuth(res.msg, email, res.data.status.toString())
                // 触发自定义事件通知水印组件
                window.dispatchEvent(new Event('watermarkUpdate'))
                toast.success(trans.loginSuccess, {
                    description: trans.welcomeBack
                })
                router.push('/')
            } else if (res.code === 4062) {
                toast.error(trans.pleaseLoginFirst, {
                    description: res.msg
                })
                setEmailError(true)
                setEmailErrorMsg(res.msg)
                // Reset email error after 3 seconds
                setTimeout(() => {
                    setEmailError(false)
                    setEmailErrorMsg('')
                }, 3000)
            } else if (res.code === 4061) {
                toast.error(trans.pleaseLoginFirst, {
                    description: res.msg
                })
                setPasswordError(true)
                setPasswordErrorMsg(res.msg)
                // Reset password error after 3 seconds
                setTimeout(() => {
                    setPasswordError(false)
                    setPasswordErrorMsg('')
                }, 3000)
            }
        } catch (error) {
            toast.error(trans.pleaseLoginFirst, {
                description: trans.tryAgainLater
            })
        }
    }

    const handleSendCode = () => {
        toast.success(trans.verificationCodeSent, {
            description: trans.checkSMS
        })
    }

    return (
        <div className="min-h-screen bg-[#0D1117] flex flex-col items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
            <div className="mb-8">
                <Link href="/" className="flex items-center">
                    <Image
                        src="/bee-logo.png"
                        alt="BeeEval Logo"
                        width={180}
                        height={60}
                        className="object-contain"
                        priority
                        quality={100}
                    />
                </Link>
            </div>

            <Card className="w-full max-w-md bg-[#1C2028] border-gray-800">
                <CardHeader className="space-y-1">
                    <CardTitle className="text-2xl text-center font-bold text-white">{trans.welcomeLogin}</CardTitle>
                    <CardDescription className="text-center text-gray-400">{trans.loginToAccessMore}</CardDescription>
                </CardHeader>
                <CardContent>
                    <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
                        {/* <TabsList className="grid w-full grid-cols-1"> */}
                        {/* <TabsTrigger value="password">邮箱密码登录</TabsTrigger> */}
                        {/* <TabsTrigger value="phone">手机登录</TabsTrigger> */}
                        {/* </TabsList> */}
                        {/* <TabsContent value="password"> */}
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div className="space-y-2">
                                <Label htmlFor="email" className="text-gray-200">{trans.emailLabel}</Label>
                                <Input
                                    id="email"
                                    type="email"
                                    placeholder={trans.pleaseEnterEmail}
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                    className={`bg-[#0D1117] border-gray-800 text-white ${emailError ? 'border-red-500 focus:border-red-500 animate-shake' : ''
                                        }`}
                                />
                                {emailErrorMsg && (
                                    <p className="text-sm text-red-500 mt-1">{emailErrorMsg}</p>
                                )}
                            </div>
                            <div className="space-y-2">
                                <Label htmlFor="password" className="text-gray-200">{trans.passwordLabel}</Label>
                                <Input
                                    id="password"
                                    type="password"
                                    placeholder={trans.pleaseEnterPassword}
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                    className={`bg-[#0D1117] border-gray-800 text-white ${passwordError ? 'border-red-500 focus:border-red-500 animate-shake' : ''
                                        }`}
                                />
                                {passwordErrorMsg && (
                                    <p className="text-sm text-red-500 mt-1">{passwordErrorMsg}</p>
                                )}
                            </div>
                            <Button type="submit" className="w-full bg-amber-500 hover:bg-amber-600 text-black">
                                {trans.login}
                            </Button>
                        </form>
                        {/* </TabsContent> */}
                        <TabsContent value="phone">
                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div className="space-y-2">
                                    <Label htmlFor="phone">{trans.phoneNumber}</Label>
                                    <Input
                                        id="phone"
                                        type="tel"
                                        placeholder={trans.pleaseEnterPhoneNumber}
                                        value={phone}
                                        onChange={(e) => setPhone(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="space-y-2">
                                    <Label htmlFor="code">{trans.verificationCode}</Label>
                                    <div className="flex gap-2">
                                        <Input
                                            id="code"
                                            type="text"
                                            placeholder={trans.pleaseEnterVerificationCode}
                                            value={verificationCode}
                                            onChange={(e) => setVerificationCode(e.target.value)}
                                            required
                                        />
                                        <Button type="button" variant="outline" onClick={handleSendCode}>
                                            {trans.sendVerificationCode}
                                        </Button>
                                    </div>
                                </div>
                                <Button type="submit" className="w-full">
                                    {trans.login}
                                </Button>
                            </form>
                        </TabsContent>
                    </Tabs>
                    {/* <div className="mt-4 text-center text-sm text-gray-400">
                        {trans.noAccount}{' '}
                        <Link href="/register" className="text-amber-500 hover:underline">
                            {trans.register}
                        </Link>
                    </div> */}
                </CardContent>
            </Card>
        </div>
    )
}
