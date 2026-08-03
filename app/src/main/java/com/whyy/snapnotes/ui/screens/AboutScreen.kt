package com.whyy.snapnotes.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.whyy.snapnotes.R
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberTopAppBarState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic

// ── 开发者与参考项目信息 ──────────────────────────────────────────────
private const val DEV_NAME = "文华逸洋"
/** QQ 号（数字字符串），点击复制到剪贴板。 */
private const val CONTACT_QQ = "664249113"
/** GitHub 用户名（不含 @ 与域名），点击跳转 github.com/<[user]>；与昵称一致。 */
private const val CONTACT_GITHUB = "WenHuaYiYang"
/** GitHub 主页完整链接（用于「其他渠道」/参考项目等需要完整 url 的条目）。 */
private const val GITHUB_URL = "https://github.com/WenHuaYiYang"
/** 邮箱，点击复制到剪贴板。 */
private const val CONTACT_EMAIL = "zcsjwhdh@163.com"
/** 其他渠道：哔哩哔哩个人空间，点击跳转。 */
private const val CONTACT_OTHER = "哔哩哔哩"
private const val CONTACT_OTHER_URL = "https://space.bilibili.com/1927172224"
/** 参考项目。 */
private const val REF_PROJECT_NAME = "弦电子书-安卓（com.bandbbs.ebook-android）"
private const val REF_PROJECT_URL = "https://github.com/youshen2/com.bandbbs.ebook-android"
private const val REF_PROJECT_NAME_ANDROID = "弦电子书-手环（com.bandbbs.ebook）"
private const val REF_PROJECT_URL_ANDROID = "https://github.com/youshen2/com.bandbbs.ebook"
/** 底部版本标签（彩蛋藏在这条的长按上；不带任何 ❤ 装饰）。 */
private const val APP_VERSION_LABEL = "版本 1.0"
// ──────────────────────────────────────────────────────────────────────

@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollBehavior = MiuixScrollBehavior(rememberTopAppBarState())
    // 彩蛋显示态：长按底部隐藏条目唤起。
    var showEgg by remember { mutableStateOf(false) }

    // 复制到剪贴板 + Toast（与 BuiltinFileManagerScreen 的 Toast 写法一致）。
    fun copyToClipboard(label: String, value: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.setPrimaryClip(ClipData.newPlainText(label, value))
        Toast.makeText(context, "已复制：$label", Toast.LENGTH_SHORT).show()
    }

    // 跳转外链：与 VersionInConfirmDialog 跳转官网同一写法。
    fun openUrl(url: String) {
        runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = "关于",
                    largeTitle = "关于",
                    navigationIcon = {
                        IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 6.dp)) {
                            Icon(imageVector = MiuixIcons.Back, contentDescription = "返回")
                        }
                    }
                )
            },
            popupHost = {}
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .overScrollVertical()
                    .scrollEndHaptic(),
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = 40.dp
                )
            ) {
                // ── 开发者 ──
                item {
                    SmallTitle(text = "开发者", modifier = Modifier.padding(top = 12.dp))
                    Card(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        insideMargin = PaddingValues(0.dp)
                    ) {
                        // 头像 + 昵称居中展示在卡片顶部，下方紧接联系方式列表。
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, bottom = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.avatar),
                                contentDescription = "头像",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = DEV_NAME,
                                style = MiuixTheme.textStyles.title2,
                                fontWeight = FontWeight.SemiBold,
                                color = MiuixTheme.colorScheme.onSurface
                            )
                        }
                        BasicComponent(
                            title = "QQ 号",
                            summary = CONTACT_QQ,
                            onClick = { copyToClipboard("QQ 号", CONTACT_QQ) }
                        )
                        BasicComponent(
                            title = "GitHub",
                            summary = "@$CONTACT_GITHUB",
                            onClick = { openUrl(GITHUB_URL) }
                        )
                        BasicComponent(
                            title = "邮箱",
                            summary = CONTACT_EMAIL,
                            onClick = { copyToClipboard("邮箱", CONTACT_EMAIL) }
                        )
                        BasicComponent(
                            title = "其他渠道",
                            summary = CONTACT_OTHER,
                            onClick = { openUrl(CONTACT_OTHER_URL) }
                        )
                    }
                }
                // ── 参考项目 ──
                item {
                    SmallTitle(text = "参考项目")
                    Card(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        insideMargin = PaddingValues(0.dp)
                    ) {
                        BasicComponent(
                            title = REF_PROJECT_NAME_ANDROID,
                            onClick = { openUrl(REF_PROJECT_URL_ANDROID) }
                        )
                        BasicComponent(
                            title = REF_PROJECT_NAME,
                            onClick = { openUrl(REF_PROJECT_URL) }
                        )
                    }
                }
                // ── 隐藏条目（彩蛋触发点） ──
                // 用一块「裸 Box」（非 Miuix 组件、无内置手势消费）展示版本号文字。
                // 长按这块区域唤起粒子彩蛋；普通点击不响应、看上去就是个版本标签。
                // 之所以用裸 Box 而非.SmallTitle/BasicComponent，是后两者内置 pointer 处理
                // 会消费事件，导致 detectTapGestures(onLongPress) 收不到回调（实测长按无反应）。
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            // 给一块足够大的命中区域，方便长按不脱靶。
                            .height(56.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = { showEgg = true })
                            }
                    ) {
                        Text(
                            text = APP_VERSION_LABEL,
                            style = MiuixTheme.textStyles.body2,
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, top = 18.dp)
                        )
                    }
                }
            }
        }
        // 彩蛋覆盖层：长按底部版本条目唤起，点空白 / 返回键关闭。
        EasterEggParticle(
            show = showEgg,
            onDismiss = { showEgg = false }
        )
    }
}
