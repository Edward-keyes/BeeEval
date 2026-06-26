import Image from 'next/image'

export function VehicleGallery() {
  const images = Array(12).fill('/placeholder.svg?height=200&width=300')
  
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 p-6">
      {images.map((src, index) => (
        <div key={index} className="relative aspect-video rounded-lg overflow-hidden">
          <Image
            src={src}
            alt={`Vehicle image ${index + 1}`}
            fill
            className="object-cover"
          />
        </div>
      ))}
    </div>
  )
}

