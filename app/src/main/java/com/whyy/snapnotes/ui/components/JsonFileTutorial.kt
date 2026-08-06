package com.whyy.snapnotes.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Info

/**
 * 可折叠的「JSON 文件教程」，平铺在主页选择文件入口之后。
 * 内容用 Markdown 渲染，与手环内置知识点格式说明保持同源。
 */
@Composable
fun JsonFileTutorial(modifier: Modifier = Modifier) {
    TutorialCard(
        title = "JSON 文件教程",
        subtitle = "了解知识点 JSON 文件怎么写",
        icon = MiuixIcons.Info,
        modifier = modifier
    ) {
        MarkdownText(JSON_TUTORIAL_MD)
    }
}

private val JSON_TUTORIAL_MD = """
# 一句话

文件就是一个**大括号对象**，键是科目名，值是这个科目下的条目数组。

```json
{ "我的笔记": [ { "title": "勾股定理" } ] }
```

推到手环后，就会多出一个叫「我的笔记」的科目，里面有一条「勾股定理」。

# 最简单的写法

最小可用文件就是上面这样，`title` 是唯一必填字段，其它字段缺了都有兜底，不报错。

# 一条条目里能写什么

- **title（标题）**：必填，没有这条会被丢掉。
- **id（编号）**：可选，缺了用数组里的顺序号（从 1 开始）。同名科目里按 id 去重。
- **desc（简介）**：可选，显示在标题下方。
- **points（要点）**：可选，字符串数组，每个字符串是一条速记要点。
- **raw（原文）**：可选，整段原文，会按行分段、按字数分页显示。
- **formulas（公式）**：可选，字符串数组，每条是一个公式。推完 JSON 后手机会自动把公式渲染成图片推到手环，详见「公式教程」。

# 和内置知识点合并的规则

1. **科目名和手环自带科目相同**（如「语文」）：按 id 合并，编号撞上内置的条目会被跳过（内置内容不会被覆盖）。想给内置科目补充内容，id 用一个大数。
2. **科目名手环没有**（如「拓展物理」）：整科作为新科目加进来，首页标「导入」，排在自带科目后面。
3. **同一个文件里**同科目同编号多次出现，以最后一个为准。重复推送同一文件不会让条目翻倍。

# 文件要求

- 必须是 **UTF-8 文本**（无 BOM），中文直接写、不用转义，保存为 `.json` 文件。
- 文件大小没有硬上限，几 KB 到几百 KB 都行，手机会自动切片传给手环。

# 推荐完整写法

```json
{
  "拓展物理": [
    {
      "id": 1,
      "title": "相对论初步",
      "desc": "狭义相对论的基本假设与时间膨胀效应。",
      "raw": "狭义相对论建立在两条基本假设之上……",
      "points": [
        "光速不变原理",
        "相对性原理",
        "运动钟变慢"
      ],
      "formulas": [
        "t = t0 / sqrt(1 - v^2/c^2)"
      ]
    }
  ]
}
```

# 推送后没出现的常见原因

- 顶层写成了数组（应是对象）。
- 条目里没有 title。
- 给内置科目补充，但 id 撞了内置编号（会被跳过）。
- points 写成了字符串而不是数组。
- 文件不是 UTF-8，中文乱码。

以上都不会报错，只是对应内容被忽略，按这里逐项检查即可。
""".trimIndent()
