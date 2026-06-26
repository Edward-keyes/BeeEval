import { Card, CardContent } from "@/components/ui/card"

interface MetricsCardProps {
  value: string | number
  label: string
  trend?: number
}

export function MetricsCard({ value, label, trend }: MetricsCardProps) {
  return (
    <Card>
      <CardContent className="pt-6">
        <div className="flex justify-between items-start">
          <div>
            <div className="text-3xl font-semibold">{value}</div>
            <div className="text-sm text-muted-foreground mt-1">{label}</div>
          </div>
          {trend && (
            <div className={`text-sm ${trend > 0 ? 'text-green-500' : 'text-red-500'}`}>
              {trend > 0 ? '+' : ''}{trend}%
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  )
}

