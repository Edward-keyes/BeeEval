import Image from 'next/image'

export function WelcomeSection() {
  return (
    <div className="relative h-[600px] bg-gradient-to-b from-gray-900 to-gray-800 text-white">
      <div className="absolute inset-0">
        <Image
          src="/placeholder.svg?height=600&width=1200"
          alt="Interior dashboard view"
          fill
          className="object-cover opacity-50"
        />
      </div>
      <div className="relative z-10 container mx-auto px-6 py-20">
        <h2 className="text-4xl font-bold mb-6">欢迎您的到来</h2>
        <p className="max-w-2xl text-lg text-gray-300">
          我们致力于为您提供最优质的汽车服务体验。通过我们的平台，您可以轻松管理您的车辆信息，获取最新的行业动态，以及享受个性化的服务推荐。
        </p>
      </div>
    </div>
  )
}

