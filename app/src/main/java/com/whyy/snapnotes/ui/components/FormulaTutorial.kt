package com.whyy.snapnotes.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Notes

/**
 * 可折叠的「公式教程」，公式板块从 JSON 教程里单独拎出来讲。
 * 介绍 formulas 字段的写法、渲染与推送流程、以及常见问题。
 */
@Composable
fun FormulaTutorial(modifier: Modifier = Modifier) {
    TutorialCard(
        title = "公式教程",
        subtitle = "formulas 怎么写、怎么自动渲染推送到手环",
        icon = MiuixIcons.Notes,
        modifier = modifier
    ) {
        MarkdownText(FORMULA_TUTORIAL_MD)
    }
}

private val FORMULA_TUTORIAL_MD = """
# 一句话

条目里的 `formulas` 数组写好公式后，**JSON 推送完成会自动额外地**把每条公式渲染成一张**公式图**，再逐张推给手环；手环按「科目名#id」登记索引，知识点详情页的公式区就能显示出来。

# 在 JSON 里怎么写公式

`formulas` 是字符串数组，每个字符串一条公式，写在条目里即可：

```json
{
  "拓展物理": [
    {
      "id": 1,
      "title": "时间膨胀",
      "formulas": [
        "t = t0 / sqrt(1 - v^2/c^2)",
        "L = L0 * sqrt(1 - v^2/c^2)"
      ]
    }
  ]
}
```

写法上很自由：

- **直接写数学文本**：`t = t0 / sqrt(1 - v^2/c^2)` 这种就行，√、½、上标等符号都能转。内置 168 条常用公式的精确映射表命中时，直接用手环内置同款渲染。
- **也可以直接写 LaTeX**：`\frac{-b \pm \sqrt{b^2 - 4ac}}{2a}` 这样的也支持。
- 一个条目里的多条公式会**垂直堆叠**成一张图，公式之间自动留间距，与手环内置公式图同规格。

# 推送流程（全自动）

1. JSON 推完并收到 `transfer_finished` 回执后自动开始。
2. 手机端自动找出这份 JSON 里所有写了 `formulas` 的条目。
3. 逐条把公式渲染成 **336px 宽的 PNG**（白字、透明底，与手环内置规格一致）。
4. 逐张用 `startFormula` 协议分片推给手环，文件名 = `md5(科目名#id)` 前 12 位 + `.png`。
5. 全部完成后显示「知识点与公式图已同步」；个别失败的会跳过，结果页会提示几个没同步。

# 注意事项

- **科目名和 id 必须和 JSON 条目完全一致**（大小写、全半角都要一致）。手环靠「科目名#id」这个 key 去找图，对不上就显示不出来。
- 公式别写太长，超出 336px 宽会自动等比缩小，尽量保持不换行。
- 重复推送同一份文件是**幂等**的：同名文件直接覆盖，不会重复。
- 手环端删除某个导入科目时，会一并清理该科目下已推送的公式图。
- 手机上的实时预览（编辑页公式区）和推送到手环是**同一套渲染**，看到的效果就是手环上的效果。

# 常见问题排查

| 现象 | 多半原因 |
| --- | --- |
| 公式区不显示 | ① 科目名/id 与 JSON 不一致（索引 key 对不上）② 条目里没写 `formulas` ③ 该条渲染或推送失败被跳过（看结果页提示） |
| 图片被压缩变形 | `w`/`h` 与 PNG 实际像素不符（正常推送不会发生，手机端取的是真实宽高） |
| 传输进度卡住 | 没等手环回执就发下一条（单 BLE 通道需要流控，手机端已按回执逐片确认） |
""".trimIndent()