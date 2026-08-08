package com.whyy.snapnotes.ui.components

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.whyy.snapnotes.ui.viewmodel.VersionIncompatibleState
import kotlin.system.exitProcess
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayDialog   // 替换 SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

// 联系开发者获取新版手环端闪念小抄的提示文案与 QQ 号。
// 与 AboutScreen.kt 的 CONTACT_QQ 保持同值；这里独立成常量避免跨包暴露私有常量，
// 改 QQ 号时记得两处同步。
private const val CONTACT_DEV_QQ = "664249113"
private const val CONTACT_DEV_HINT = "未找到 QQ 应用，已复制开发者 QQ：$CONTACT_DEV_QQ"

/** 调起 QQ 好友聊天窗口的 scheme 链接（QQ 应用内解析 mqqwpa://）。 */
private fun buildQqChatUri(uin: String): Uri =
    Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=$uin&version=1&src_type=web&web_src=oicqzone.com")

/** 真正退出应用：关掉全部 Activity 并结束进程。 */
private fun exitApp(context: Context) {
    (context as? Activity)?.finishAffinity()
    exitProcess(0)
}

/** 调起 QQ 聊天窗口；未安装 QQ 时回退为复制 QQ 号 + Toast 提示。 */
private fun contactDeveloper(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, buildQqChatUri(CONTACT_DEV_QQ))
    runCatching { context.startActivity(intent) }.onFailure {
        val clipboard = context.getSystemService(ClipboardManager::class.java)
        clipboard?.setPrimaryClip(ClipData.newPlainText("开发者 QQ", CONTACT_DEV_QQ))
        Toast.makeText(context, CONTACT_DEV_HINT, Toast.LENGTH_LONG).show()
    }
}

@Composable
fun VersionIncompatibleDialog(
    state: VersionIncompatibleState?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // 硬拦截：禁止返回键绕开版本检查。
    BackHandler(enabled = state != null) { }

    // 不在 OverlayDialog 外用 if (state==null) return 拔组件——那样退场动画来不及播。
    // 显示/隐藏完全交给 OverlayDialog 的 show：state 由非空→null 时播退场。
    val s = state
    OverlayDialog(
        title = "版本不兼容",
        show = s != null,   // 由 state 是否为空驱动；组件始终在组合树里，OverlayDialog 自管进退场
        onDismissRequest = { /* 禁止点击外部取消 */ }
    ) {
        if (s != null) {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "手环端版本过低，无法继续操作。请联系开发者更新手环端闪念小抄后再使用。",
                        style = MiuixTheme.textStyles.body1,
                        color = MiuixTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "当前手环端版本号：${s.currentVersion}",
                        style = MiuixTheme.textStyles.body2,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )
                    Text(
                        text = "所需最低版本：${s.requiredVersionName} (${s.requiredVersion})",
                        style = MiuixTheme.textStyles.body2,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    text = "退出应用",
                    onClick = {
                        onDismiss()
                        exitApp(context)
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = "联系开发者",
                    colors = ButtonDefaults.textButtonColorsPrimary(),
                    onClick = { contactDeveloper(context) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
