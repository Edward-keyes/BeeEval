'use client'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { translations } from "@/language"
import { useLanguage } from "@/constants/language"

export function UserFeedback() {
    const { language } = useLanguage()
    return (
        <Card>
            <CardHeader>
                <CardTitle>{translations[language].userFeedback}</CardTitle>
            </CardHeader>
            <CardContent>
                <div className="text-center text-muted-foreground py-8">
                    {translations[language].userFeedbackDescription}
                </div>
            </CardContent>
        </Card>
    )
}

