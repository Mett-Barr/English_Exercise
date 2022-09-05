package com.example.english.ui.components.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.english.R
import com.example.english.data.newslist.room.News
import com.example.english.ui.components.ClickableIcon

//@Preview
@Composable
fun PopupInfo(news: News, progress: Int, delete: () -> Unit) {
    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(12.dp)
    ) {

        CompositionLocalProvider(LocalTextStyle provides TextStyle(fontSize = 20.sp)) {


//            Box(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier
                .padding(8.dp)
                .width(intrinsicSize = IntrinsicSize.Max)) {

//                Column {
//                    if (news.source.isNotBlank()) {
//                        InfoRow(tag = "   source", description = news.source)
//                        Divider(modifier = Modifier.padding(vertical = 4.dp))
//                    }
//
//                    InfoRow(tag = "   date", description = news.date)
//                    Divider(modifier = Modifier.padding(vertical = 4.dp))
//
//                    if (news.tag.isNotBlank()) {
//                        InfoRow(tag = "   tag", description = news.tag)
//                        Divider(modifier = Modifier.padding(vertical = 4.dp))
//                    }
////                        InfoRow(tag = "   start", description = "2022/10/11")
////                        Divider(modifier = Modifier.padding(vertical = 4.dp))
//                    InfoRow(tag = "   progress", description = "${progress}%")
//                }

                Row {
                    Column(modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max)) {
//                    Column(modifier = Modifier.weight(1f)) {
                        if (news.source.isNotBlank()) {
                            Text("   source    ")
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                        Text("   date    ")
                        Divider(modifier = Modifier.padding(vertical = 4.dp))

                        if (news.tag.isNotBlank()) {
                            Text("   tag    ")
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                        Text("   progress    ")
                        ClickableIcon(painter = painterResource(R.drawable.delete),
                            tint = MaterialTheme.colors.error)

                    }
                    Column(modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max)) {
//                    Column(modifier = Modifier.weight(2f)) {
                        if (news.source.isNotBlank()) {
                            Text(news.source)
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                        Text(news.date)
                        Divider(modifier = Modifier.padding(vertical = 4.dp))

                        if (news.tag.isNotBlank()) {
                            Text("Tech")
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                        Text("${progress}%")
                        if (news.source.isNotBlank()) {
                            Row {
                                ClickableIcon(painter = painterResource(R.drawable.link))
                                ClickableIcon(painter = painterResource(R.drawable.share))
                                ClickableIcon(painter = painterResource(R.drawable.website))
                            }
                        }

                    }
                }
//        Text("2022/10/10  Tech")

//                Row(modifier = Modifier.align(Alignment.End)) {
//                    ClickableIcon(painter = painterResource(R.drawable.delete),
//                        tint = MaterialTheme.colors.error)
//                    Spacer(modifier = Modifier.weight(1f))
//                    if (news.source.isNotBlank()) {
//                        ClickableIcon(painter = painterResource(R.drawable.website))
//                        ClickableIcon(painter = painterResource(R.drawable.link))
//                        ClickableIcon(painter = painterResource(R.drawable.share))
//                    }
//                }
            }
//            }
        }
    }
}

@Composable
fun InfoRow(tag: String, description: String) {
    Row {
        Text(text = tag, Modifier.weight(1f), maxLines = 1)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = description, Modifier.weight(2f), maxLines = 1)
    }
}

//@Preview
//@Composable
//fun PopupInfo() {
//    Box(modifier = Modifier.padding(8.dp)) {
//        Column(modifier = Modifier
//            .fillMaxWidth()
//            .background(MaterialTheme.colors.background)) {
//            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
//                Text("BBC",
//                    style = Typography().h5,
//                    modifier = Modifier.align(Alignment.CenterVertically))
//                ClickableIcon(painter = painterResource(id = R.drawable.link))
//            }
//            Text("2022/10/10  Tech")
//            Text("start  2022/10/11")
//            Text("end 2022/10/11")
//            Text("50%")
////        Text("2022/10/10  Tech")
//        }
//        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
//            ClickableIcon(painter = painterResource(R.drawable.link))
//            ClickableIcon(painter = painterResource(R.drawable.share))
//        }
//    }
//}