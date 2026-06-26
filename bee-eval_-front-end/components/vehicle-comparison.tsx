import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

const comparisonData = [
  { feature: '续航里程', 'Model S': '600 km', 'Taycan': '450 km', 'ET7': '580 km' },
  { feature: '百公里加速', 'Model S': '2.1 秒', 'Taycan': '2.8 秒', 'ET7': '3.8 秒' },
  { feature: '最大功率', 'Model S': '670 kW', 'Taycan': '560 kW', 'ET7': '480 kW' },
  { feature: '起步价', 'Model S': '¥688,900', 'Taycan': '¥898,000', 'ET7': '¥458,000' },
]

export function VehicleComparison() {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead className="w-[200px]">特性</TableHead>
          <TableHead>特斯拉 Model S</TableHead>
          <TableHead>保时捷 Taycan</TableHead>
          <TableHead>蔚来 ET7</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {comparisonData.map((row) => (
          <TableRow key={row.feature}>
            <TableCell className="font-medium">{row.feature}</TableCell>
            <TableCell>{row['Model S']}</TableCell>
            <TableCell>{row['Taycan']}</TableCell>
            <TableCell>{row['ET7']}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  )
}

