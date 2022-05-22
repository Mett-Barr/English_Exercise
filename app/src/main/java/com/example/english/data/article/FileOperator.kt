package com.example.english.data.article

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import com.example.english.data.word.wordlist.WordListTable
import com.example.english.data.word.wordlist.stringToWordListTable
import com.example.english.data.word.wordlist.wordListTableToString
import com.example.english.stringconverter.StringConverter
import com.example.english.translation.json.Translation
import com.example.english.translation.translateArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

const val FILE_NAME = "News_"
const val FILE_NAME_CN = "News_CN_"
const val FILE_NAME_Tr = "News_TR_"
const val FILE_NAME_WORDLISTTABLE = "News_WordListTable_"

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

        // add blank wordListTable file
        val fosWordListTable = context.openFileOutput(FILE_NAME_WORDLISTTABLE + fileNum, Context.MODE_PRIVATE)
        val wordListTable: MutableList<List<Int>> = mutableListOf(emptyList())
        repeat(list.size) {
            wordListTable.add(emptyList())
        }
        val wordListTableString = wordListTableToString(WordListTable(wordListTable))
        fosWordListTable.write(wordListTableString.toByteArray())
        fosWordListTable.close()
    }

    private fun addTranslatedFile(fileNum: String, originContent: String, context: Context) {
        val list = StringConverter().stringToList(originContent)
        translateArticle(fileNum, list, context)
    }


    // for file Tr
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

    fun getWordListTableFile(
        fileName: String,
        context: Context
    ): SnapshotStateList<SnapshotStateList<Int>> {
        val fos = context.openFileInput(fileName)
        val reader = BufferedReader(InputStreamReader(fos))
        val wordListTableString = reader.readText()
        val wordListTable = stringToWordListTable(wordListTableString).wordListTable
        val list: SnapshotStateList<SnapshotStateList<Int>> = emptyList<SnapshotStateList<Int>>().toMutableStateList()
        wordListTable.forEach {
            list.add(it.toMutableStateList())
        }
        return list
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

    fun saveCurrentFile(
        currentFileName: String,
        currentFileNameCn: String,
        currentFileNameTr: String,
        currentFileNameWordListTable: String,

        currentContent: SnapshotStateList<TextFieldValue>,
        currentContentCn: SnapshotStateList<TextFieldValue>,
        currentContentTr: SnapshotStateList<TextFieldValue>,
        wordListTable: SnapshotStateList<SnapshotStateList<Int>>,

        context: Context,
    ) {
        val fos = context.openFileOutput(currentFileName, Context.MODE_PRIVATE)
        currentContent.forEach {
            fos.write(it.text.plus("\n").toByteArray())
        }
        fos.close()

        val fosCn = context.openFileOutput(currentFileNameCn, Context.MODE_PRIVATE)
        currentContentCn.forEach {
            fosCn.write(it.text.plus("\n").toByteArray())
        }
        fosCn.close()


        val fosTr = context.openFileOutput(currentFileNameTr, Context.MODE_PRIVATE)
        currentContentTr.forEach {
            fosTr.write(it.text.plus("\n").toByteArray())
        }
        fosTr.close()

        // save wordListTable
        val fosWordListTable = context.openFileOutput(currentFileNameWordListTable, Context.MODE_PRIVATE)
        val wordListTableForStringFile = wordListTableToString(WordListTable(wordListTable))
        fosWordListTable.write(wordListTableForStringFile.toByteArray())
        fosWordListTable.close()
    }



    // Delete
    suspend fun deleteCurrentFile(fileNum: String, context: Context) {
        withContext(Dispatchers.IO) {
            context.apply {
                val fileName = FILE_NAME + fileNum
                deleteFile(fileName)

                val fileNameCn = FILE_NAME_CN + fileNum
                deleteFile(fileNameCn)

                val fileNameTr = FILE_NAME_Tr + fileNum
                deleteFile(fileNameTr)

                val fileNameWordListTable = FILE_NAME_WORDLISTTABLE + fileNum
                deleteFile(fileNameWordListTable)
            }
        }
    }
}