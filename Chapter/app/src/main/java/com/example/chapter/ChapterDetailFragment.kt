package com.example.chapter

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chapter.databinding.FragmentChapterDetailBinding
import kotlinx.coroutines.launch
import java.util.*
// stop fields from being blank

class ChapterDetailFragment: Fragment() {

    private val args: ChapterDetailFragmentArgs by navArgs()
    private val chapterDetailViewModel: ChapterDetailViewModel by viewModels {
        ChapterDetailViewModelFactory(args.chapterId)
    }
    private lateinit var binding: FragmentChapterDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChapterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_chapter_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        deleteChapter()
        return super.onOptionsItemSelected(item)
    }
    private fun deleteChapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            chapterDetailViewModel.chapter.value?.let { chapter ->
                chapterDetailViewModel.deleteChapter(
                    chapter = chapter
                )
            }
            findNavController().popBackStack()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity?.onBackPressedDispatcher?.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (chapterDetailViewModel.chapter.value?.translationText == "") {
                    Toast.makeText(context, "Crime Title can't be empty", Toast.LENGTH_SHORT).show()
                } else {
                    findNavController().popBackStack()
                }
            }
        })

    }
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    binding.apply {

        //chapterTitle.doOnTextChanged {
        translationtext.doOnTextChanged {
                text, _,_,_ ->
                chapterDetailViewModel.updateChapter { oldChapter ->
                    oldChapter.copy(translationText = text.toString())
            }
        }
        foreignText.doOnTextChanged {
                text, _, _, _ ->
                chapterDetailViewModel.updateChapter { oldChapter ->
                oldChapter.copy(strangeText = text.toString())
            }
        }

        languageText.doOnTextChanged { text, _, _, _ ->
                chapterDetailViewModel.updateChapter { oldChapter ->
                    oldChapter.copy(language = text.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                chapterDetailViewModel.chapter.collect { chapter ->
                    chapter?.let { updateUi(it) }
                }
            }
        }

        lastUpdated.apply {

            isEnabled = false
        }
    }

}
    // leaves last digit
    private fun updateUi(chapter: Chapter) {
        binding.apply {
            if(translationtext.text.toString() != chapter.translationText) {
                translationtext.setText(chapter.translationText)
                foreignText.setText(chapter.strangeText)
                languageText.setText(chapter.language)
            }
            lastUpdated.text = chapter.lastUpdated.toString()

        }
    }
}