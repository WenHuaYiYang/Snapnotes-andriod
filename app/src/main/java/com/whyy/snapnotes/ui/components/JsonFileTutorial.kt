package com.whyy.snapnotes.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Add
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 可折叠的「JSON 文件教程」，平铺在主页选择文件入口之后。
 * 默认收起，点标题行展开/收起，内容随高度动画。
 */
@Composable
fun JsonFileTutorial(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "TutorialChevron"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = MiuixIcons.Info,
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "JSON 文件教程",
                        style = MiuixTheme.textStyles.title4,
                        fontWeight = FontWeight.Medium,
                        color = MiuixTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "了解知识点 JSON 文件怎么写",
                        style = MiuixTheme.textStyles.footnote1,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )
                }
                Icon(
                    imageVector = MiuixIcons.Add,
                    contentDescription = if (expanded) "收起" else "展开",
                    tint = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    modifier = Modifier
                        .size(22.dp)
                        .rotate(chevronRotation)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
                ) + fadeIn(),
                exit = shrinkVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)
                ) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Section(
                        "一句话",
                        "文件就是一个大括号对象，里面用「科目名」当钥匙，后面跟着这个科目的所有条目。"
                    )
                    Section(
                        "最简单的写法",
                        "{\n  \"我的笔记\": [\n    { \"title\": \"勾股定理\" }\n  ]\n}\n\n这样手环就会多出一个叫「我的笔记」的科目，里面有一条「勾股定理」。"
                    )
                    Section(
                        "一条条目里能写什么",
                        "title（标题）：必填，没有这条会被丢掉。\n" +
                                "desc（简介）：可选，显示在标题下方。\n" +
                                "points（要点）：可选，是字符串数组，每一条是一条速记要点。\n" +
                                "raw（原文）：可选，整段原文，会按行分段、按字数分页显示。\n" +
                                "formulas（公式）：可选，字符串数组，但见下方说明。\n" +
                                "id（编号）：可选，缺了会用顺序号，自己定编号时注意和内置知识点别撞号（见后）。"
                    )
                    Section(
                        "和内置知识点合并的规则",
                        "1. 科目名和手环自带科目相同（如「语文」）：只有编号没撞上的条目会被加进去，编号撞上的条目会被跳过（内置内容不会被覆盖）。想给内置科目补充内容，编号用一个大数。\n" +
                                "2. 科目名手环没有（如「拓展物理」）：整科作为新科目加进来，首页会标 导入 ，排在自带科目后面。\n" +
                                "3. 同一个文件里同科目同编号出现多次，以最后一个为准。重复推送同一文件不会让条目翻倍。"
                    )
                    Section(
                        "关于公式",
                        "手环自带的公式是提前渲染好图片的。你推送来的公式目前不会自动配图，所以公式区暂时不显示，只有文字。建议正文主要用 desc、points、raw 这几个文字字段，公式可以照常写，留给以后用。"
                    )
                    Section(
                        "文件要求",
                        "必须是 UTF-8 文本（中文直接写、不用转义），保存为 .json 文件，用 UTF-8（无 BOM）。文件大小没有硬上限，几 KB 到几百 KB 都行，手机会自动切片传给手环。"
                    )
                    Section(
                        "推荐完整写法",
                        "{\n" +
                                "  \"拓展物理\": [\n" +
                                "    {\n" +
                                "      \"id\": 1,\n" +
                                "      \"title\": \"相对论初步\",\n" +
                                "      \"desc\": \"狭义相对论的基本假设与时间膨胀效应。\",\n" +
                                "      \"raw\": \"狭义相对论建立在两条基本假设之上……\",\n" +
                                "      \"points\": [\n" +
                                "        \"光速不变原理\",\n" +
                                "        \"相对性原理\",\n" +
                                "        \"运动钟变慢\"\n" +
                                "      ]\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}"
                    )
                    Section(
                        "推送后没出现的常见原因",
                        "· 顶层写成了数组（应是对象）。\n" +
                                "· 条目里没有 title。\n" +
                                "· 给内置科目补充但编号撞了内置编号（被跳过）。\n" +
                                "   目前内置科目的内置编号由 1 排到 n（n为手环上显示的知识点数）\n"+
                                "· points 写成了字符串而不是数组。\n" +
                                "· 文件不是 UTF-8，中文乱码。\n" +
                                "以上都不会报错，只是对应内容被忽略，按这里逐项检查即可。"
                    )
                }
            }
        }
    }
}

@Composable
private fun Section(title: String, body: String) {
    Text(
        text = title,
        style = MiuixTheme.textStyles.title4,
        fontWeight = FontWeight.SemiBold,
        color = MiuixTheme.colorScheme.onSurface
    )
    Text(
        text = body,
        style = MiuixTheme.textStyles.body2,
        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
    )
}
