// 生成指定范围内的随机整数
export const randomInt = (min: number, max: number) => {
  const floor = Math.ceil(min)
  const ceil = Math.floor(max)
  return Math.floor(Math.random() * (ceil - floor + 1)) + floor
}

// 随机挑选数组中的一个元素
export const randomPick = <T>(items: T[]) => {
  return items[Math.floor(Math.random() * items.length)]
}

// 生成科研时长随机分钟数
export const randomResearchMinutes = () => {
  return randomInt(60, 360)
}
