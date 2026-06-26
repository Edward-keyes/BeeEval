export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
        <div>
            我是服务器组件内容：
            Welcome
            <br />
            <br />     
            <br />
            <br />
            下面是客户端组件：
            {children}
        </div>
    )
}