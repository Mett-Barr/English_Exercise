package com.example.english.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.english.R
import com.example.english.data.word.word.room.Word
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

//@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
//@Composable
//fun WordListTable(
////    list: SnapshotStateList<Int>,
//    wordList: SnapshotStateList<State<Word>>,
//    getWordById: (Int) -> Flow<Word>,
//    deleteWord: (Int) -> Unit,
//    updateWord: (Word) -> Unit,
//    id: Int,
//    viewModel: MainViewModel,
//) {
//
//    val focusManager = LocalFocusManager.current
//    val coroutineScope = rememberCoroutineScope()
//
////    val wordList: SnapshotStateList<State<Word>> = emptyList<State<Word>>().toMutableStateList()
////    list.forEachIndexed { index, it ->
////        wordList.add(getWordById(it).collectAsState(initial = EmptyWord.word))
////        Log.d("!!!", "WordListTable: $index")
////    }
//
//    val swipeRange = with(LocalDensity.current) { (48.dp).toPx() }
//
//    Column(modifier = Modifier
//        .padding(bottom = 8.dp)
//        .animateContentSize()
//        .focusable()) {
//
//
//        wordList.forEachIndexed { index, word ->
//
//            val anchors = mapOf(0f to "normal", -swipeRange to "delete")
//            val swipeableState = rememberSwipeableState("normal")
//
//            var visible by remember {
//                mutableStateOf(true)
//            }
//
//            AnimatedVisibility(visible = visible, enter = scaleIn(), exit = scaleOut()) {
//
//
//                Box(modifier = Modifier
////                    .scale()
//                    .swipeable(
//                        state = swipeableState,
//                        anchors = anchors,
//                        thresholds = { _, _ -> FractionalThreshold(1f) },
//                        orientation = Orientation.Horizontal
//                    )
//                    .focusable()
//                    .onFocusChanged {
//                        coroutineScope.launch {
//                            swipeableState.animateTo("normal")
//                        }
//                    }
//                ) {
//
//                    val scale by remember {
//                        derivedStateOf {
//                            (-swipeableState.offset.value + swipeRange) / swipeRange / 2
//                        }
//                    }
//
//                    ClickableIcon(
//                        painter = painterResource(id = R.drawable.delete),
//                        modifier = Modifier
//                            .scale(scale)
//                            .align(Alignment.CenterEnd),
//                        onClick = {
//
//                            visible = false
//
//                            wordList.removeAt(index)
////                        list.removeAt(index)
//
//                            coroutineScope.launch {
//                                swipeableState.snapTo("normal")
//                            }
//                        })
//
//                    Card(elevation = 4.dp,
//                        modifier = Modifier
//                            .padding(4.dp)
//                            .heightIn(min = 48.dp)
//                            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) },
//                        shape = RoundedCornerShape(4.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .height(IntrinsicSize.Max)
//                                .padding(4.dp)
//                        ) {
//                            Text(
//                                text = word.value.english,
//                                modifier = Modifier
//                                    .align(Alignment.CenterVertically)
//                                    .weight(1F)
//                                    .padding(horizontal = 8.dp),
//                                style = Typography().h6
//                            )
//                            Divider(
//                                modifier = Modifier
//                                    .fillMaxHeight()
//                                    .width(1.dp),
//                                color = MaterialTheme.colors.onBackground
//                            )
//
//                            BasicTextField(
//                                value = word.value.chinese,
//                                onValueChange = {
//                                    wordList[index] =
//                                        mutableStateOf(Word(word.value.id, word.value.english, it))
//                                },
//                                modifier = Modifier
//                                    .align(Alignment.CenterVertically)
//                                    .weight(1F)
//                                    .padding(horizontal = 8.dp)
//                                    .onFocusChanged { updateWord(word.value) },
//                                textStyle = Typography().h6.copy(color = MaterialTheme.colors.onBackground),
//                                cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
//                            )
//                        }
//                    }
//                }
//
//            }
//
//        }
//
//
////        list.forEachIndexed { index, wordId ->
////            val originWord by getWordById(wordId).collectAsState(initial = EmptyWord.word)
////            val isInitialized by remember(originWord) {
////                derivedStateOf { originWord == EmptyWord.word }
////            }
////            var word by remember {
////                mutableStateOf(EmptyWord.word)
////            }
////
////            DisposableEffect(isInitialized) {
////                onDispose {
////                    word = originWord
////                }
////            }
////
////
////            val anchors = mapOf(0f to "normal", -swipeRange to "delete")
////            val swipeableState = rememberSwipeableState("normal")
////
////            Box(modifier = Modifier
////                .swipeable(
////                    state = swipeableState,
////                    anchors = anchors,
////                    thresholds = { _, _ -> FractionalThreshold(1f) },
////                    orientation = Orientation.Horizontal)
////            ) {
////
////                val scale by remember {
////                    derivedStateOf {
////                        (-swipeableState.offset.value + swipeRange) / swipeRange / 2
////                    }
////                }
////
////                ClickableIcon(
////                    painter = painterResource(id = R.drawable.delete),
////                    modifier = Modifier
////                        .scale(scale)
////                        .align(Alignment.CenterEnd),
////                    onClick = {
////                        deleteWord(wordId)
////                    })
////
////                Card(elevation = 4.dp,
////                    modifier = Modifier
////                        .padding(4.dp)
////                        .heightIn(min = 48.dp)
////                        .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) },
////                    shape = RoundedCornerShape(4.dp)
////                ) {
////                    Row(
////                        modifier = Modifier
////                            .height(IntrinsicSize.Max)
////                            .padding(4.dp)
////                    ) {
////                        Text(
////                            text = "$index $wordId ${word.english}",
////                            modifier = Modifier
////                                .align(Alignment.CenterVertically)
////                                .weight(1F)
////                                .padding(horizontal = 8.dp),
////                            style = Typography().h6
////                        )
////                        Divider(
////                            modifier = Modifier
////                                .fillMaxHeight()
////                                .width(1.dp),
////                            color = MaterialTheme.colors.onBackground
////                        )
////
////                        BasicTextField(
////                            value = word.chinese,
////                            onValueChange = { word = Word(word.id, word.english, it) },
////                            modifier = Modifier
////                                .align(Alignment.CenterVertically)
////                                .weight(1F)
////                                .padding(horizontal = 8.dp)
////                                .onFocusChanged { updateWord(word) },
////                            textStyle = Typography().h6.copy(color = MaterialTheme.colors.onBackground),
////                            cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
////                        )
////                    }
////                }
////            }
////        }
//    }
//}









@OptIn(ExperimentalMaterialApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun WordComponent(
    word: State<Word>,
    onValueChange: (String) -> Unit,
    remove: () -> Unit,
    updateWord: () -> Unit,
) {

//    val currentWord = remember {
//        mutableStateOf(word.value)
//    }

    val coroutineScope = rememberCoroutineScope()
    val swipeRange = with(LocalDensity.current) { (48.dp).toPx() }


    val anchors = mapOf(0f to "normal", -swipeRange to "delete")
    val swipeableState = rememberSwipeableState("normal")


//    val sizeDp by remember {
//        derivedStateOf { with(LocalDensity.current) { sizePx.toDp() } }
//    }

    var visible by remember {
        mutableStateOf(true)
    }


    var sizePx by remember {
        mutableStateOf(0)
    }

    val anim: Dp by animateDpAsState(
        targetValue = if (visible) LocalDensity.current.run { sizePx.toDp() } else 0.dp,
        finishedListener = {
            if (it == 0.dp) {
                remove.invoke()
                visible = true
            }
        }
    )


    val heightModifier by remember {
        derivedStateOf { if (visible) Modifier else Modifier.height(anim) }
    }

//    AnimatedVisibility(visible = visible) {
//
//
//    }

    Box(modifier = Modifier
//        .offset(y = anim)
//        .height(anim)
        .then(heightModifier)
        .swipeable(
            state = swipeableState,
            anchors = anchors,
            thresholds = { _, _ -> FractionalThreshold(1f) },
            orientation = Orientation.Horizontal
        )
        .focusable()
        .onFocusChanged {
            coroutineScope.launch {
                swipeableState.animateTo("normal")
            }
        }
        .onGloballyPositioned {
            sizePx = it.size.height
        }
    ) {

        val iconScale by remember {
            derivedStateOf {
                (-swipeableState.offset.value + swipeRange) / swipeRange / 2
            }
        }

        ClickableIcon(
            painter = painterResource(id = R.drawable.delete),
            modifier = Modifier
                .scale(iconScale)
                .align(Alignment.CenterEnd),
            onClick = {

//                visible = false

                remove.invoke()

                coroutineScope.launch {
                    swipeableState.snapTo("normal")
                }
            })

        Card(
            elevation = 3.dp,
            modifier = Modifier
                .padding(horizontal = 2.dp, vertical = 4.dp)
                .heightIn(min = 48.dp)
//                .offset(y = anim)
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) },
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .padding(4.dp)
            ) {
                Text(
                    text = word.value.english,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1F)
                        .padding(horizontal = 8.dp),
                    style = Typography().h6
                )
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colors.onBackground
                )

                BasicTextField(
                    value = word.value.chinese,
                    onValueChange = {
                        onValueChange.invoke(it)
//                        currentWord.value = Word(word.value.id, word.value.english, it)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1F)
                        .padding(horizontal = 8.dp)
                        .onFocusChanged { updateWord.invoke() },
                    textStyle = Typography().h6.copy(color = MaterialTheme.colors.onBackground),
                    cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
                )
            }
        }
    }
}
