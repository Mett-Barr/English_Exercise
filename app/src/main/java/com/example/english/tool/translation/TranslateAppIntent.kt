package com.example.english.tool.translation

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast


fun translateApp(context: Context, text: String) {
    val intent = Intent()
//    intent.action = Intent.EXTRA_PROCESS_TEXT
    intent.action = Intent.ACTION_PROCESS_TEXT
    intent.type = "text/plain"
//    intent.setPackage("com.google.android.apps.translate")
//    intent.putExtra(Intent.EXTRA_TEXT, text)
    intent.putExtra(Intent.EXTRA_PROCESS_TEXT, text)
//    context.startActivity(intent)

//    intent.component = ComponentName("com.google.android.apps.translate",
//        "com.google.android.apps.translate.QuickTranslateActivity")

    context.startActivity(intent)

//    intent.component = ComponentName("com.google.android.apps.translate",
//        "com.google.android.apps.translate.TranslateActivity")
//    context.startActivity(intent)

//    for (resolveInfo in context.packageManager.queryIntentActivities(intent, 0)) {
//        if (resolveInfo.activityInfo.packageName.contains("com.google.android.apps.translate")) {
//            intent.component = ComponentName(
//                resolveInfo.activityInfo.packageName,
//                resolveInfo.activityInfo.name)
//            context.startActivity(intent)
//        }
//    }
}

fun wordTranslate(context: Context, text: String) {
    try {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
//        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, text)
//        intent.putExtra(Intent.EXTRA_TEXT, "hello")
//        intent.putExtra("key_text_input", text)
//        intent.putExtra("key_text_output", "")
//        intent.putExtra("key_language_from", "en")
//        intent.putExtra("key_language_to", "mal")
//        intent.putExtra("key_suggest_translation", "")
        intent.putExtra("key_from_floating_window", false)
        intent.component = ComponentName(
            "com.google.android.apps.translate",  //Change is here
            //"com.google.android.apps.translate.HomeActivity"));
            "com.google.android.apps.translate.TranslateActivity")
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO Auto-generated catch block
        Toast.makeText(context, "Sorry, No Google Translation Installed",
            Toast.LENGTH_SHORT).show()
    }
}

fun createProcessTextIntent(): Intent {
    return Intent()
        .setAction(Intent.ACTION_PROCESS_TEXT)
        .setType("text/plain")
}

fun getSupportedActivities(context: Context): MutableList<ResolveInfo> {
    val packageManager = context.packageManager
    return packageManager.queryIntentActivities(
        createProcessTextIntent(), 0)

}

//fun onInitializeMenu(menu: Menu, context: Context) {
//    // Start with a menu Item order value that is high enough
//    // so that your "PROCESS_TEXT" menu items appear after the
//    // standard selection menu items like Cut, Copy, Paste.
//    var menuItemOrder = 100
//    for (resolveInfo in getSupportedActivities(context)) {
//        menu.add(Menu.NONE, Menu.NONE,
//            menuItemOrder++,
//            resolveInfo.labelRes)
//            .setIntent(createProcessTextIntentForResolveInfo(resolveInfo)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
//    }
//}

fun onInitializeMenu(menu: Menu, context: Context) {
    // Start with a menu Item order value that is high enough
    // so that your "PROCESS_TEXT" menu items appear after the
    // standard selection menu items like Cut, Copy, Paste.
    var menuItemOrder = 100
    for (resolveInfo in getSupportedActivities(context = context)) {
        menu.add(Menu.NONE, Menu.NONE,
            menuItemOrder++,
            resolveInfo.labelRes)
            .setIntent(createProcessTextIntentForResolveInfo(resolveInfo))
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }
}

private fun createProcessTextIntentForResolveInfo(info: ResolveInfo): Intent? {
    return createProcessTextIntent()
        .putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)
        .setClassName(info.activityInfo.packageName,
            info.activityInfo.name)
}