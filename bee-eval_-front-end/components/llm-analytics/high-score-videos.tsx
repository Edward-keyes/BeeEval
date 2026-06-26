import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ArrowRight } from 'lucide-react'
import Image from "next/image"
import Link from "next/link"

const videoData = {
  "车书域": [
    { title: "精准导航", score: 95, image: "/placeholder.svg?height=200&width=300", link: "/video/1" },
    { title: "路况预测", score: 93, image: "/placeholder.svg?height=200&width=300", link: "/video/2" },
    { title: "车辆诊断", score: 91, image: "/placeholder.svg?height=200&width=300", link: "/video/3" },
  ],
  // ... 其他域的数据
}

interface HighScoreVideosProps {
  domain: string
}

export function HighScoreVideos({ domain }: HighScoreVideosProps) {
  const videos = videoData[domain] || []

  return (
    <Card className="overflow-hidden border-2 border-primary/10">
      <CardHeader className="bg-gradient-to-r from-primary/5 to-primary/10 border-b border-primary/10">
        <div className="flex items-center justify-between">
          <CardTitle className="text-2xl font-bold text-primary">高分视频</CardTitle>
          <Link 
            href={`/analytics/${domain}`}
            className="text-sm text-muted-foreground hover:text-primary transition-colors flex items-center gap-1"
          >
            查看全部 <ArrowRight className="h-4 w-4" />
          </Link>
        </div>
      </CardHeader>
      <CardContent className="p-6">
        <div className="grid gap-6">
          {videos.map((video, index) => (
            <Card key={video.title} className="overflow-hidden hover:border-primary transition-colors">
              <div className="flex items-center">
                <div className="relative w-1/3 aspect-video">
                  <Image
                    src={video.image}
                    alt={video.title}
                    fill
                    className="object-cover"
                  />
                  <div className="absolute top-2 right-2 bg-primary text-primary-foreground text-sm font-medium px-2 py-1 rounded-full">
                    {video.score}分
                  </div>
                </div>
                <div className="p-4 w-2/3">
                  <h3 className="font-medium text-lg mb-2">{video.title}</h3>
                  <Link 
                    href={video.link}
                    className="text-sm text-primary hover:underline"
                  >
                    观看视频
                  </Link>
                </div>
              </div>
            </Card>
          ))}
        </div>
      </CardContent>
    </Card>
  )
}

