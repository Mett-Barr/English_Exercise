package com.example.english.data.article

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import com.example.english.data.word.wordlist.itemToString
import com.example.english.data.word.wordlist.room.EmptyWordList
import com.example.english.data.word.wordlist.stringToItem
import com.example.english.data.word.wordlist.wordListToTable
import com.example.english.data.word.wordlist.wordListToStringFile
import com.example.english.stringconverter.StringConverter
import com.example.english.translation.json.Translation
import com.example.english.translation.translateArticle
import java.io.BufferedReader
import java.io.InputStreamReader

const val FILE_NAME = "News_"
const val FILE_NAME_CN = "News_CN_"
const val FILE_NAME_Tr = "News_TR_"
const val FILE_NAME_WORDLIST = "News_WordList_"

object FileOperator {
    fun addFile(fileNum: String, draftContent: String, context: Context) {
        val fos = context.openFileOutput(FILE_NAME + fileNum, Context.MODE_PRIVATE)
        val list = StringConverter().stringToList(draftContent)
        list.forEach { fos.write(it.toByteArray()) }
        fos.close()

        val fosCn = context.openFileOutput(FILE_NAME_CN + fileNum, Context.MODE_PRIVATE)
        repeat(list.size) { fosCn.write("\n".toByteArray()) }
        fosCn.close()

        addTranslatedFile(fileNum, draftContent, context)


        // add blank wordList file
        val fosWordList = context.openFileOutput(FILE_NAME_WORDLIST + fileNum, Context.MODE_PRIVATE)
        val wordListString = itemToString(EmptyWordList.emptyWordListItem)
        fosWordList.write(wordListString.toByteArray())
        fosWordList.close()

//        Log.d("!!!", "addFile: ")
    }

    private fun addTranslatedFile(fileNum: String, originContent: String, context: Context) {
        val list = StringConverter().stringToList(originContent)
        translateArticle(fileNum, list, context)
    }

    fun addFileByList(fileNum: String, contentList: List<Translation>, context: Context) {
        val fos = context.openFileOutput(FILE_NAME_Tr + fileNum, Context.MODE_PRIVATE)
        contentList.forEach { fos.write(it.translatedText.toByteArray()) }
        fos.close()
    }

    fun getFile(fileName: String, context: Context): SnapshotStateList<TextFieldValue> {
        val fos = context.openFileInput(fileName)
        val reader = BufferedReader(InputStreamReader(fos))
        val tmpList = reader.readLines()
        val currentContent = emptyList<TextFieldValue>().toMutableStateList()
        tmpList.forEach {
            currentContent.add(TextFieldValue(it))
        }
        return currentContent
    }

    fun getWordListFile(
        fileName: String,
        newsSize: Int,
        context: Context
    ): SnapshotStateList<SnapshotStateList<Int>> {
        val fos = context.openFileInput(fileName)
        val reader = BufferedReader(InputStreamReader(fos))
        val wordListItemString = reader.readText()
        val wordListItem = stringToItem(wordListItemString)
        return wordListToTable(wordListItem, newsSize)
    }



    fun getFileCn(fileNameCn: String, context: Context): SnapshotStateList<TextFieldValue> {
        val fosCn = context.openFileInput(fileNameCn)
        val readerCn = BufferedReader(InputStreamReader(fosCn))
        val tmpListCn = readerCn.readLines()
        val currentContentCn = emptyList<TextFieldValue>().toMutableStateList()
        tmpListCn.forEach {
            currentContentCn.add(TextFieldValue(it))
        }
        return currentContentCn
    }

    fun saveFile(
        fileName: String,
        currentContent: SnapshotStateList<TextFieldValue>,
        context: Context,
    ) {
        val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        currentContent.forEach {
            fos.write(it.text.toByteArray())
        }
//        val list = StringConverter().stringToList(draftContent.text)
//        list.forEach { fos.write(it.toByteArray()) }
        fos.close()

    }

fun saveFileCn(
        fileNameCn: String,
        currentContentCn: SnapshotStateList<TextFieldValue>,
        context: Context,
    ) {
        val fosCn = context.openFileOutput(fileNameCn, Context.MODE_PRIVATE)
        currentContentCn.forEach {
            fosCn.write(it.text.toByteArray())
        }
        //        repeat(list.size) { fosCn.write("\n".toByteArray())}
        fosCn.close()
    }

    fun saveCurrentFile(
        currentFileName: String,
        currentFileNameCn: String,
        currentFileNameTr: String,
        currentFileNameWordList: String,

        currentContent: SnapshotStateList<TextFieldValue>,
        currentContentCn: SnapshotStateList<TextFieldValue>,
        currentContentTr: SnapshotStateList<TextFieldValue>,
        currentContentWordList: SnapshotStateList<SnapshotStateList<Int>>,

        context: Context,
    ) {
        val fos = context.openFileOutput(currentFileName, Context.MODE_PRIVATE)
        currentContent.forEach {
            fos.write(it.text.plus("\n").toByteArray())
        }
//        val list = StringConverter().stringToList(draftContent.text)
//        list.forEach { fos.write(it.toByteArray()) }
        fos.close()

        val fosCn = context.openFileOutput(currentFileNameCn, Context.MODE_PRIVATE)
        currentContentCn.forEach {
            fosCn.write(it.text.plus("\n").toByteArray())
        }
//        repeat(list.size) { fosCn.write("\n".toByteArray())}
        fosCn.close()


        val fosTr = context.openFileOutput(currentFileNameTr, Context.MODE_PRIVATE)
        currentContentTr.forEach {
            fosTr.write(it.text.plus("\n").toByteArray())
        }
//        repeat(list.size) { fosCn.write("\n".toByteArray())}
        fosTr.close()



        // save wordList file
        val fosWordList = context.openFileOutput(currentFileNameWordList, Context.MODE_PRIVATE)
        val wordListForStringFile = wordListToStringFile(currentContentWordList.toList())
        fosWordList.write(wordListForStringFile.toByteArray())

        fosWordList.close()
    }
}