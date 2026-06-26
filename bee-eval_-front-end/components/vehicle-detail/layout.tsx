export default function VehicleDetailLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <div className="min-h-screen bg-[#171717]">
      <div className="container mx-auto py-6">
        {children}
      </div>
    </div>
  )
} 