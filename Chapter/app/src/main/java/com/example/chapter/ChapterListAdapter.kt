package com.example.chapter

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chapter.databinding.ListItemChapterBinding
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class ChapterHolder (
    val binding: ListItemChapterBinding
):  RecyclerView.ViewHolder(binding.root) {
    fun bind(chapter: Chapter, onChapterClicked: (chapterId: UUID) -> Unit) {
        binding.chapterTitle.text = chapter.translationText
        binding.foreignText.text = chapter.strangeText
        binding.date.text = formatDate(chapter.lastUpdated)
        binding.root.setOnClickListener {
            onChapterClicked(chapter.id)
        }
    }

    private fun formatDate(date: Date): CharSequence {
        //date.toGMTString();
        val datef = date.toString()
        if (DateUtils.isToday(date.time)) {

            return if (Integer.parseInt(datef.subSequence(11, 13) as String) > 12) {
                var part1 = Integer.parseInt(datef.subSequence(11,13) as String);
                part1 = 24 - part1
                part1.toString()+datef.subSequence(13,16)+" PM";
            } else {
                (datef.subSequence(11, 16)).toString()+" AM";
            }
            // DateUtils.getRelativeTimeSpanString(date.time)
            //return "Today"
        } else {
            Log.d("Today", datef)
            return datef.subSequence(0, 10);

        }
        // USE IF TO CHECK IF THE ENTERED DATE IS TODAY: MAKE
        // RETURN THE TIME
    }
}

class ChapterListAdapter (
    private val chapters: List<Chapter>,
    private val onChapterClicked: (chapterId: UUID) -> Unit
): RecyclerView.Adapter<ChapterHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemChapterBinding.inflate(inflater, parent, false)
        return ChapterHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterHolder, position: Int) {
        val chapter = chapters[position]
        holder.bind(chapter, onChapterClicked)
    }
    override fun getItemCount() = chapters.size


}