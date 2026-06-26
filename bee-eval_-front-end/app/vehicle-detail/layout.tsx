import { Metadata } from 'next'

export const metadata: Metadata = {
  title: '车辆详情 - BeeEval',
  description: '车辆智能系统评测详情页面',
}

export default function VehicleDetailLayout({
  children,
}: {
  children: React.ReactNode
  }) {
  return children
} 