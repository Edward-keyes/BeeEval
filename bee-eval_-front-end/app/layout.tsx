// 服务器组件 - 处理元数据
import type { Metadata } from 'next'
import { ClientLayout } from './client-layout';
import localFont from 'next/font/local'

const oswaldFont = localFont({
    src: '../typeface/Oswald-VariableFont_wght.ttf',
    variable: '--font-oswald'
})

export const metadata: Metadata = {
    title: 'BeeEval - 智能汽车体验平台',
    description: '探索下一代智能汽车体验',
    icons: {
        icon: '/images/BEE_Eval.png',
        shortcut: '/images/BEE_Eval.png',
        apple: '/images/BEE_Eval.png',
    }
};

export default function RootLayout({
    children,
}: {
    children: React.ReactNode
}) {
    return (
        <html suppressHydrationWarning className={oswaldFont.variable}>
            <body>
                <ClientLayout>
                    {children}
                </ClientLayout>
            </body>
        </html>
    )
}

