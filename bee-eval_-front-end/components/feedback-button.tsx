import { useState } from 'react'
import { MessageSquarePlus, HelpCircle } from 'lucide-react'
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { translations } from '@/language'
import { useLanguage, Language } from '@/constants/language'
import {
    Tooltip,
    TooltipContent,
    TooltipProvider,
    TooltipTrigger,
} from "@/components/ui/tooltip"
import { useRouter } from 'next/navigation'

export function FeedbackButton() {
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        message: ''
    })
    const { language } = useLanguage()
    const router = useRouter()

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        // 处理表单提交
        // console.log('Form submitted:', formData)
        setIsDialogOpen(false)
        setFormData({ name: '', email: '', message: '' })
    }

    const feedbackContent = {
        title: translations[language as Language].feedbackTitle,
        description: translations[language as Language].feedbackDescription
    }

    return (
        <TooltipProvider>
            <div className="fixed right-8 bottom-24 z-50 flex flex-col gap-4">
                <Tooltip>
                    <TooltipTrigger asChild>
                        <Button
                            className="rounded-full w-12 h-12 bg-amber-500 hover:bg-amber-600 text-black shadow-lg flex items-center justify-center"
                            size="icon"
                            onClick={() => router.push('/manual')}
                        >
                            <HelpCircle className="h-6 w-6" />
                        </Button>
                    </TooltipTrigger>
                    <TooltipContent>
                        <p>网站使用手册</p>
                    </TooltipContent>
                </Tooltip>

                <Tooltip>
                    <TooltipTrigger asChild>
                        <Button
                            onClick={() => setIsDialogOpen(true)}
                            className="rounded-full w-12 h-12 bg-amber-500 hover:bg-amber-600 text-black shadow-lg flex items-center justify-center"
                            size="icon"
                        >
                            <MessageSquarePlus className="h-6 w-6" />
                        </Button>
                    </TooltipTrigger>
                    <TooltipContent>
                        <p>意见反馈</p>
                    </TooltipContent>
                </Tooltip>
            </div>

            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <DialogContent className="bg-[#1C2028] border-gray-800 sm:max-w-[425px]">
                    <DialogHeader>
                        <DialogTitle className="text-2xl font-semibold text-amber-500">
                            {translations[language as Language].feedbackTitle}
                        </DialogTitle>
                        <DialogDescription className="text-gray-400">
                            {translations[language as Language].feedbackDescription}
                        </DialogDescription>
                    </DialogHeader>
                    <form onSubmit={handleSubmit} className="space-y-6 py-4">
                        <div className="space-y-4">
                            <div className="space-y-2">
                                <Input
                                    placeholder={translations[language as Language].feedbackName}
                                    className="bg-black/20 border-gray-800 text-white placeholder:text-gray-500"
                                    value={formData.name}
                                    onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                                />
                            </div>
                            <div className="space-y-2">
                                <Input
                                    type="email"
                                    placeholder={translations[language as Language].feedbackEmail}
                                    className="bg-black/20 border-gray-800 text-white placeholder:text-gray-500"
                                    value={formData.email}
                                    onChange={(e) => setFormData(prev => ({ ...prev, email: e.target.value }))}
                                />
                            </div>
                            <div className="space-y-2">
                                <Textarea
                                    placeholder={translations[language as Language].feedbackMessage}
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
                            {translations[language as Language].feedbackSubmit}
                        </Button>
                    </form>
                </DialogContent>
            </Dialog>
        </TooltipProvider>
    )
} 